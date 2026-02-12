package com.aideesigns.backend.order.service

import com.aideesigns.backend.delivery.repository.DeliveryRepository
import com.aideesigns.backend.notification.events.PaymentConfirmedEvent
import com.aideesigns.backend.order.dto.*
import com.aideesigns.backend.order.entity.DeliveryAddress
import com.aideesigns.backend.order.entity.Order
import com.aideesigns.backend.order.entity.OrderItem
import com.aideesigns.backend.order.entity.Payment
import com.aideesigns.backend.order.repository.OrderRepository
import com.aideesigns.backend.payment.repository.PaymentRepository
import com.aideesigns.backend.payment.service.PaymentInitRequest
import com.aideesigns.backend.payment.service.PaystackGatewayService
import com.aideesigns.backend.payment.service.FlutterwaveGatewayService
import com.aideesigns.backend.product.repository.ProductRepository
import com.aideesigns.backend.product.repository.ProductSizeRepository
import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentGateway
import com.aideesigns.backend.shared.enums.PaymentStatus
import com.aideesigns.backend.shared.exception.DomainException
import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val deliveryRepository: DeliveryRepository,
    private val productRepository: ProductRepository,
    private val productSizeRepository: ProductSizeRepository,
    private val paystackService: PaystackGatewayService,
    private val flutterwaveService: FlutterwaveGatewayService,
    private val eventPublisher: ApplicationEventPublisher,
    @Value("\${app.payment.callback-url}") private val callbackUrl: String
) {

    // ─── Public ────────────────────────────────────────────────────────────────

    @Transactional
    fun createOrder(request: CreateOrderRequest): CreateOrderResponse {

        // Validate items and calculate total
        val resolvedItems = request.items.map { item ->
            val product = productRepository.findByIdAndIsDeletedFalse(item.productId)
                ?: throw ResourceNotFoundException("Product not found: ${item.productId}")

            if (!product.isAvailable) {
                throw DomainException("Product '${product.name}' is currently unavailable.")
            }

            val size = productSizeRepository.findAllByProductId(item.productId)
                .find { it.sizeLabel == item.sizeLabel }
                ?: throw DomainException(
                    "Size '${item.sizeLabel}' is not available for product '${product.name}'."
                )

            if (size.stockQuantity < item.quantity) {
                throw DomainException(
                    "Insufficient stock for '${product.name}' in size '${item.sizeLabel}'. " +
                    "Available: ${size.stockQuantity}, Requested: ${item.quantity}."
                )
            }

            Triple(product, size, item)
        }

        val itemsTotal = resolvedItems.sumOf { (product, _, item) ->
            product.price.multiply(BigDecimal(item.quantity))
        }

        val deliveryFee = calculateDeliveryFee(request.deliveryAddress.state)
        val totalAmount = itemsTotal.add(deliveryFee)

        // Create order
        val order = Order(
            customerName = request.customerName,
            phone = request.phone,
            email = request.email,
            totalAmount = totalAmount
        )
        orderRepository.save(order)

        // Attach order items and decrement stock
        resolvedItems.forEach { (product, size, item) ->
            val orderItem = OrderItem(
                order = order,
                productId = product.id,
                sizeLabel = item.sizeLabel,
                quantity = item.quantity,
                unitPrice = product.price
            )
            order.items.add(orderItem)

            // Decrement stock
            size.stockQuantity -= item.quantity
            productSizeRepository.save(size)
        }

        // Attach delivery address
        val delivery = DeliveryAddress(
            order = order,
            addressLine = request.deliveryAddress.addressLine,
            city = request.deliveryAddress.city,
            state = request.deliveryAddress.state,
            deliveryFee = deliveryFee,
            contactPhone = request.deliveryAddress.contactPhone
        )
        order.deliveryAddress = delivery

        orderRepository.save(order)

        // Initialize payment
        val customerEmail = request.email
            ?: "${request.phone}@aideesigns.placeholder.com"

        val gatewayService = when (request.gateway) {
            PaymentGateway.PAYSTACK -> paystackService
            PaymentGateway.FLUTTERWAVE -> flutterwaveService
            PaymentGateway.MANUAL -> throw DomainException(
                "Manual payment must be confirmed by admin. Use PAYSTACK or FLUTTERWAVE."
            )
        }

        val paymentInit = gatewayService.initializePayment(
            PaymentInitRequest(
                orderId = order.id,
                email = customerEmail,
                amount = totalAmount,
                gateway = request.gateway,
                callbackUrl = callbackUrl
            )
        )

        val payment = Payment(
            order = order,
            gateway = request.gateway,
            gatewayReference = paymentInit.reference,
            amount = totalAmount,
            status = PaymentStatus.PENDING
        )
        order.payment = payment
        orderRepository.save(order)

        eventPublisher.publishEvent(PaymentConfirmedEvent(this, order))

        return CreateOrderResponse(
            order = order.toResponse(),
            paymentAuthorizationUrl = paymentInit.authorizationUrl,
            paymentReference = paymentInit.reference
        )
    }

    fun trackOrder(id: UUID, phone: String?, email: String?): OrderResponse {
        if (phone == null && email == null) {
            throw DomainException("Please provide either your phone number or email to track your order.")
        }

        val order = when {
            phone != null -> orderRepository.findByIdAndPhone(id, phone)
            else -> orderRepository.findByIdAndEmail(id, email!!)
        } ?: throw ResourceNotFoundException("Order not found. Please check your order ID and contact details.")

        return order.toResponse()
    }

    // ─── Admin ─────────────────────────────────────────────────────────────────

    fun getAllOrders(): List<OrderResponse> =
        orderRepository.findAllByOrderByCreatedAtDesc().map { it.toResponse() }

    fun getOrderById(id: UUID): OrderResponse =
        findOrderOrThrow(id).toResponse()

    fun getOrdersByPaymentStatus(status: PaymentStatus): List<OrderResponse> =
        orderRepository.findAllByPaymentStatus(status).map { it.toResponse() }

    fun getOrdersByFulfillmentStatus(status: FulfillmentStatus): List<OrderResponse> =
        orderRepository.findAllByFulfillmentStatus(status).map { it.toResponse() }

    @Transactional
    fun updateFulfillmentStatus(id: UUID, request: FulfillmentStatusUpdateRequest): OrderResponse {
        val order = findOrderOrThrow(id)
        order.fulfillmentStatus = request.status
        order.updatedAt = Instant.now()
        return orderRepository.save(order).toResponse()
    }

    @Transactional
    fun manualPaymentConfirm(id: UUID): OrderResponse {
        val order = findOrderOrThrow(id)

        if (order.paymentStatus == PaymentStatus.PAID) {
            throw DomainException("Order is already marked as paid.")
        }

        order.paymentStatus = PaymentStatus.PAID
        order.fulfillmentStatus = FulfillmentStatus.CONFIRMED
        order.updatedAt = Instant.now()

        order.payment?.let {
            it.status = PaymentStatus.PAID
            it.paidAt = Instant.now()
        } ?: run {
            val manualPayment = Payment(
                order = order,
                gateway = PaymentGateway.MANUAL,
                amount = order.totalAmount,
                status = PaymentStatus.PAID,
                paidAt = Instant.now()
            )
            order.payment = manualPayment
        }

        return orderRepository.save(order).toResponse()
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private fun calculateDeliveryFee(state: String): BigDecimal {
        // Basic flat-rate delivery fee by state — can be expanded later
        return when (state.lowercase().trim()) {
            "lagos", "lagos state" -> BigDecimal("2000.00")
            "abuja", "fct", "federal capital territory" -> BigDecimal("3500.00")
            else -> BigDecimal("5000.00")
        }
    }

    private fun findOrderOrThrow(id: UUID): Order =
        orderRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Order not found with id: $id") }

    private fun Order.toResponse() = OrderResponse(
        id = id,
        customerName = customerName,
        phone = phone,
        email = email,
        totalAmount = totalAmount,
        paymentStatus = paymentStatus,
        fulfillmentStatus = fulfillmentStatus,
        items = items.map { item ->
            OrderItemResponse(
                id = item.id,
                productId = item.productId,
                sizeLabel = item.sizeLabel,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
                subtotal = item.subtotal
            )
        },
        deliveryAddress = deliveryAddress?.let {
            DeliveryAddressResponse(
                addressLine = it.addressLine,
                city = it.city,
                state = it.state,
                deliveryFee = it.deliveryFee,
                contactPhone = it.contactPhone
            )
        },
        payment = payment?.let {
            PaymentResponse(
                id = it.id,
                gateway = it.gateway,
                gatewayReference = it.gatewayReference,
                amount = it.amount,
                status = it.status,
                paidAt = it.paidAt,
                createdAt = it.createdAt
            )
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

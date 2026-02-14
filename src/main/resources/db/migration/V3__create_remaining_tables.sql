-- Admins
CREATE TABLE admins (
    id            UUID         PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(50)  NOT NULL DEFAULT 'ADMIN',
    created_at    TIMESTAMPTZ  NOT NULL
);

-- Production slots
CREATE TABLE production_slots (
    id              UUID    PRIMARY KEY,
    production_date DATE    NOT NULL UNIQUE,
    max_capacity    INTEGER NOT NULL,
    booked_count    INTEGER NOT NULL DEFAULT 0,
    is_closed       BOOLEAN NOT NULL DEFAULT FALSE
);

-- Bookings
CREATE TABLE bookings (
    id                    UUID         PRIMARY KEY,
    customer_name         VARCHAR(255) NOT NULL,
    phone                 VARCHAR(20)  NOT NULL,
    email                 VARCHAR(255),
    outfit_type           VARCHAR(100) NOT NULL,
    inspiration_image_url TEXT,
    notes                 TEXT,
    production_slot_id    UUID         REFERENCES production_slots(id),
    status                VARCHAR(50)  NOT NULL DEFAULT 'PENDING',
    created_at            TIMESTAMPTZ  NOT NULL
);

-- Orders
CREATE TABLE orders (
    id                 UUID          PRIMARY KEY,
    customer_name      VARCHAR(255)  NOT NULL,
    phone              VARCHAR(20)   NOT NULL,
    email              VARCHAR(255),
    total_amount       NUMERIC(12,2) NOT NULL,
    payment_status     VARCHAR(50)   NOT NULL DEFAULT 'PENDING',
    fulfillment_status VARCHAR(50)   NOT NULL DEFAULT 'PENDING',
    created_at         TIMESTAMPTZ   NOT NULL,
    updated_at         TIMESTAMPTZ   NOT NULL
);

-- Order items
CREATE TABLE order_items (
    id         UUID          PRIMARY KEY,
    order_id   UUID          NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID          NOT NULL REFERENCES products(id),
    size_label VARCHAR(20)   NOT NULL,
    quantity   INTEGER       NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL
);

-- Payments
CREATE TABLE payments (
    id                UUID          PRIMARY KEY,
    order_id          UUID          NOT NULL REFERENCES orders(id),
    gateway_reference VARCHAR(255),
    amount            NUMERIC(12,2) NOT NULL,
    status            VARCHAR(50)   NOT NULL DEFAULT 'PENDING',
    payment_method    VARCHAR(50),
    paid_at           TIMESTAMPTZ,
    created_at        TIMESTAMPTZ   NOT NULL
);

-- Delivery addresses
CREATE TABLE delivery_addresses (
    id            UUID          PRIMARY KEY,
    order_id      UUID          NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    address_line  TEXT          NOT NULL,
    city          VARCHAR(100)  NOT NULL,
    state         VARCHAR(100)  NOT NULL,
    delivery_fee  NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    contact_phone VARCHAR(20)   NOT NULL
);

-- Testimonials
CREATE TABLE testimonials (
    id            UUID         PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    message       TEXT         NOT NULL,
    rating        INTEGER      CHECK (rating BETWEEN 1 AND 5),
    is_approved   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ  NOT NULL
);

-- CMS pages
CREATE TABLE content_pages (
    id         UUID         PRIMARY KEY,
    slug       VARCHAR(100) NOT NULL UNIQUE,
    title      VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
);

-- CMS content blocks
CREATE TABLE content_blocks (
    id         UUID         PRIMARY KEY,
    page_id    UUID         NOT NULL REFERENCES content_pages(id) ON DELETE CASCADE,
    block_key  VARCHAR(100) NOT NULL,
    block_type VARCHAR(50)  NOT NULL,
    content    TEXT,
    image_url  TEXT,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,
    UNIQUE (page_id, block_key)
);

-- Indexes
CREATE INDEX idx_bookings_status       ON bookings(status);
CREATE INDEX idx_bookings_slot         ON bookings(production_slot_id);
CREATE INDEX idx_orders_payment        ON orders(payment_status);
CREATE INDEX idx_orders_fulfillment    ON orders(fulfillment_status);
CREATE INDEX idx_order_items_order     ON order_items(order_id);
CREATE INDEX idx_payments_order        ON payments(order_id);
CREATE INDEX idx_payments_gateway_ref  ON payments(gateway_reference);
CREATE INDEX idx_delivery_order        ON delivery_addresses(order_id);
CREATE INDEX idx_testimonials_approved ON testimonials(is_approved);
CREATE INDEX idx_content_blocks_page   ON content_blocks(page_id);
CREATE INDEX idx_production_slots_date ON production_slots(production_date);
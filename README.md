# Aideesigns Backend

Aideesigns Backend is a production‑ready Spring Boot application built with Kotlin. It powers the booking, ordering, payment, CMS, and administrative workflows for the Aideesigns fashion platform.

The system is designed with scalability, maintainability, and operational clarity in mind, following a feature‑based architecture and modern backend best practices.

---

## 🚀 Tech Stack

- **Language:** Kotlin
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Migrations:** Flyway
- **Security:** Spring Security (JWT-ready)
- **Documentation:** OpenAPI / Swagger
- **Monitoring:** Spring Boot Actuator
- **Email:** Spring Mail
- **Build Tool:** Gradle (Kotlin DSL)

---

## 📁 Project Structure

The project follows a **feature-first architecture**, where each domain contains its controllers, services, repositories, and entities.

```
com.aideesigns.backend
│
├── admin
├── auth
├── booking
├── cms
├── delivery
├── notification
├── order
├── payment
├── product
├── testimonial
│
├── config        # Global configuration
├── shared        # Cross-cutting concerns (exceptions, base classes, enums)
└── AideesignsApplication.kt
```

This structure improves modularity and makes the codebase easier to scale as the system grows.

---

## ⚙️ Prerequisites

Before running the application, ensure you have:

- Java **17+**
- PostgreSQL **14+** (16 recommended)
- Gradle (or use the included wrapper)

---

## 🗄️ Local Database Setup

Create a database and user locally:

```sql
CREATE DATABASE aideesigns_db;

CREATE USER aideesigns_user WITH PASSWORD 'devpassword';

GRANT ALL PRIVILEGES ON DATABASE aideesigns_db TO aideesigns_user;
```

---

## 🔧 Configuration

The application uses environment-variable fallbacks for local development.

Create a profile file:

```
src/main/resources/application-local.yaml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/aideesigns_db
    username: aideesigns_user
    password: devpassword
```

Run the app with:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

---

## 🧬 Database Migrations

Flyway automatically runs migrations on startup.

Migration files live in:

```
src/main/resources/db/migration
```

Naming convention:

```
V1__init.sql
V2__create_products.sql
V3__add_indexes.sql
```

**Important:** Never modify an existing migration once it has been applied. Always create a new version.

---

## ▶️ Running the Application

Start the server:

```bash
./gradlew bootRun
```

Default port:

```
http://localhost:8080
```

---

## 📘 API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

This provides interactive API exploration and testing.

---

## ❤️ Health & Monitoring

Actuator endpoints are exposed for operational insight.

Health check:

```
GET /actuator/health
```

Useful for:

- uptime monitoring
- container orchestration
- load balancers

---

## 📬 Email Notifications

The system includes an event-driven notification layer.

Typical flow:

```
Domain Event → Listener → Email Service
```

This keeps business logic decoupled from messaging infrastructure and improves system resilience.

---

## 🔐 Security Notes

- Admin authentication is JWT-ready.
- Password policies and advanced security controls can be expanded as the platform grows.
- Avoid committing secrets to version control — prefer environment variables.

---

## 🧪 Testing

Run tests with:

```bash
./gradlew test
```

The project supports **Testcontainers** for realistic database testing.

---

## 🏗️ Architectural Principles

The backend is built around several guiding principles:

- Feature-based modular design
- Clear domain boundaries
- Schema-first database management
- Event-driven notifications
- Environment-aware configuration
- Minimal premature abstraction

---

## 🚀 Future Enhancements

Potential areas for expansion:

- SMS / push notifications
- Advanced admin roles
- Customer accounts
- Caching layer (Redis)
- Search optimization
- Asynchronous job processing
- Cloud-native deployment

---

## 🤝 Contributing

1. Create a feature branch
2. Write clean, testable code
3. Ensure migrations run successfully
4. Open a pull request

---

## 📄 License

This project is proprietary and intended for internal use unless otherwise specified.

---

## 👨‍💻 Maintainers

Built and maintained by the Aideesigns engineering team.


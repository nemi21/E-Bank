# E-Bank E-Commerce Platform - Backend

A production-ready e-commerce backend system with integrated digital wallet payment processing and JWT authentication, built with Spring Boot and MySQL.

![Java](https://img.shields.io/badge/Java-23-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.6-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JWT](https://img.shields.io/badge/JWT-Enabled-yellow)
![Status](https://img.shields.io/badge/Status-Production%20Ready-success)

## 🚀 Project Overview

Complete REST API for an e-commerce platform featuring JWT authentication, digital wallet payments, order management, and admin analytics dashboard.

**Status:** ✅ Production Ready | **Completed:** December 2024

## 🎯 Key Features

### 🔐 Authentication & Security
- JWT token-based authentication
- Role-based access control (USER/ADMIN)
- BCrypt password encryption
- Automatic token validation and refresh

### 💰 E-Bank Integration
- Digital wallet system for each user
- Deposit/withdraw functionality
- Automatic payment processing
- Transaction history tracking
- Refund system with stock restoration

### 🛍️ E-Commerce Core
- Product catalog with categories
- Inventory management
- Shopping cart functionality
- Order lifecycle management
- Order status tracking (PENDING → PAID → PROCESSING → SHIPPED → DELIVERED)

### 👑 Admin Dashboard
- Revenue analytics and reporting
- Top-selling products analysis
- Order management system
- User statistics
- Product management (CRUD)

## 🛠️ Technology Stack

- **Java 23**
- **Spring Boot 3.2.6**
- **Spring Security with JWT**
- **Spring Data JPA / Hibernate**
- **MySQL 8.0**
- **Maven 3.9.9**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## 📊 Database Schema

### Tables
- `users` - User accounts with roles
- `wallets` - Digital wallet (1-to-1 with users)
- `transactions` - Transaction history
- `products` - Product catalog
- `category` - Product categories
- `orders` - Order records
- `order_item` - Order line items
- `reviews` - Product reviews

## 🔌 API Endpoints

### Authentication
```
POST   /auth/register          - Register new user
POST   /auth/login             - Login (returns JWT)
GET    /auth/validate          - Validate token
```

### Wallet Operations
```
POST   /wallets                              - Create wallet
GET    /wallets/user/{userId}                - Get wallet
POST   /wallets/user/{userId}/deposit        - Deposit money
POST   /wallets/user/{userId}/withdraw       - Withdraw money
GET    /wallets/user/{userId}/transactions   - Transaction history
```

### Products
```
GET    /products               - List all products
POST   /products               - Create product
GET    /products/{id}          - Get product details
PUT    /products/{id}          - Update product
DELETE /products/{id}          - Delete product
GET    /products/search        - Search products
```

### Orders
```
POST   /api/orders/create-and-pay     - Create and pay for order
POST   /api/orders/{id}/cancel        - Cancel order (with refund)
GET    /api/orders/user/{userId}      - User's orders
```

### Admin (ADMIN role required)
```
GET    /admin/analytics/dashboard              - Dashboard stats
GET    /admin/analytics/revenue                - Revenue reports
GET    /admin/analytics/products/top-selling   - Top products
GET    /admin/orders                           - All orders
PUT    /admin/orders/{id}/status               - Update order status
```

## 🚀 Getting Started

### Prerequisites
- Java 23+
- MySQL 8.0+
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone <your-repo-url>
cd ebank-backend
```

2. **Create MySQL database**
```sql
CREATE DATABASE ecommerce_db;
```

3. **Configure application.properties**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password

jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

4. **Build and run**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

### Access API Documentation

Open your browser:
```
http://localhost:8080/swagger-ui.html
```

## 🧪 Testing

### Using Swagger UI
1. Navigate to `http://localhost:8080/swagger-ui.html`
2. Click "Authorize" and login to get JWT token
3. Enter token in authorization field
4. Test all endpoints interactively

### Example Flow
1. Register user → Get JWT token
2. Create wallet with initial balance
3. Create categories and products
4. Create and pay for order
5. Verify wallet balance decreased
6. Check transaction history
7. Test admin analytics

## 🔒 Security Features

- JWT stateless authentication
- BCrypt password hashing
- Role-based endpoint protection
- CORS configuration
- SQL injection prevention (JPA)
- Input validation (Jakarta Bean Validation)

## 📈 Order & Payment Workflow

```
1. User creates order (PENDING)
2. Payment deducted from wallet
3. Stock automatically updated
4. Order status → PAID
5. Admin updates → PROCESSING → SHIPPED → DELIVERED
6. Cancel anytime → Automatic refund + stock restore
```

## 🏗️ Project Structure

```
src/main/java/com/ecommerce/
├── config/              # Security, JWT, CORS, Swagger
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── exception/           # Exception handling
├── model/               # JPA entities
├── repository/          # Data access layer
├── security/            # JWT utilities
└── service/             # Business logic
```

## 🐛 Common Issues

**Issue:** CORS errors from frontend
**Solution:** Ensure `CorsConfig.java` allows your frontend origin

**Issue:** JWT token expired
**Solution:** Login again to get new token

**Issue:** Insufficient balance
**Solution:** Deposit more funds to wallet

## 📊 Project Metrics

- **Total Endpoints:** 35+
- **Lines of Code:** 2500+
- **Database Tables:** 8
- **Response Time:** <100ms average

## 👨‍💻 Author

**Nehemiah Gugssa**
- GitHub: [@nemi21](https://github.com/nemi21)
- LinkedIn: [Nehemiah Gugssa](https://www.linkedin.com/in/nehemiah-gugssa-105920209)
- Email: nemigugssa10@gmail.com

## 📝 License

This project is open source and available under the MIT License.

## 🙏 Acknowledgments

- Built with Spring Boot framework
- JWT implementation with io.jsonwebtoken
- MySQL for database management
- Swagger for API documentation

---

**Last Updated:** December 2024 | **Version:** 2.0.0
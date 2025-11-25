# E-Bank E-Commerce Platform

A full-stack e-commerce backend system with integrated digital wallet payment processing, built with Spring Boot and MySQL.

## 🚀 Project Overview

This project is a comprehensive e-commerce platform that combines traditional online shopping functionality with an integrated digital banking system (E-Bank). Users can manage their digital wallets, make purchases, track transactions, and manage orders - all through a secure REST API.

**Status:** ✅ Production Ready | **Started:** March 2025 | **Completed:** November 2025

---

## 🎯 Key Features

### E-Commerce Core
- **Product Management** - Full CRUD operations with categories and inventory tracking
- **User Management** - Secure user registration and profile management with role-based access
- **Order Processing** - Complete order lifecycle management with status tracking
- **Shopping Cart** - Multi-item order creation with quantity validation
- **Inventory Control** - Automatic stock validation and deduction on purchase

### E-Bank Integration
- **Digital Wallet System** - Personal wallet for each user with balance management
- **Payment Processing** - Seamless integration between orders and wallet payments
- **Transaction History** - Complete audit trail of all financial transactions
- **Balance Validation** - Real-time balance checks before order completion
- **Refund System** - Automatic refunds for cancelled orders with stock restoration

### Technical Features
- **RESTful API** - Clean, well-documented endpoints following REST principles
- **Data Validation** - Comprehensive input validation using Jakarta Bean Validation
- **Exception Handling** - Global exception handling with meaningful error messages
- **Transaction Management** - ACID-compliant database transactions
- **API Documentation** - Interactive Swagger/OpenAPI documentation
- **Security** - BCrypt password encryption and configurable authentication

---

## 🛠️ Technology Stack

**Backend Framework:**
- Java 23
- Spring Boot 3.2.6
- Spring Data JPA
- Spring Security
- Hibernate ORM

**Database:**
- MySQL 8.0
- HikariCP Connection Pooling

**API Documentation:**
- Swagger/OpenAPI 3.0
- SpringDoc OpenAPI

**Build Tools:**
- Maven 3.9.9

**Development:**
- Spring Boot DevTools
- Lombok (code generation)

---

## 📊 Database Schema

### Core Entities
- **users** - User accounts with authentication
- **wallets** - Digital wallet for each user
- **transactions** - Complete transaction history
- **products** - Product catalog
- **category** - Product categorization
- **orders** - Order records
- **order_item** - Order line items

### Key Relationships
- User → Wallet (One-to-One)
- User → Transactions (One-to-Many)
- User → Orders (One-to-Many)
- Order → OrderItems (One-to-Many)
- Product → Category (Many-to-One)
- Transaction → Order (Many-to-One, optional)

---

## 🔌 API Endpoints

### User Management
```
POST   /users              - Create new user
GET    /users              - Get all users
GET    /users/{id}         - Get user by ID
```

### Wallet Operations
```
POST   /wallets                           - Create wallet
GET    /wallets/user/{userId}             - Get user's wallet
GET    /wallets/user/{userId}/balance     - Check balance
POST   /wallets/user/{userId}/deposit     - Deposit money
POST   /wallets/user/{userId}/withdraw    - Withdraw money
GET    /wallets/user/{userId}/transactions - Transaction history
```

### Product & Category Management
```
GET    /products                    - List all products
POST   /products                    - Create product
GET    /products/{id}               - Get product details
PUT    /products/{id}               - Update product
DELETE /products/{id}               - Delete product
GET    /products/category/{id}      - Products by category

GET    /categories                  - List all categories
POST   /categories                  - Create category
```

### Order Processing
```
POST   /api/orders                       - Create order (PENDING)
POST   /api/orders/create-and-pay        - Create and pay immediately
POST   /api/orders/{id}/pay              - Pay for existing order
POST   /api/orders/{id}/cancel           - Cancel order (with refund)
PUT    /api/orders/{id}/status           - Update order status
GET    /api/orders/{id}                  - Get order details
GET    /api/orders/user/{userId}         - User's order history
GET    /api/orders                       - All orders (admin)
```

---

## 🎬 Order & Payment Workflow

### Standard Flow
1. **User Registration** → Create user account
2. **Wallet Creation** → Initialize digital wallet with balance
3. **Browse Products** → View catalog and select items
4. **Create Order** → Order created in PENDING status
5. **Payment** → Wallet balance validated and deducted
6. **Stock Update** → Product inventory automatically reduced
7. **Transaction Record** → Payment logged in transaction history
8. **Order Completion** → Status updated to PAID

### Order Status Lifecycle
```
PENDING → PAID → PROCESSING → SHIPPED → DELIVERED
         ↓
      CANCELLED (if unpaid)
      REFUNDED (if paid and cancelled)
```

### Refund Flow
- Order cancelled by user
- System checks if order was paid
- If paid: money returned to wallet, stock restored
- Transaction recorded as REFUND type

---

## 🚀 Getting Started

### Prerequisites
- Java 23 or higher
- MySQL 8.0 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/ecommerce-ebank.git
cd ecommerce-ebank
```

2. **Create MySQL database**
```sql
CREATE DATABASE ecommerce_db;
```

3. **Configure database connection**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password
```

4. **Build the project**
```bash
./mvnw clean install
```

5. **Run the application**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Access API Documentation

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

---

## 📖 Usage Examples

### 1. Create a User
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "USER"
  }'
```

### 2. Create Wallet with Initial Balance
```bash
curl -X POST "http://localhost:8080/wallets?userId=1&initialBalance=1000.0"
```

### 3. Create and Pay for Order
```bash
curl -X POST http://localhost:8080/api/orders/create-and-pay \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "orderItems": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

### 4. Check Transaction History
```bash
curl http://localhost:8080/wallets/user/1/transactions
```

---

## 🧪 Testing

### Manual Testing via Swagger UI
1. Start the application
2. Navigate to `http://localhost:8080/swagger-ui.html`
3. Use the interactive interface to test all endpoints

### Example Test Flow
1. Create a user → Note the user ID
2. Create a wallet for that user with $1000 initial balance
3. Create a category (e.g., "Electronics")
4. Create a product in that category
5. Create and pay for an order
6. Verify wallet balance decreased
7. Verify product stock decreased
8. Check transaction history

---

## 🏗️ Architecture

### Design Patterns Used
- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic separation
- **DTO Pattern** - Clean API contracts
- **Dependency Injection** - Loose coupling

### Project Structure
```
src/main/java/com/ecommerce/
├── config/              # Security and application configuration
├── controller/          # REST API endpoints
├── dto/                 # Data Transfer Objects
├── exception/           # Custom exceptions and global handler
├── model/               # JPA entities
├── repository/          # Data access layer
└── service/             # Business logic layer
```

---

## 🔒 Security Features

- **Password Encryption** - BCrypt hashing for user passwords
- **Input Validation** - Jakarta Bean Validation on all DTOs
- **SQL Injection Prevention** - JPA/Hibernate parameterized queries
- **CSRF Protection** - Configurable CSRF tokens
- **Exception Handling** - Secure error messages (no sensitive data exposure)

---

## 🐛 Error Handling

The application implements comprehensive error handling:

- **400 Bad Request** - Invalid input data
- **404 Not Found** - Resource doesn't exist
- **409 Conflict** - Insufficient stock or balance
- **500 Internal Server Error** - Server-side errors

Example error response:
```json
{
  "error": "Insufficient wallet balance. Required: 999.99, Available: 500.00"
}
```

---

## 📈 Future Enhancements

- [ ] JWT-based authentication
- [ ] Role-based access control (ADMIN/USER endpoints)
- [ ] Email notifications for orders
- [ ] Product search and filtering
- [ ] Pagination for large result sets
- [ ] Product reviews and ratings
- [ ] Wishlist functionality
- [ ] Order tracking with shipping integration
- [ ] Analytics dashboard
- [ ] Multi-currency support

---

## 🤝 Contributing

This is a personal portfolio project. However, feedback and suggestions are welcome!

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

---

## 🙏 Acknowledgments

- Built with guidance from Claude AI
- Spring Boot community for excellent documentation
- MySQL for reliable database management

---

## 📞 Support

For questions or issues, please open an issue on GitHub or contact me directly.

---

**Last Updated:** November 2025
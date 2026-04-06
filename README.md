# FinSecure Backend 

A comprehensive financial management system backend built with Spring Boot, featuring role-based access control, advanced pagination, enterprise-grade error handling, and production-ready architecture.

## 🏗️ Architecture Overview

### **Backend (Spring Boot 4.0.5)**
- **Framework**: Spring Boot 4.0.5 with Java 17
- **Security**: JWT Authentication with Role-Based Access Control (RBAC)
- **Database**: PostgreSQL with JPA/Hibernate
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Architecture**: Layered architecture with Controller → Service → Repository pattern

## 🚀 Key Features

### 🔐 **Authentication & Authorization**
- **JWT-based authentication** with secure token management
- **Role-based access control**: ADMIN, ANALYST, VIEWER
- **Method-level security** with `@PreAuthorize` annotations
- **Token validation** and automatic refresh

### 📊 **Financial Management**
- **Transaction CRUD operations** with comprehensive validation
- **Advanced filtering** by type, category, date, and user
- **Real-time analytics** and dashboard summaries
- **Category-wise spending analysis**
- **Monthly trends and statistics**

### 📄 **Pagination & Performance**
- **Built-in pagination** for all list endpoints
- **Configurable page size** (default: 20, max: 100)
- **Sorting capabilities** on multiple fields
- **Database optimization** with proper indexes
- **Connection pooling** with HikariCP

### 🛡️ **Enterprise Error Handling**
- **Structured exception handling** with correlation IDs
- **Comprehensive logging** for debugging and monitoring
- **Custom exception types**: `ResourceNotFoundException`, `BusinessException`
- **Consistent error response format** across all endpoints
- **Request tracking** and audit trails

### ✅ **Validation & Business Logic**
- **Comprehensive input validation** with custom error messages
- **Business rule enforcement** (transaction limits, user permissions)
- **Role-based transaction restrictions**
- **Data integrity constraints** at database level

## 📚 API Documentation

### **Base URL**: `http://localhost:8080/api`

### **Authentication Endpoints**
```
POST /users/register    - User registration
POST /users/login       - User authentication
```

### **Dashboard Endpoints (All Roles)**
```
GET /dashboard/summary           - Financial metrics (income, expense, balance)
GET /dashboard/category-summary  - Category-wise spending breakdown
GET /dashboard/monthly-trends    - Monthly financial trends
```

### **Transaction Endpoints**
```
GET /transactions?page=0&size=20&sortBy=createdAt&sortDir=desc    - Get all transactions (Admin/Analyst)
GET /transactions/user/{userId}                              - Get user transactions (Admin/Analyst)
GET /transactions/filter?type=EXPENSE&page=0&size=20         - Filter transactions (Admin/Analyst)
POST /transactions                                           - Create transaction (Admin only)
PUT /transactions/{id}                                       - Update transaction (Admin only)
DELETE /transactions/{id}                                    - Delete transaction (Admin only)
GET /transactions/statistics/{userId}                        - Get user statistics (Admin/Analyst)
```

### **Response Format**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### **Error Response Format**
```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Transaction with id 123 not found",
  "path": "/api/transactions/123"
}
```

## 🛠️ Technology Stack

### **Core Framework**
- **Spring Boot 4.0.5** - Main application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations and ORM
- **Spring Validation** - Input validation framework

### **Database & Persistence**
- **PostgreSQL** - Primary database
- **Hibernate** - ORM provider
- **HikariCP** - Database connection pooling
- **Flyway** - Database migrations (optional)

### **Security & Authentication**
- **JWT (io.jsonwebtoken)** - Token-based authentication
- **BCrypt** - Password encryption
- **Spring Security** - Framework security

### **API & Documentation**
- **SpringDoc OpenAPI 3** - API documentation
- **Swagger UI** - Interactive API explorer
- **Jackson** - JSON serialization/deserialization

### **Utilities & Monitoring**
- **SLF4J + Logback** - Logging framework
- **Lombok** - Code generation
- **Maven** - Build and dependency management

## 📋 Data Models

### **Transaction**
```java
{
  "id": Long,
  "amount": BigDecimal,           // Precision: 10, scale: 2
  "type": "INCOME" | "EXPENSE",
  "date": LocalDate,
  "notes": String (max 500 chars),
  "category": Category,
  "user": User,
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### **User**
```java
{
  "id": Long,
  "name": String (max 100 chars),
  "email": String (unique, max 100 chars),
  "password": String (encrypted),
  "role": "ADMIN" | "ANALYST" | "VIEWER",
  "active": Boolean,
  "createdAt": LocalDateTime,
  "updatedAt": LocalDateTime
}
```

### **Category**
```java
{
  "id": Long,
  "name": String,
  "type": "INCOME" | "EXPENSE"
}
```

## 🔐 Role-Based Access Control

| Feature | VIEWER | ANALYST | ADMIN |
|---------|--------|---------|-------|
| View Dashboard | ✅ | ✅ | ✅ |
| View Analytics | ✅ | ✅ | ✅ |
| Filter Data | ❌ | ✅ | ✅ |
| Create Transactions | ❌ | ❌ | ✅ |
| Edit Transactions | ❌ | ❌ | ✅ |
| Delete Transactions | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |
| Transaction Limits | N/A | $10,000 | Unlimited |

## 🚀 Getting Started

### **Prerequisites**
- **Java 17** or higher
- **Maven 3.8.0** or higher
- **PostgreSQL 16** or higher

### **Database Setup**
```sql
-- Create database
CREATE DATABASE finance_db;

-- Create user (optional)
CREATE USER finance_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE finance_db TO finance_user;
```

### **Configuration**
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### **Run Application**
```bash
# Clone repository
git clone https://github.com/sathish0416/FinSecure.git
cd FinSecure/finance-dashboard

# Compile and run
mvn clean compile
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📖 API Documentation

Once the application is running, visit:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/api-docs`

## 🧪 Testing

### **Run Tests**
```bash
mvn test
```

### **Run with Coverage**
```bash
mvn test jacoco:report
```

### **API Testing Examples**
```bash
# Register a user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"password123","role":"ADMIN"}'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'

# Get transactions (with JWT token)
curl -X GET "http://localhost:8080/api/transactions?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔧 Configuration

### **Application Properties**
Key configuration options in `application.properties`:

```properties
# Pagination
spring.data.web.pageable.default-page-size=20
spring.data.web.pageable.max-page-size=100

# Database Connection Pooling
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# Logging
logging.level.com.financeapp=DEBUG

# JWT
jwt.secret=mySecretKey
jwt.expiration=86400000  # 24 hours
```

### **Database Migration**
Run the migration script to set up the database:
```bash
psql -h localhost -U postgres -d finance_db \
  -f src/main/resources/db/migration/V1__backend_improvements.sql
```

### 📊 Role-Based Dashboards

#### 📖 Viewer Dashboard
- Financial overview (balance, income, expenses)
- Category-wise spending analysis
- Monthly trends visualization
- Read-only access with clear permission indicators

#### 🔍 Analyst Dashboard
- All viewer features +
- Advanced transaction filtering and search
- Data export capabilities (CSV format)
- Enhanced analytics with percentage calculations
- Detailed category and monthly performance metrics

#### ⚙️ Admin Dashboard
- Complete system administration
- Transaction CRUD operations (Create, Read, Update, Delete)
- User management interface with role assignment
- Quick action buttons for common tasks
- Full data access and modification capabilities

### 💼 Financial Management
- **Transaction Management**: Complete CRUD operations for financial records
- **Category Analysis**: Visual breakdown of spending by category
- **Monthly Trends**: Track income and expenses over time
- **Real-time Updates**: Automatic data refresh and synchronization
- **Data Export**: CSV export for external analysis

### 🎨 User Experience
- **Responsive Design**: Optimized for desktop, tablet, and mobile
- **Modern UI**: Glass morphism design with smooth animations
- **Error Handling**: Comprehensive error states and user feedback
- **Loading States**: Professional loading indicators
- **Accessibility**: Semantic HTML and ARIA labels

---

## 🤝 Contributing

Contributions are welcome! If you'd like to improve this project:

1. Fork the repository
2. Create a new branch 
3. Commit your changes
4. Push to your branch
5. Open a Pull Request

---

## 📜 License

This project is licensed under the **MIT License**.

---

## 👨‍💻 Author

**Sathish Madanu**

- GitHub: https://github.com/sathish0416
---


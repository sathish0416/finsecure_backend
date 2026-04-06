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

## 📊 Performance & Monitoring

### **Database Optimizations**
- **Indexes** on frequently queried columns
- **Composite indexes** for complex queries
- **Connection pooling** for optimal performance
- **Query optimization** with proper JPA queries

### **Logging & Monitoring**
- **Structured logging** with correlation IDs
- **Request/response logging** for debugging
- **Performance metrics** tracking
- **Error tracking** with detailed context

## 🔄 Business Logic

### **Transaction Validation**
- **Amount must be positive** (> 0)
- **Date cannot be in future**
- **Category must exist**
- **User must be active**

### **Role-Based Restrictions**
- **VIEWER**: Read-only access to dashboard data
- **ANALYST**: Can view/filter data, max transaction $10,000
- **ADMIN**: Full system access, no transaction limits

### **Data Integrity**
- **Audit trails** with created/updated timestamps
- **Soft validation** with user-friendly error messages
- **Database constraints** for data consistency

## 🚀 Deployment

### **Production Build**
```bash
mvn clean package
java -jar target/finance-dashboard-0.0.1-SNAPSHOT.jar
```

### **Docker Deployment**
```dockerfile
FROM openjdk:17-jre-slim
COPY target/finance-dashboard-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### **Environment Variables**
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/finance_db
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password
export JWT_SECRET=your_production_secret
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the API documentation at `http://localhost:8080/swagger-ui.html`
- Review the logs for detailed error information

## 🔄 Version History

### **v2.0.0** - Enterprise Backend Implementation
- ✅ Complete pagination system with PageResponse wrapper
- ✅ Standardized API responses with ApiResponse wrapper
- ✅ Enhanced validation with comprehensive business rules
- ✅ Enterprise-grade error handling with correlation IDs
- ✅ BigDecimal for all financial calculations
- ✅ Performance optimizations with database indexes
- ✅ Production-ready configuration and monitoring
- ✅ Comprehensive API documentation with Swagger
- ✅ Role-based business logic and security
- ✅ Audit trails and data integrity features

### **v1.0.0** - Initial Implementation
- ✅ Basic Spring Boot setup with JWT authentication
- ✅ Role-based access control (ADMIN, ANALYST, VIEWER)
- ✅ Transaction CRUD operations
- ✅ Basic dashboard endpoints
- ✅ PostgreSQL integration
- **Role-based access control** with three distinct roles:
  - **VIEWER**: Read-only access to financial data
  - **ANALYST**: Advanced analytics and filtering capabilities
  - **ADMIN**: Full system control including user and transaction management

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

## 🚀 Getting Started

### Prerequisites
- **Node.js** 18+ and npm
- **Java 17** and Maven 3.8+
- **PostgreSQL** 16+

### Backend Setup

1. **Clone the repository**
```bash
git clone https://github.com/sathish0416/FinSecure.git
cd FinSecure/finance-dashboard
```

2. **Database Configuration**
```bash
# Create PostgreSQL database
createdb finance_db

# Update database credentials in src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

3. **Run the Backend**
```bash
# Using Maven
mvn spring-boot:run

# Or using Maven Wrapper
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to Frontend Directory**
```bash
cd ../finance-frontend
```

2. **Install Dependencies**
```bash
npm install
```

3. **Start Development Server**
```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User authentication

### Dashboard Endpoints (All Roles)
- `GET /api/dashboard/summary` - Financial metrics
- `GET /api/dashboard/category-summary` - Category-wise data
- `GET /api/dashboard/monthly-trends` - Monthly analytics

### Transaction Endpoints (Role-Restricted)
- `GET /api/transactions` - View transactions (Admin/Analyst)
- `POST /api/transactions` - Create transaction (Admin)
- `PUT /api/transactions/{id}` - Update transaction (Admin)
- `DELETE /api/transactions/{id}` - Delete transaction (Admin)
- `GET /api/transactions/filter` - Filter transactions (Admin/Analyst)

### User Management Endpoints (Admin Only)
- `GET /api/admin/users` - List all users
- `POST /api/users/register` - Create user (with role assignment)

## 🔐 Security Features

- **JWT Authentication**: Stateless token-based authentication
- **Role-Based Access Control**: Granular permissions by user role
- **Password Security**: BCrypt encryption for password storage
- **API Protection**: All sensitive endpoints protected with `@PreAuthorize`
- **Token Management**: Automatic token injection and refresh

## 🎯 Role Permissions

| Feature | VIEWER | ANALYST | ADMIN |
|---------|--------|---------|-------|
| View Dashboard | ✅ | ✅ | ✅ |
| View Analytics | ✅ | ✅ | ✅ |
| Filter Data | ❌ | ✅ | ✅ |
| Export Data | ❌ | ✅ | ✅ |
| Create Transactions | ❌ | ❌ | ✅ |
| Edit Transactions | ❌ | ❌ | ✅ |
| Delete Transactions | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |

## 🛠️ Technology Stack

### Backend
- **Spring Boot 4.0.5** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database
- **JWT (io.jsonwebtoken)** - Token-based authentication
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build tool

### Frontend
- **React 19.2.4** - UI framework
- **Vite 8.0.4** - Build tool and dev server
- **React Router DOM** - Client-side routing
- **Axios** - HTTP client
- **Lucide React** - Icon library
- **jwt-decode** - JWT token parsing

## 📱 Responsive Design

The application is fully responsive and works seamlessly across:
- **Desktop** (1200px+): Full dashboard experience
- **Tablet** (768px-1199px): Optimized layout with collapsible sections
- **Mobile** (320px-767px): Stacked layout with touch-friendly controls

## 🧪 Testing

### Backend Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn test jacoco:report
```

### Frontend Testing
```bash
# Run ESLint
npm run lint

# Build for production
npm run build

# Preview production build
npm run preview
```

## 📦 Deployment

### Backend Deployment
```bash
# Build JAR file
mvn clean package

# Run JAR file
java -jar target/finance-dashboard-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
```bash
# Build for production
npm run build

# Deploy dist/ folder to your web server
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the API documentation at `http://localhost:8080/swagger-ui.html`
- Review the role permissions table above

## 🔄 Version History

- **v1.0.0** - Complete financial management system with role-based access control
  - Full authentication and authorization
  - Three role-based dashboards
  - Transaction management
  - User administration
  - Responsive design
  - Comprehensive error handling

**JnanaGen- An AI-powered quiz platform** backend -> built with Spring Boot, Spring Security, and OpenAI integration

A RESTful API that generates dynamic quizzes using AI, handles secure user authentication, and provides comprehensive score tracking with persistent storage.

## ✨ Core Features

### 🔐 **Enterprise Authentication**
- **JWT-based security** with HTTP-only cookies
- **BCrypt password hashing** for secure credential storage
- **Custom JWT filter** with Spring Security integration
- **Stateless session management** for scalability

### 🤖 **AI Quiz Generation**
- **Spring AI integration** with OpenAI for dynamic content generation
- **Structured data parsing** - Quiz → Questions → Options
- **Database persistence** with JPA entity relationships
- **Real-time quiz creation** via intelligent prompts

### 📊 **Smart Evaluation Engine**
- **Instant score calculation** 
- **Answer validation** against correct options

## 🏗️ Architecture

```
├── controllers/     # REST endpoints & request handling
├── services/        # Business logic & AI integration
├── repositories/    # Data access layer (JPA)
├── entities/        # JPA entities with relationships
├── dtos/            # Data transfer objects
├── security/        # JWT filters & auth configuration
└── config/          # Spring configuration & CORS
```

## 🔧 Tech Stack

| Layer | Technology | Purpose |
|-------|------------|---------|
| **Framework** | Spring Boot 3.x | Core application framework |
| **Security** | Spring Security + JWT | Authentication & authorization |
| **Database** | PostgreSQL + JPA | Data persistence & relationships |
| **AI** | Spring AI + OpenAI | Dynamic quiz generation |
| **Build** | Maven | Dependency management |
| **Language** | Java 21 | Modern Java features |

## 🚀 API Reference

### Authentication
```http
POST /auth/signup          # User registration
POST /auth/login           # User authentication
```

### Quiz Management
```http
POST /user/generateQuiz    # AI-powered quiz creation
POST /user/calculateScore  # Submit answers & get results
GET  /user/getQuizInfo     # Detailed quiz performance
GET  /user/quizzes         # User quiz history
```

## 🔒 Security Features

- **JWT token validation** on every secured endpoint
- **CORS configuration** for frontend integration
- **Password strength enforcement** with BCrypt
- **Request rate limiting** and input validation
- **SQL injection prevention** via JPA parameterized queries

## 📊 Database Schema

```sql
Users ─┐
       ├── Quizzes ─┐
       │            ├── Questions ─── Options
       │            └── QuizResults
       └── UserQuizHistory
```

## 🎯 Production Considerations

- **Stateless design** for horizontal scaling
- **Transaction management** for data consistency
- **Error handling** with proper HTTP status codes
- **DTO pattern** for clean API contracts

## 📦 Quick Start

```bash
# Clone and setup
git clone <repository-url>
cd quiz-app-backend

# Configure environment
cp application.properties.example application.properties
# Set your OpenAI API key and database credentials

# Run application
./mvnw spring-boot:run
```

## 🧪 Testing

All endpoints tested via **Postman** with:
- Authentication flow validation
- AI quiz generation verification
- Score calculation accuracy
- Error handling scenarios

## 📝 Key Implementation Details

- **Custom JWT Filter** extends `OncePerRequestFilter`
- **AI Integration** uses Spring AI's structured output parsing
- **Entity Relationships** properly mapped with JPA annotations
- **Exception Handling** with `@ControllerAdvice`
- **CORS Configuration** for cross-origin frontend communication

---

*Built with enterprise-grade patterns and production-ready architecture*
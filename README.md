## **JnanaGen** 🧠: [See it in action](https://jnana-gen.netlify.app/)
### A full-stack web application that automatically generates intelligent quizzes from user-provided notes using AI technology. The system leverages Spring AI with OpenAI integration to create customized quizzes with multiple-choice questions, explanations, and scoring analysis.

## Project Overview

JnanaGen transforms educational content into interactive quizzes through AI-powered generation. Users can input their study notes or any text content, configure quiz parameters, and receive a comprehensive quiz with instant feedback and detailed explanations.

## Features

### Core Functionality
- AI-powered quiz generation from text notes using OpenAI GPT models
- Customizable quiz parameters (number of questions, answer options, difficulty)
- Real-time quiz taking with interactive interface
- Comprehensive scoring with detailed explanations for each answer
- User authentication and authorization with JWT
- Quiz history management and result tracking
- Responsive design optimized for desktop and mobile devices

### Technical Features
- RESTful API architecture with Spring Boot
- Secure JWT-based authentication with HTTP-only cookies
- PostgreSQL database with JPA/Hibernate ORM
- React TypeScript frontend with modern UI components
- Tailwind CSS with DaisyUI for consistent styling
- CORS-enabled cross-origin communication

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Database**: PostgreSQL with JPA/Hibernate
- **Authentication**: JWT with Spring Security
- **AI Integration**: Spring AI with OpenAI
- **Build Tool**: Maven
- **Additional**: BCrypt password hashing, Bean Validation

### Frontend
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS with DaisyUI components
- **Routing**: React Router DOM
- **HTTP Client**: Fetch API
- **Development**: ESLint with TypeScript support

## Project Structure

```
ai_quiz/
├── backend/
│   ├── src/main/java/com/youngineer/backend/
│   │   ├── controllers/        # REST API endpoints
│   │   ├── services/           # Business logic layer
│   │   ├── models/             # JPA entities
│   │   ├── dto/                # Data transfer objects
│   │   ├── repository/         # Data access layer
│   │   ├── security/           # JWT and authentication
│   │   └── utils/              # Constants and utilities
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile              # Container configuration
└── frontend/
    ├── src/
    │   ├── components/         # React components
    │   ├── services/           # API service layer
    │   └── utils/              # Interfaces and constants
    ├── package.json            # NPM dependencies
    ├── tailwind.config.ts      # Tailwind configuration
    └── vite.config.ts          # Vite build configuration
```

## Local Development Setup

### Prerequisites
- Java 21 or higher
- Node.js 18+ with npm
- PostgreSQL 12+
- OpenAI API key

### Backend Setup

1. **Clone repository and navigate to backend**
```bash
git clone <repository-url>
cd ai_quiz/backend
```

2. **Configure environment variables**
Create `.env` file in backend directory:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ai_quiz
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_AI_OPENAI_API_KEY=your_openai_api_key
JWT_SECRET=your_jwt_secret_minimum_256_bits
FRONTEND_URL=http://localhost:5173
```

3. **Set up PostgreSQL database**
```sql
CREATE DATABASE ai_quiz;
```

4. **Build and run the application**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Backend will be available at: `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd ai_quiz/frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API endpoints for local development**
Update `src/utils/constants.ts`:
```typescript
export const BASE_AUTH_URL = "http://localhost:8080/auth";
export const BASE_QUIZ_URL = "http://localhost:8080/user";
```

4. **Start development server**
```bash
npm run dev
```

Frontend will be available at: `http://localhost:5173`

## API Documentation

### Authentication Endpoints
```
POST /auth/signup
Content-Type: application/json
Body: {
  "username": "string",
  "email": "string", 
  "password": "string"
}

POST /auth/login
Content-Type: application/json
Body: {
  "email": "string",
  "password": "string"
}
```

### Quiz Management Endpoints
```
POST /user/generateQuiz
Authorization: Bearer <jwt-token>
Content-Type: application/json
Body: {
  "userNotes": "string",
  "numQuestions": number,
  "numOptions": number
}

GET /user/loadQuiz/{quizId}
Authorization: Bearer <jwt-token>

POST /user/calculateScore
Authorization: Bearer <jwt-token>
Content-Type: application/json
Body: {
  "quizId": "string",
  "answers": ["string"]
}

GET /user/getUserQuizzes
Authorization: Bearer <jwt-token>

POST /user/getQuizInfo
Authorization: Bearer <jwt-token>
Content-Type: application/json
Body: {
  "quizId": "string"
}
```

## Database Schema

The application uses PostgreSQL with the following entity relationships:

```
Users
├── id (Primary Key)
├── username
├── email
├── password (BCrypt hashed)
└── timestamps

Quizzes
├── id (Primary Key)  
├── title
├── user_id (Foreign Key)
├── ai_generated_content (JSONB)
└── timestamps

Questions
├── id (Primary Key)
├── quiz_id (Foreign Key)
├── question_text
├── correct_answer
└── timestamps

Options
├── id (Primary Key)
├── question_id (Foreign Key)
├── option_text
└── timestamps

Quiz_Results
├── id (Primary Key)
├── quiz_id (Foreign Key)
├── user_answers (JSONB)
├── score
├── total_questions
└── timestamps
```

## Configuration

### Environment Variables

**Backend (.env)**
```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ai_quiz
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password

# AI Configuration
SPRING_AI_OPENAI_API_KEY=sk-...

# Security Configuration
JWT_SECRET=your-256-bit-secret-key

# CORS Configuration
FRONTEND_URL=http://localhost:5173
```

**Frontend (constants.ts)**
```typescript
export const BASE_AUTH_URL = "http://localhost:8080/auth";
export const BASE_QUIZ_URL = "http://localhost:8080/user";
```

### Build Configuration

**Backend (pom.xml)**
- Spring Boot 3.5.3
- Spring AI 1.0.0
- Java 21 target

**Frontend (package.json)**
- React 18
- TypeScript 5
- Vite 5
- Tailwind CSS 3

## Deployment

### Production Environment Variables
```env
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=your_production_database_url
SPRING_AI_OPENAI_API_KEY=your_openai_api_key
JWT_SECRET=secure_production_secret_256_bits
FRONTEND_URL=https://your-frontend-domain.com
```

### Docker Deployment
The backend includes a Dockerfile for containerized deployment:

```bash
# Build Docker image
docker build -t jnanagen-backend .

# Run container
docker run -p 8080:8080 --env-file .env jnanagen-backend
```

### Frontend Build
```bash
# Production build
npm run build

# Preview production build
npm run preview
```

## Security Considerations

- JWT tokens stored in HTTP-only cookies for XSS protection
- BCrypt password hashing with salt rounds
- CORS configuration restricts origins
- Input validation on all API endpoints
- SQL injection prevention through JPA parameterized queries
- OpenAI API key secured in environment variables

## Testing

### Backend Testing
```bash
# Run unit tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

### Frontend Testing
```bash
# Install testing dependencies
npm install --save-dev @testing-library/react @testing-library/jest-dom

# Run tests
npm test
```

## Performance Considerations

- Database connection pooling via Spring Boot defaults
- JPA lazy loading for entity relationships
- Frontend code splitting with Vite
- Optimized Tailwind CSS purging for production builds
- Stateless JWT authentication for horizontal scaling

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify PostgreSQL is running
   - Check database credentials in .env file
   - Ensure database exists

2. **OpenAI API Errors**
   - Verify API key is valid and has credits
   - Check network connectivity
   - Review API rate limits

3. **CORS Errors**
   - Ensure FRONTEND_URL matches your frontend domain
   - Verify CORS configuration in Spring Security

4. **Frontend Build Issues**
   - Clear node_modules and reinstall: `rm -rf node_modules && npm install`
   - Check TypeScript compilation: `npm run type-check`

## License

This project is licensed under the Apache License 2.0.

## Contributors

- **Kartik S** (youngineer@protonmail.com) - Full-stack development

## Additional Information

- GitHub Repository: https://github.com/youngineer/JnanaGen
- Live Demo: https://jnana-gen.netlify.app/
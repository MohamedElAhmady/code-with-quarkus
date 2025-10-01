# User Management API

A RESTful API for user management built with Quarkus and PostgreSQL.

## Features

- Create, read users
- Pagination support
- Email-based user lookup
- Input validation
- Exception handling with JSON error responses

## API Endpoints

- `POST /users` - Create a new user
- `GET /users/{id}` - Get user by ID
- `GET /users` - Get all users with pagination
- `GET /users/by-email/{email}` - Get user by email

## Quick Start

### Prerequisites
- Java 17+
- Docker (for PostgreSQL)

### Run the Application
```bash
./gradlew quarkusDev
```
The API will be available at `http://localhost:8080`

### API Documentation
- Swagger UI: `http://localhost:8080/q/swagger-ui`
- OpenAPI spec: `http://localhost:8080/q/openapi`

### Run Tests
```bash
./gradlew test
```

### Database
The application uses PostgreSQL with Quarkus Dev Services - Docker will automatically start a PostgreSQL container during development.

## Testing
Import the `User_API_Comprehensive_Tests.postman_collection.json` file into Postman for API testing.
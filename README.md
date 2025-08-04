# Spring Base Project

This is a Spring Boot project configured to use a MySQL database named `spring_base`.

## Features
- User authentication and registration
- JWT-based security
- Exception handling
- MySQL database integration

## Getting Started

### Prerequisites
- Java 17 or later
- Maven
- MySQL server

### Database Setup
1. Create a MySQL database named `spring_base`:
   ```sql
   CREATE DATABASE spring_base;
   ```
2. Update the `src/main/resources/application.properties` file with your MySQL username and password:
   ```properties
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

### Build and Run
1. Build the project:
   ```bash
   ./mvnw clean install
   ```
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### API Endpoints
- `/api/auth/register` - Register a new user
- `/api/auth/authenticate` - Authenticate and receive a JWT
- `/api/test` - Test endpoint (secured)

## Project Structure
- `Controllers/` - REST controllers
- `Dtos/` - Data transfer objects
- `Entities/` - JPA entities
- `Repositories/` - Spring Data JPA repositories
- `Services/` - Service layer
- `security/` - Security configuration and JWT utilities

## License
This project is licensed under the MIT License.


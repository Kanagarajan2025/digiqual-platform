# Backend Project Structure

## Directory Structure
```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── digiquaI/
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           └── repository/
│   │   └── resources/
│   │       ├── application.properties
│   └── test/
└── pom.xml
```

## Build and Run
1. Make sure you have [Maven](https://maven.apache.org/) installed.
2. Compile your application using:
   ```bash
   mvn clean install
   ```
3. Run your application:
   ```bash
   mvn spring-boot:run
   ```

## Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Data JPA
- PostgreSQL Driver
- Lombok

## Testing
- You can use JUnit or any other testing framework of your choice to create test cases.
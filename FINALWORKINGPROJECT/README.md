# SprintDB Order Inventory System

## Features (Checklist)
- Java 8 (Streams/Lambdas)
- MySQL Database (SprintDB)
- Spring Boot + Maven
- Spring MVC (Layered: Controller → Service → Repository)
- Spring Data JPA
- DTOs
- Global Exception Handler
- Spring Security (ADMIN + STAFF)
- ResponseEntity APIs
- Mockito/JUnit tests
- JaCoCo code coverage

## Roles
- ADMIN: manage categories, products, customers
- STAFF: view categories/products + record sales (purchases)

## Endpoints (UI)
- /login
- /dashboard
- /categories
- /categories/{id}  (Category → Brand → Product drill-down)
- /admin/categories
- /admin/products/new?categoryId=ID
- /admin/customers
- /staff/purchases/new

## API
- GET /api/categories
- GET /api/products/by-category/{categoryId}

## Credentials
- admin / admin123
- staff / staff123

## Run
1) Create MySQL DB: SprintDB
2) Update application.properties with DB username/password
3) mvn clean test
4) mvn spring-boot:run

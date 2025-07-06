# 🎓 University Backend API

A **Spring Boot REST API** for managing university data — Faculties, Departments, Levels, Courses, Students, and Enrollments.

---

## 📂 Project Structure

```

university/
├── src/
│   ├── main/
│   │   ├── java/com/egabi/university/
│   │   │   ├── controller/      # REST Controllers
│   │   │   ├── dto/             # Data Transfer Objects (DTOs)
│   │   │   ├── entity/          # JPA Entities
│   │   │   ├── exception/       # Custom Exceptions & Handlers
│   │   │   ├── mapper/          # MapStruct Mappers
│   │   │   ├── repository/      # Spring Data JPA Repositories
│   │   │   ├── service/         # Service Interfaces
│   │   │   ├── service/impl/    # Service Implementations
│   │   │   └── UniversityApplication.java # Spring Boot Main Class
│   ├── resources/
│   │   ├── application.properties # DB configuration
│   │   └── data.sql               # Seed data (optional)
└── pom.xml                        # Maven Build File

````

---

## ⚙️ Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL**
- **MapStruct**
- **Lombok**

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21 installed
- PostgreSQL running
- Maven installed

---

### ⚡ Setup & Run

1️⃣ **Clone the repository**
```bash
git clone https://github.com/Omar-26/University_Managment_System.git
cd University_Managment_System
````

2️⃣ **Configure the database**

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/university
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

3️⃣ **Build & Run**

```bash
mvn clean install
mvn spring-boot:run
```

API will run at: `http://localhost:8080`

---

## 📌 API Endpoints Overview

| Resource             | Methods                     |
| -------------------- | --------------------------- |
| **/api/faculties**   | `GET` `POST` `PUT` `DELETE` |
| **/api/departments** | `GET` `POST` `PUT` `DELETE` |
| **/api/levels**      | `GET` `POST` `PUT` `DELETE` |
| **/api/courses**     | `GET` `POST` `PUT` `DELETE` |
| **/api/students**    | `GET` `POST` `PUT` `DELETE` |
| **/api/enrollments** | `GET` `POST` `DELETE`       |

All endpoints follow **REST** principles with proper validation and error handling.

---

## 📌 Example Requests

### ➕ Create Faculty

**Request**

```http
POST /api/faculties
Content-Type: application/json

{
  "name": "Engineering"
}
```

**Response**

```json
{
  "id": 1,
  "name": "Engineering"
}
```

---

### ➖ Delete Course

```http
DELETE /api/courses/{code}
```

Deletes a course by code, only if there are no enrollments.

---

## ⚠️ Error Responses

Errors are returned in a clear format:

```json
{
  "timestamp": "2025-07-05T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Faculty with id 123 not found",
  "errorCode": "FACULTY_NOT_FOUND"
}
```

---

## 🧪 Test with Postman

Test the API easily using Postman 👇

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/33349689-b38c91bc-f365-40b0-8f31-dbf434b0286d?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D33349689-b38c91bc-f365-40b0-8f31-dbf434b0286d%26entityType%3Dcollection%26workspaceId%3Dabf57dda-0ca1-416c-855a-3034349f5efc)
---

## 📜 License

This project is licensed under the MIT License.

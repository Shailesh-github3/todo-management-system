# Task Management System

## Description
A secure, full-stack web application developed using **Spring Boot 3** and **Spring Security**.  
It enables users to register, log in, and manage tasks efficiently. Advanced features include task status tracking, priority categorization, filtering, sorting, and search functionality.  

The application follows **SOLID principles** and a layered architecture to ensure scalability, maintainability, and clean separation of concerns.

---

## Tech Stack
- **Language:** Java 21  
- **Framework:** Spring Boot 3.3.5  
- **Security:** Spring Security 6 (Role-Based Access Control, BCrypt Encryption)  
- **Data Access:** Spring Data JPA (Hibernate)  
- **Database:** MySQL 8  
- **Build Tool:** Maven  
- **Frontend:** Thymeleaf  
- **Backend:** Spring MVC  
- **IDE:** IntelliJ IDEA  

---

## Features

### User Authentication
- Secure user registration and login
- Role-based access control (USER / ADMIN)
- BCrypt password hashing
- Persistent authentication sessions stored in MySQL

### Task Management
- Full CRUD operations (Create, Read, Update, Delete)
- Advanced filtering by:
  - Status
  - Priority
  - Category
  - Keyword search
- Sorting by:
  - Date
  - Status
  - Priority
- Dashboard with task progress tracking

### User Experience
- Persistent Dark Mode using LocalStorage
- Export tasks to CSV format
- Responsive UI built with Bootstrap 5
- Graceful empty-state handling

---

## Database Schema

### Table: `users`
- `id` (Primary Key)
- `username` (Unique, String)
- `password` (Encrypted Hash)
- `role` (String, default: `USER`)

### Table: `tasks`
- `id` (Primary Key)
- `title` (String)
- `description` (String, nullable)
- `status` (Enum)
- `priority` (Enum)
- `category` (Enum)
- `due_date` (LocalDate)
- `user_id` (Foreign Key → users.id)

---

## Project Structure

```

src/
├── main/
│   ├── java/
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── TaskController.java
│   │   ├── models/
│   │   │   ├── Task.java
│   │   │   ├── enums/
│   │   │   │   ├── Priority.java
│   │   │   │   ├── Status.java
│   │   │   │   └── Category.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── tasks.html
│           ├── profile.html
│           ├── login.html
│           └── register.html

````

---

## Installation

### Prerequisites
- Java 21
- Maven 3.9.5 or higher
- MySQL Server 8.0.31 or higher

### Steps

1. Clone the repository:

```
git clone https://github.com/your-username/task-management-system.git
````

2. Open the project in IntelliJ IDEA:

* Open IntelliJ IDEA
* Click **Open**
* Select the project folder
* IntelliJ will automatically detect it as a Maven project

3. Install dependencies:

```
mvn clean install
```

---

## Configuration

Update the MySQL configuration in
`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springboot1
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

spring.sql.init.mode=always
```

---

## Usage

### Run the Application

Execute the following command from the project root directory:

```
mvn spring-boot:run
```

Then open your browser and visit:

```
http://localhost:8080
```

---

## API Documentation

### Authentication & User Endpoints

| Method | URL               | Description                      |
| -----: | ----------------- | -------------------------------- |
|    GET | `/register`       | Displays registration page       |
|   POST | `/register/save`  | Saves a new user to the database |
|    GET | `/login`          | Displays login page              |
|   POST | `/login`          | Authenticates the user           |
|    GET | `/profile`        | Displays user profile            |
|   POST | `/profile/update` | Updates user password            |

---

## Testing

### Manual Test Cases

| Scenario | Description                                                     |
| -------- | --------------------------------------------------------------- |
| Auth     | Register a new user and log in                                  |
| CRUD     | Create, update, and delete a task                               |
| Search   | Search tasks using keywords                                     |
| Filter   | Filter tasks by Priority or Status                              |
| Security | Access `/profile` without login (should redirect to login page) |

---

## Conclusion

This project demonstrates secure authentication, clean backend architecture, and practical task management features using modern Spring Boot best practices.
It is suitable for **portfolio presentation**, **academic evaluation**, and **entry-level backend roles**.

---

```

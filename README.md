# Library-Spring-API
Spring boot API for Library

Spring Boot API for a Book Store system, packed with several features to ensure smooth operations for users and administrators alike.

🔧 Key Features:

User Auth: Secure login, registration, and email verification powered by JWT, Bcrypt for password hashing, and Spring Security.

Book Management: Complete CRUD operations on books with support for cover image uploads and shareable status updates.

Book Borrowing and Returning: Users can borrow and return books, with approval workflows for returned books.

Feedback System: Users can leave feedback on books, which includes a rating and comment section.

Validation: All user inputs are validated , ensuring data integrity across the system.

Custom & Global Exception Handling: Centralized error handling for better debugging and user feedback.

💻 Tech Stack:

Java 21

Spring Boot 3

Spring Data JPA

Hibernate

Spring Security

MySQL as the primary data store

JWT for secure token-based authentication

Spring Mail for ending emails

Thymeleaf for email templete

Springdoc OpenAPI (Swagger) for easy API documentation and testing

🗂 Database Structure:

User: Manages user accounts, roles, and status.

Book: Handles book details, including title, author, ISBN, and ownership.

Feedback: Tracks user feedback on books.

Book Transaction: Records book borrowing and returning transactions.

🌟 Additional Features:

Email Verification: Ensuring valid accounts using email confirmation links.

Password Hashing: Secured with Bcrypt to protect sensitive data.
![DB-Diaghram](https://github.com/user-attachments/assets/a316f649-ca65-42cb-b19b-2270cb5945a2)
![swagger](https://github.com/user-attachments/assets/ef35383a-616e-438c-b67e-79fef1fc25e0)


Custom and Global Exception Handling: Providing consistent and detailed error messages across the application.

Builder Pattern: Implemented for constructing complex objects, enhancing code readability and maintainability.


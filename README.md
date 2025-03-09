# StoreLink

StoreLink is a secure and feature-rich system that provides JWT-based authentication for access control. The Admin CMS allows administrators to manage categories, brands, products, users, and permissions efficiently. Additionally, users can create shops, add products, and search for the nearest shop offering a specific product.

---

## Features

### CMS Features
- User authentication (login, logout)
- Role-based access control for different user permissions
- Manage Products, Brands, Categories, User Permissions, and Admins
- Filtering, Searching, and Pagination for enhanced usability

### API Features
- JWT-based authentication
- Users can create shops and add products
- Search for the nearest shop that has the desired product
- Support for Pagination and Filtering
- Error Logging

---

## Requirements
- **Java** 21 or higher
- **Spring Boot** 3.4.1
- **Maven**
- **PostgreSQL** 17 or higher
- `.env` file for:
  - Database credentials
  - Mail username and password
- `jwt-secret.key` file in the project root

---

## Installation

Follow these steps to set up the project locally:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/hridoyPRGMR/StoreLink.git
   ```
2. **Set Up the Database** (Configure PostgreSQL with required credentials)
   -- for cms login - manually insert user data, password should be encrypted.

3. **Build the Application:**
   ```bash
   mvn clean install
   ```
4. **Package the Application (JAR file):**
  ```bash
  mvn clean package -DskipTests
  ```
5. **Start the Application:**
   Using Docker:
   ```bash
   docker-compose up --build
   ```
---

## Usage

- Access the CMS at: [http://localhost:9090/cms/login](http://localhost:9090/cms/login)

---

## Technologies Used

### Backend:
- **Java 21**
- **Spring Boot**

### Database:
- **PostgreSQL**

### Server-side Frontend:
- **Thymeleaf**

### DevOps:
- **Docker**

---

## API Documentation
For detailed API documentation, visit:  
[Postman Documentation](https://documenter.getpostman.com/view/40331456/2sAYdcrsRd)

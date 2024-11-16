

**E-Commerce Application README**

**Table of Contents**

* [Introduction](#introduction)
* [Features](#features)
* [Technology Stack](#technology-stack)
* [Getting Started](#getting-started)
* [API Endpoints](#api-endpoints)
* [Controllers](#controllers)
* [Services](#services)
* [Data Access](#data-access)
* [Security](#security)
* [Error Handling](#error-handling)
* [Contributing](#contributing)

**Introduction**

This is an e-commerce application built using Java and Spring Boot. It provides a simple and intuitive way to manage products, categories, users, and roles.

**Features**

* Product management: create, read, update, and delete products
* Category management: create, read, update, and delete categories
* User management: create, read, update, and delete users
* Role management: create, read, update, and delete roles
* Authentication and authorization using JSON Web Tokens (JWT)

**Technology Stack**

* Java 8
* Spring Boot 2.x
* MySQL 5.x
* Maven

**Getting Started**

To run the application, follow these steps:

1. Clone the repository to your local machine.
2. Install the required dependencies using Maven.
3. Start the application using the `mvn spring-boot:run` command.
4. Use a tool like Postman or cURL to test the API endpoints.

**API Endpoints**

* **Product Controller**
	+ `POST /public/products`: Create a new product.
	+ `GET /public/products`: Get a list of all products.
	+ `GET /public/products/{id}`: Get a product by ID.
	+ `PUT /public/products/{id}`: Update a product.
	+ `DELETE /public/products/{id}`: Delete a product.
* **Category Controller**
	+ `POST /public/categories`: Create a new category.
	+ `GET /public/categories`: Get a list of all categories.
	+ `GET /public/categories/{id}`: Get a category by ID.
	+ `PUT /public/categories/{id}`: Update a category.
	+ `DELETE /public/categories/{id}`: Delete a category.
* **User Controller**
	+ `POST /public/users`: Create a new user.
	+ `GET /public/users`: Get a list of all users.
	+ `GET /public/users/{id}`: Get a user by ID.
	+ `PUT /public/users/{id}`: Update a user.
	+ `DELETE /public/users/{id}`: Delete a user.
* **Role Controller**
	+ `POST /public/roles`: Create a new role.
	+ `GET /public/roles`: Get a list of all roles.
	+ `GET /public/roles/{id}`: Get a role by ID.
	+ `PUT /public/roles/{id}`: Update a role.
	+ `DELETE /public/roles/{id}`: Delete a role.
* **Auth Controller**
	+ `POST /public/auth/login`: Login a user.
	+ `POST /public/auth/register`: Register a new user.

**Controllers**

* `ProductController`: Handles product-related requests.
* `CategoryController`: Handles category-related requests.
* `UserController`: Handles user-related requests.
* `RoleController`: Handles role-related requests.
* `AuthController`: Handles authentication-related requests.

**Services**

* `ProductService`: Provides business logic for product-related operations.
* `CategoryService`: Provides business logic for category-related operations.
* `UserService`: Provides business logic for user-related operations.
* `RoleService`: Provides business logic for role-related operations.

**Data Access**

* `ProductRepository`: Provides data access for products.
* `CategoryRepository`: Provides data access for categories.
* `UserRepository`: Provides data access for users.
* `RoleRepository`: Provides data access for roles.

**Security**

* Authentication and authorization using JSON Web Tokens (JWT)
* Password encryption using BCrypt

**Error Handling**

* Global exception handler for handling errors and exceptions
* Custom error messages for better error handling

**Contributing**

Pull requests and issues are welcome! If you'd like to contribute to this project, please fork the repository and submit a pull request with your changes.


Based on the code snippets you provided earlier, I've studied the project and identified the controllers and APIs. Here is the corrected README file with the APIs:

**E-Commerce API**

A RESTful API for managing products and categories.

**Table of Contents**

* [Introduction](#introduction)
* [Features](#features)
* [Technology Stack](#technology-stack)
* [Getting Started](#getting-started)
* [API Endpoints](#api-endpoints)
* [Contributing](#contributing)

**Introduction**

This API provides a simple and intuitive way to manage products and categories for an e-commerce platform. It is built using Java and Spring Boot, and uses a MySQL database for data storage.

**Features**

* Product management: create, read, update, and delete products
* Category management: create, read, update, and delete categories

**Technology Stack**

* Java 8
* Spring Boot 2.x
* MySQL 5.x

**Getting Started**

To run the API, follow these steps:

1. Clone the repository to your local machine.
2. Install the required dependencies using Maven.
3. Start the API using the `mvn spring-boot:run` command.
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

**Contributing**

Pull requests and issues are welcome! If you'd like to contribute to this project, please fork the repository and submit a pull request with your changes.
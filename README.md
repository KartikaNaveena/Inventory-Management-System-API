# Inventory-Management-System-API

## Table of Contents
1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Summary](#project-summary)
    - [Entities & Fields](#entities--fields)
    - [Design Choices & Assumptions](#design-choices--assumptions)
    - [Separation of Concerns](#separation-of-concerns)
    - [Error Handling](#error-handling)
    - [Test Cases](#test-cases)
    - [Project Structure](#project-structure)
4. [API Endpoints](#api-endpoints)
5. [Installation & Setup](#installation--setup)
6. [Testing the API](#testing-the-api)
7. [Developer](#developer)

## Project Overview

This project implements a **RESTful backend API** for an **Inventory Management System** as part of the ASE Challenge by Verto.

#### **Product Management**
- Full CRUD (Create, Read, Update, Delete) operations for products.

#### **Inventory Management**
- Increase or decrease product stock with proper validation.
- Handles insufficient stock scenarios with clear error messages.

#### **Additional Features**
- Low stock threshold for products with an endpoint to list all products below this threshold.
- Unit tests covering stock operations and edge cases (e.g., attempting to remove more stock than available).

## Tech Stack

The project is built using the following technologies:

- **Language:** Java 17  
- **Framework:** Spring Boot 3.5.6  
- **Database:** SQLite 3.45.1.0  
- **ORM:** Spring Data JPA with Hibernate 6.6.29.Final  
- **Build Tool:** Maven  
- **Testing:** JUnit 5, Mockito

## Project Summary
### Entities & Fields

#### Product
Represents a product in the warehouse inventory.

| Field Name         | Type    | Required | Updatable | Description |
|-------------------|--------|---------|-----------|-------------|
| id                | Long   | No      | No        | System-generated unique identifier; used internally and not exposed to users |
| name              | String | Yes     | No        | Name of the product |
| variant           | String | No      | No        | Optional product model, edition, or version; combined with name to ensure uniqueness |
| sku               | String | No      | No        | System-generated human-readable unique identifier based on name and variant; used to reference the product externally for users.|
| description       | String | No      | Yes       | Optional information about the product |
| stockQuantity     | Integer| No      | Yes       | Current stock quantity; must be ≥ 0. Defaults to 0 if not provided during creation |
| lowStockThreshold | Integer| Yes     | No        | Threshold for low stock alerts; must be > 0 and required during creation |

### Design Choices & Assumptions
The following table summarizes key design decisions and assumptions made for the Inventory Management System:

| Aspect | Details |
|--------|---------|
| **Identifier Design** | Two identifiers are maintained per product:<br>- **id**: System-generated internal unique identifier, not exposed via APIs. Used internally and for future extensibility (e.g., linking with transaction logs or audit tables).<br>- **sku**: System-generated, human-readable unique identifier derived from `name` and `variant`. Exposed externally for referencing products.<br>This separation hides internal IDs while providing user-friendly references. |
| **Product Naming & Variant Logic** | Products are uniquely identified by a combination of `name` and `variant`.<br>- Allows multiple versions of the same product to coexist (e.g., iPhone 13 vs iPhone 13 Pro).<br>- `variant` is optional, supporting products with variants (electronics) and without (stationery). |
| **Field Mutability & Defaults** | - Only `description` and `stockQuantity` can be updated post-creation.<br>- `stockQuantity` defaults to 0 if not provided.<br>- `lowStockThreshold` is mandatory and must be > 0.<br>- `lowStockThreshold` cannot be updated after creation. |
| **Data Validation & Business Constraints** | - Unique constraint on `(name, variant)` to prevent duplicates.<br>- Stock levels cannot go below 0.<br>- Stock is stored as integers; fractional inputs are truncated to the integer part. |
| **Database Design** | **SQLite** chosen for its lightweight nature and easy local setup, requiring no external dependencies. |


### Separation of Concerns
The project is structured to clearly separate responsibilities across layers:

| Layer | Responsibilities |
|----------------|-----------------|
| **Controller** | - Exposes **REST API endpoints**.<br>- Receives HTTP requests and returns HTTP responses.<br>- Uses **Request DTOs** to receive data and **Response DTOs** to send data, keeping internal entity structure hidden from users.<br>- Delegates all business logic to the **Service Facade**. |
| **Service** | - Contains all **business logic** related to products and inventory.<br>- Individual service classes handle **specific operations** like creation, update, deletion, and queries.<br>- Exposed to controllers only through the **ProductServiceFacade**, ensuring a clean access point. |
| **Repository** | - Handles all **database interactions**.<br>- Abstracts data access from the business logic. |
| **Entity** | - Represents **database tables** as Java classes. |
| **DTO (Data Transfer Objects)** | - Keeps external API representations separate from internal entities.<br>- Includes **Request DTOs** (for input) and **Response DTOs** (for output). |

### Error Handling
The API provides **clear and standardized responses** for both successful operations and errors.

#### Success Responses

| HTTP Status | Description |
|------------|-------------|
| **200 OK** | Request was successful. Used for GET and PATCH endpoints returning a resource. |
| **201 CREATED** | Resource was successfully created. Used for POST endpoints. |
| **204 NO CONTENT** | Resource was successfully deleted. No content returned. |

#### Error Responses

| HTTP Status | Description |
|------------|-------------|
| **400 BAD REQUEST** | Invalid input, missing parameters, or invalid stock value. |
| **404 NOT FOUND** | Requested resource does not exist. |
| **409 CONFLICT** | Conflict when trying to create a resource that already exists. |
| **500 INTERNAL SERVER ERROR** | Unexpected errors such as server crashes or database issues. |

**Key Points:**
- All errors are returned as a standardized `ErrorResponseDTO` with:
  - Timestamp of the error
  - HTTP status code
  - Error message
  - Request path
- Ensures clients receive meaningful and uniform error responses.

### Test Cases

| Test Class | Key Scenarios Tested |
|------------|--------------------|
| **ProductCreationServiceTest** | - Attempting to create a duplicate product (`DuplicateProductException`)<br>- Creating a product with null `stockQuantity` defaults it to 0 |
| **ProductUpdateServiceTest** | - Updating product with blank or null description (`IllegalArgumentException`)<br>- Decreasing stock beyond available quantity (`InvalidStockValueException`) |
| **ProductQueryServiceTest** | - Fetching all products when none exist (`ProductNotFoundException`)<br>- Fetching products by name with no match (`ProductNotFoundException`)<br>- Fetching product by name and variant when not found (`ProductNotFoundException`)<br>- Fetching low-stock products when none exist (`ProductNotFoundException`) |
| **ProductDeleteServiceTest** | - Attempting to delete a non-existent product (`ProductNotFoundException`) |

**Notes:**
- Tests mainly cover **edge/failure scenarios** to ensure proper exception handling.  
- All tests use **Mockito** to mock repository interactions.  
- Business logic is unit-tested in isolation.
### Project Structure 
```
inventory-management-system-api/
├── src/main/java/com/asechallenge/inventorymanagement/
│ ├── controller/ # Contains REST API endpoints
│ │ └── ProductController.java
│ ├── service/ # Contains all business logic
│ │ ├── ProductCreationService.java
│ │ ├── ProductUpdateService.java
│ │ ├── ProductDeleteService.java
│ │ ├── ProductQueryService.java
│ │ └── ProductServiceFacade.java
│ ├── repository/ # Data access layer (handles database operations)
│ │ └── ProductRepository.java
│ ├── entity/ # Data models representing database tables
│ │ └── Product.java
│ ├── dto/ # Data Transfer Objects for request & response
│ │ ├── ProductCreationRequestDTO.java
│ │ ├── ProductUpdateRequestDTO.java
│ │ ├── ProductResponseDTO.java
│ │ └── StockChangeRequestDTO.java
│ ├── util/ # Utility classes
│ │ └── SkuGenerator.java
│ ├── exception/ # Custom exceptions and global exception handler
│ │ ├── GlobalExceptionHandler.java
│ │ ├── ProductNotFoundException.java
│ │ ├── DuplicateProductException.java
│ │ └── InventoryException.java
│ └── InventorymanagementApplication.java # Main Spring Boot class
└── src/test/java/com/asechallenge/inventorymanagement/
├── service/ # Unit tests for service classes
│ ├── ProductCreationServiceTest.java
│ ├── ProductUpdateServiceTest.java
│ ├── ProductDeleteServiceTest.java
│ └── ProductQueryServiceTest.java
└── util/ # Unit tests for utility classes
  └── SkuGeneratorTest.java
```
## API Endpoints

> **Note:** All product-specific endpoints require users to provide the combination of name and variant to identify a product.
> System-generated IDs and SKUs are not used, because users cannot easily remember them.
> Since the combination of `name` and `variant` is **unique** for each product,
> this approach allows users to easily reference products without needing to remember internal IDs.

---

### Product CRUD

| Endpoint | Method | Description | Request DTO | Response DTO |
|----------|--------|-------------|------------|-------------|
| `/api/products` | GET | Fetch all products | N/A | `ProductResponseDTO` (List) |
| `/api/products?name={name}` | GET | Fetch products by name | N/A | `ProductResponseDTO` (List) |
| `/api/products?name={name}&variant={variant}` | GET | Fetch product by name & variant | N/A | `ProductResponseDTO` |
| `/api/products` | POST | Create a new product | `ProductCreationRequestDTO` | `ProductResponseDTO` |
| `/api/products?name={name}&variant={variant}` | PATCH | Update product | `ProductUpdateRequestDTO` | `ProductResponseDTO` |
| `/api/products?name={name}&variant={variant}` | DELETE | Delete a product | N/A | N/A |

---

### Stock Management

| Endpoint | Method | Description | Request DTO | Response DTO |
|----------|--------|-------------|------------|-------------|
| `/api/products/stock/increase?name={name}&variant={variant}` | PATCH | Increase product stock | `StockChangeRequestDTO` | `ProductResponseDTO` |
| `/api/products/stock/decrease?name={name}&variant={variant}` | PATCH | Decrease product stock | `StockChangeRequestDTO` | `ProductResponseDTO` |

---

### Additional Features

| Endpoint | Method | Description | Request DTO | Response DTO |
|----------|--------|-------------|------------|-------------|
| `/api/products?lowStock=true` | GET | Fetch all products with stock below `lowStockThreshold` | N/A | `ProductResponseDTO` (List) |

## Installation & Setup

Follow these steps to set up the Inventory Management API locally:

1. **Prerequisites**  
     Ensure you have installed:  
   - Java 17+  
   - Maven 3.6+  
   - Git  
   - (Optional) Postman for testing  

2. **Clone the Repository**  
   ```bash
   git clone https://github.com/KartikaNaveena/Inventory-Management-System-API.git
   cd Inventory-Management-System-API
3. **Build the Project**  
     Run the following command to download dependencies and compile the project:  
    ```bash
    mvn clean install
    ```

4. **Run the Application**  
     Start the Spring Boot server using:  
    ```bash
    mvn spring-boot:run
    ```
5. **Run Tests**  
     To execute all unit tests, use the following command:

    ```bash
    mvn test
    ```
6. **Access the application**

   API Base URL: http://localhost:8080/api/products

## Testing the API

> **Note:** The product names and variants used in the examples below (e.g., `IPhone`, `Wireless Mouse`) are for demonstration purposes only.  
> When testing the APIs locally, replace them with the `name` and `variant` of the products in your own database.

> **How to test:**  
> - Run the Spring Boot application locally (default: `http://localhost:8080`).  
> - Use an API client like **Postman** to send requests.  
> - For endpoints requiring query parameters (like `name` or `variant`), add them in the query/params section of your API client.  
> - For endpoints requiring a request body (like POST or PATCH), add the JSON data in the body section of your API client.  
> - Send the request and verify the response matches the examples below.

### 🟢 Get All Products
**Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/products`

**Expected Response:**
```json
[
  {
    "name": "Wireless Mouse",
    "variant": "Black",
    "description": "A wireless mouse for computers",
    "stockQuantity": 404,
    "lowStockThreshold": 300,
    "sku": "wirelessmouseBlack"
  },
  {
    "name": "Wireless Keyboard",
    "variant": "Black",
    "description": "A wireless keyboard for computers",
    "stockQuantity": 400,
    "lowStockThreshold": 300,
    "sku": "wirelesskeyboardBlack"
  }
]


```

### 🟠 Get Low Stock Products

**Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/products?lowStock=true`

**Expected Response:**
```json
[
  {
    "name": "Wireless Mouse",
    "variant": "",
    "description": "A wireless mouse for computers",
    "stockQuantity": 100,
    "lowStockThreshold": 300,
    "sku": "wirelessmouse"
  },
  {
    "name": "Wireless Mouse",
    "variant": "Blue",
    "description": "A wireless mouse for computers",
    "stockQuantity": 100,
    "lowStockThreshold": 300,
    "sku": "wirelessmouseBlue"
  }
]
```

### 🔵 Get Product by Name

**Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/products?name=Wireless Mouse`

**Expected Response:**
```json
[
  {
    "name": "Wireless Mouse",
    "variant": "Black",
    "description": "A wireless mouse for computers",
    "stockQuantity": 404,
    "lowStockThreshold": 300,
    "sku": "wirelessmouseBlack"
  },
  {
    "name": "Wireless Mouse",
    "variant": "Blue",
    "description": "A wireless mouse for computers",
    "stockQuantity": 100,
    "lowStockThreshold": 300,
    "sku": "wirelessmouseBlue"
  }
]
```

### 🟣 Get Product by Name & Variant

**Method:** `GET`  
**Endpoint:** `http://localhost:8080/api/products?name=Wireless Mouse&variant=Blue`

**Expected Response:**
```json
{
  "name": "Wireless Mouse",
  "variant": "Blue",
  "description": "A wireless mouse for computers",
  "stockQuantity": 100,
  "lowStockThreshold": 300,
  "sku": "wirelessmouseBlue"
}
```

### 🟢 Create Product

**Method:** `POST`  
**Endpoint:** `http://localhost:8080/api/products`

**Request Body:**
```json
{
  "name": "IPhone",
  "variant": "13",
  "description": "An IOS mobile",
  "stockQuantity": 100,
  "lowStockThreshold": 300
}
```
**Expected Response:**
```json
{
  "name": "IPhone",
  "variant": "13",
  "description": "An IOS mobile",
  "stockQuantity": 100,
  "lowStockThreshold": 300,
  "sku": "iphone13"
}

```
### 🟠 Update Product 
> **Note:** Currently, the Product Update API only allows updating the description field.

> Fields like name, variant, sku, and lowStockThreshold are not relevant for updating.

> Stock quantity cannot be updated via this endpoint — it is managed separately through the Stock Management APIs (/stock/increase and /stock/decrease).

**Method:** `PATCH`  
**Endpoint:** `http://localhost:8080/api/products?name=IPhone&variant=13`

**Request Body:**

```json
{
  "description": "An IOS mobile of version 13"
}
```
**Expected Response:**
```json
{
    "name": "IPhone",
    "variant": "13",
    "description": "An IOS mobile of version 13",
    "stockQuantity": 100,
    "lowStockThreshold": 300,
    "sku": "iphone13"
}
```
### 🟢 Increase Product Stock

**Method:** `PATCH`  
**Endpoint:** `http://localhost:8080/api/products/stock/increase?name=IPhone&variant=13`

**Request Body:**
```json
{
  "quantity": 10
}
```
**Expected Response:**
```json
{
  "name": "IPhone",
  "variant": "13",
  "description": "An IOS mobile of version 13",
  "stockQuantity": 110,
  "lowStockThreshold": 300,
  "sku": "iphone13"
}

```

### 🔴 Decrease Product Stock

**Method:** `PATCH`  
**Endpoint:** `http://localhost:8080/api/products/stock/decrease?name=IPhone&variant=13`

**Request Body:**
```json
{
  "quantity": 10
}
```
**Expected Response:**
```json
{
  "name": "IPhone",
  "variant": "13",
  "description": "An IOS mobile of version 13",
  "stockQuantity": 100,
  "lowStockThreshold": 300,
  "sku": "iphone13"
}


```

### 🗑️ Delete Product

**Method:** `DELETE`  
**Endpoint:** `http://localhost:8080/api/products?name=IPhone&variant=13`

**Request Body:** None

**Expected Response:** None (204 No Content)

**Notes:**
- This API deletes the product identified by `name` and `variant`.  
- No request body is required.  
- The response returns **no content** (HTTP status 204) if the deletion is successful.  
- Once deleted, the product cannot be retrieved unless re-created via the Create Product API.

## 👨‍💻 Developer

**Saladi Kartika Naveena** – B.Tech (CSE, 2025), two SDE internships at Amazon.

### 📧 Contact
- Email: [naveenasaladi27@gmail.com](mailto:naveenasaladi27@gmail.com)  
- LinkedIn: [linkedin.com/in/saladi-kartika-naveena](https://www.linkedin.com/in/saladi-kartika-naveena/)




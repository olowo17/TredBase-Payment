# 📘 Payment Processing System

This is a Spring Boot-based payment processing application designed to handle unique and shared payments for students by their respective parents. 
The app demonstrates secure design, multi-table entity relationships, and balance arithmetic logic using Spring Data JPA and H2 database.

---

## 🚀 Features

- Parent-Student account structure
- Role-based payment types: `UNIQUE` and `SHARED`
- Balance management with validation
- Service fee logic based on payment amount
- In-memory database with H2 for development/testing

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database (file-based)
- ModelMapper
- Lombok
- SLF4J Logging

---

## 🧩 Entities & Relationships

- `Parent` (has balance, can pay)
- `Student` (can receive funds, belongs to one or more parents)
- `Payment` (record of transaction)
- `PaymentType`: `UNIQUE` (one parent) or `SHARED` (two parents)
- `PaymentStatus`: e.g., `SUCCESS`

---

## 🔐 Security Design & Access Control for Payment Endpoint 

- All database access is performed via **Spring Data JPA repositories**.
- Validation checks ensure:
  - Only associated parents can pay for a student
  - Each `SHARED` payment requires **exactly two parents**
  - Each parent in a `SHARED` payment must have at least **half** the required amount
  - Parents must have **sufficient balance** for any transaction
  - The `@Transactional` boundary ensures **data consistency** across related tables.

# Role-Based Access Control (RBAC) with JWT
The application uses Spring Security with JWT (JSON Web Token) for stateless authentication and authorization.

Upon login, a JWT access token is issued to the authenticated user.

This token must be included in the Authorization header of protected endpoints using the format:
  - Only users with the Admin role can access the payment processing endpoint.
  - Parent or Student users will receive a 401 Unauthorized response if they attempt to access the payment endpoint.


🛡️ Permission-Based Endpoint Access
Access to sensitive endpoints (like /api/payments) is strictly controlled:

✅ Only users with the ADMIN role can access and perform payment transactions.

❌ Users with roles PARENT and STUDENT are denied access.

This access control is enforced via Spring Security's method-level or route-based authorization.
-----

## Postman Collection Link
https://documenter.getpostman.com/view/33523574/2sB2ca5eF6

-----

## 🧮 Arithmetic Logic

### Processing Fee Logic:
- Default service fee: **4%** of the payment amount
- Discounted fee: **3.5%** for amounts **greater than 200,000**

### Calculation Steps:
```java
BigDecimal serviceFee = originalAmount > 200000 ? 
  originalAmount * 0.035 : 
  originalAmount * 0.04;

BigDecimal adjustedAmount = originalAmount + serviceFee;

----
### 💳 Payment Modes

# 1. UNIQUE Payment
Only one parent pays the entire adjusted amount.

Conditions checked:

Parent is associated with the student.

Parent’s balance ≥ adjusted amount.

Actions:

Deduct from parent's balance.

Credit student with the original amount (excluding fee).

# 2. SHARED Payment
Payment is split between exactly two parents.

Conditions checked:

Student must have exactly two associated parents.

Each parent must have at least 50% of the adjusted amount.

Their combined balance must be ≥ adjusted amount.

Actions:

Deduct 50% of the adjusted amount from each parent.

Credit student with the original amount.

----
### 🧪 How to Build, Run & Test
git clone https://github.com/olowo17/tredBasePayment.git
cd payment

---
## Build and Run the project
./mvnw clean install
./mvnw spring-boot:run

App will start on: http://localhost:8080

----


### 🧪 H2 Database Console
http://localhost:8080/h2-console

**Use**:

JDBC URL: jdbc:h2:file:./data/payment-db
Username: sa
Password: password

## sample request and response

{
  "parentId": 1,
  "studentId": 1,
  "paymentAmount": 20000
}
Expected:

4.0% service fee applied

Adjusted amount deducted from parent(s)

Original amount credited to student

Response
{
    "code": 0,
    "description": "Operation Successful",
    "data": {
        "transactionReference": "64406edb-a8fc-4620-ba30-3607d715d9bd",
        "originalAmount": 20000,
        "serviceFee": 800,
        "adjustedAmount": 20800,
        "paymentStatus": "SUCCESS",
        "paymentType": "SHARED",
        "timestamp": "2025-04-12T16:49:42.9697571"
    }
}
 ### Link to postman colllection


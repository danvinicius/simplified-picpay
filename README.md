## About
API built using Java and Java Spring for the [Simplified PicPay Backend Challenge](https://github.com/PicPay/picpay-desafio-backend/blob/main/readme.md).<br/>
The aim of this project is to practice Java and Spring Framework. 

<div style="display: inline_block">
    <img align="center" alt="Java" title="Java" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg">
    <img align="center" alt="Spring" title="Spring" height="30" width="40" src="https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg">
</div>
<br/>

## Simplified PicPay challenge
<h4> 
    🚧 Under development 🚧
</h4>
<br/>

## Installation

1. Clone the repository:

```bash
git clone https://github.com/danvinicius/simplified-picpay-challenge.git
```

2. Install dependencies with Maven

3. Update `application.properties` puting your H2 credentials and endpoints offered by PicPay for mocking transactions authorization and notification:

```yaml
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

picpay.authorizerService.url=https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc
picpay.notificationService.url=https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6
```

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080

## Endpoints

### 1. Creates an user

#### Endpoint: POST /api/user

Request:
```http
POST /api/user
Content-Type: application/json

{
    "email": "someone@mail.com",
    "firstName": "first name",
    "lastName": "last name",
    "document": "123456789-0",
    "password": "1234",
    "type": "COMMON/MERCHANT",
    "balance": 50000
}
```

Response:
```http
200 OK
Content-Type: application/json

{
    "id": 1,
    "email": "someone@mail.com",
    "firstName": "first name",
    "lastName": "last name",
    "document": "123456789-0",
    "password": "1234",
    "type": "COMMON/MERCHANT",
    "balance": 500000
}
```

#### Error:
An error may occur user's e-mail or document are already registered.

Response:
```http
400 BAD REQUEST
Content-Type: application/json

{
    "message": "User already exists",
    "statusCode": 400
}
```

### 2. Lists all users

#### Endpoint: GET /api/user

Request:
```http
GET /api/user
Content-Type: application/json
```

Response:
```http
200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "email": "someone@mail.com",
    "firstName": "first name",
    "lastName": "last name",
    "document": "123456789-0",
    "password": "1234",
    "type": "COMMON/MERCHANT",
    "balance": 500000
  },
  ...
]
```

### 3. Creates an transaction

#### Endpoint: POST /api/transaction

Request:
```http
POST /api/transaction
Content-Type: application/json

{
    "receiverId": 1,
    "senderId": 2,
    "amount": 10000
}
```

Response:
```http
200 OK
Content-Type: application/json

{
    "id": 1,
    "receiverId": 1,
    "senderId": 2,
    "amount": 10000,
    "sender": {
      "id": 1,
      "email": "someone@mail.com",
      "firstName": "first name",
      "lastName": "last name",
      "document": "123456789-0",
      "password": "1234",
      "type": "COMMON",
      "balance": 490000
    },
    "receiver": {
      "id": 1,
      "email": "someone@mail.com",
      "firstName": "first name",
      "lastName": "last name",
      "document": "123456789-0",
      "password": "1234",
      "type": "COMMON/MERCHANT",
      "balance": 510000
    }
}
```

#### Errors:
An error may occur if sender or receiver users are not found

Response:
```http
404 NOT FOUND
Content-Type: application/json

{
    "message": "User not found",
    "statusCode": 404
}
```

An error may occur if sender is an merchant user

Response:
```http
401 UNAUTHORIZED
Content-Type: application/json

{
    "message": "Merchant user cannot make transactions",
    "statusCode": 401
}
```

An error may occur if sender has isufficient balance

Response:
```http
401 UNAUTHORIZED
Content-Type: application/json

{
    "message": "Insufficient balance",
    "statusCode": 401
}
```

An error may occur if external PicPay authorization service returns false

Response:
```http
401 UNAUTHORIZED
Content-Type: application/json

{
    "message": "Unauthorized transaction",
    "statusCode": 401
}
```

<br/>
Install <a href="https://git-scm.com/">GIT</a> and <a href="https://www.java.com/pt-BR/">Java</a> on your computer.

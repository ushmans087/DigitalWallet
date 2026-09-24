## Digital Wallet – Microservices

A distributed digital wallet application built using **Spring Boot Microservices**, providing secure authentication, wallet management, bank transfers, transaction tracking, caching, and asynchronous communication.

### Tech Stack

* **Java**
* **Spring Boot**
* **Spring Cloud Gateway**
* **Netflix Eureka**
* **Spring Security + JWT**
* **MySQL**
* **Apache Kafka**
* **Redis**
* **Docker**
* **REST APIs**

---

### Architecture

The application follows a **Microservices Architecture**.

### Services

* **API Gateway** – Single entry point for client requests
* **Authentication Service** – User registration, login, authentication and authorization
* **Wallet Service** – Wallet and bank account management
* **Payment Service** – Payment and transfer processing
* **Eureka Discovery Server** – Service discovery
* **MySQL** – Persistent data storage
* **Redis** – Caching
* **Kafka** – Asynchronous communication between services

### Service Ports

| Service                 |   Port |
| ----------------------- | -----: |
| API Gateway             | `8080` |
| Eureka Discovery Server | `8082` |
| Authentication Server   | `8084` |
| Wallet Server           | `8086` |
| Payment Server          | `8088` |
| MySQL                   | `3306` |
| Redis                   | `6379` |
| Kafka                   | `9092` |

---

### Project Structure

```text
DigitalWallet/
│
├── api-gateway/
│   └── Dockerfile
│
├── discovery/
│   └── Dockerfile
│
├── test/
│   ├── Dockerfile
│   └── src/
│
├── wallet-server/
│   └── Dockerfile
│
├── payment-server/
│   └── Dockerfile
│
├── docker-compose.yml
├── init.sql
└── README.md
```

---

### Features

* User registration and login
* JWT-based authentication
* Role-based authorization
* Digital wallet management
* Bank account management
* Wallet-to-wallet transfers
* Bank transfers
* Payment processing
* Transaction history
* Date-based transaction filtering
* Redis caching
* Kafka-based asynchronous communication
* Service discovery using Eureka
* API Gateway
* MySQL database persistence
* Dockerized deployment

---

## Running Locally

### Prerequisites

Install the following:

* Java 17+
* Maven
* MySQL
* Redis
* Apache Kafka
* IntelliJ IDEA or another Java IDE

---

### 1. Clone the Repository

```bash
git clone https://github.com/ushmans087/DigitalWallet.git
cd DigitalWallet
```

---

### 2. Configure MySQL

Create the required databases:

```sql
CREATE DATABASE auth_db;
CREATE DATABASE wallet_db;
CREATE DATABASE payment_db;
```

Create a MySQL user and grant the required privileges.

Example:

```sql
CREATE USER 'your_username'@'%' IDENTIFIED BY 'your_password';

GRANT ALL PRIVILEGES ON auth_db.* TO 'your_username'@'%';
GRANT ALL PRIVILEGES ON wallet_db.* TO 'your_username'@'%';
GRANT ALL PRIVILEGES ON payment_db.* TO 'your_username'@'%';

FLUSH PRIVILEGES;
```

Update the MySQL credentials in the respective `application.properties` files.
change user_name and password and database for mysql in

1. payment-server\application.properties (use payment_db)
2. wallet-server\application.properties (use wallet_db)
3. auth-server\application.properties (use auth_db)

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wallet_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 3. Start Redis

Start Redis on:

```text
localhost:6379
```

run redis in wsl ubuntu terminal, if windows.

Verify that Redis is running:

```bash
redis-cli ping
```

Expected output:

```text
PONG
```

---

### 4. Start Kafka

Start Apache Kafka in KRaft mode and configure it first.

Kafka should be available at:

```text
localhost:9092
```

Make sure Kafka is running before starting the services that depend on it.

---

### Redis Caching

### Cached Endpoints

| Endpoint                   | Cache                | Purpose                                              |
| -------------------------- | -------------------- | ---------------------------------------------------- |
| `GET /wallet/findby/email` | `walletByEmail`      | Retrieves wallet details using email                 |
| `GET /wallet/accounts`     | Wallet account cache | Retrieves the bank accounts associated with a wallet |

The cache is invalidated when relevant wallet data is modified, such as wallet balance or account updates.

Redis configuration:

```text
Local:  localhost:6379
Docker: redis:6379
```

---

### Kafka

### Kafka Topics

| Topic               | Producer       | Consumer      | Purpose                                             |
| ------------------- | -------------- | ------------- | --------------------------------------------------- |
| `user-registration` | Auth Server    | Wallet Server | Publishes newly registered user information         |
| `payment-topic`     | Payment Server | Wallet Server | Sends payment/transfer events for wallet processing |

### Kafka Configuration

Local:

```text
localhost:9092
```

Docker:

```text
kafka:9092
```

Kafka consumer groups:

```text
wallet-server
payment-server
```


### 5. Start Eureka Discovery Server

Run the Discovery Server first.

```text
Port: 8082
```

The other microservices register themselves with Eureka.

---

### 6. Start the Authentication Server

Run the Authentication Server.

```text
Port: 8084
```

---

### 7. Start the Wallet Server

Run the Wallet Server.

```text
Port: 8086
```

---

### 8. Start the Payment Server

Run the Payment Server.

```text
Port: 8088
```

---

### 9. Start the API Gateway

Run the API Gateway.

```text
Port: 8080
```

The API Gateway acts as the **single entry point** for the application.

Client requests should be sent through:

```text
http://localhost:8080
```

---

## Running with Docker

The project can also be run completely using Docker.

With Docker, MySQL, Redis, Kafka and all Spring Boot microservices run as containers.

### Prerequisites

Install:

* Docker Desktop
* Git

You do **not** need to separately install MySQL, Redis or Kafka when using the Docker setup.

---

### 1. Clone the Repository

```bash
git clone https://github.com/ushmans087/DigitalWallet.git
cd DigitalWallet
```

---

### 2. Configure MySQL Credentials

Before starting Docker, open `docker-compose.yml` and update the following environment variables with your own MySQL credentials:

```yaml
environment:
  MYSQL_ROOT_PASSWORD: your_root_password
  MYSQL_USER: your_database_user
  MYSQL_PASSWORD: your_database_password
```

Use the same `MYSQL_USER` and `MYSQL_PASSWORD` values in the Spring Boot services that connect to MySQL.

> **Note:** Do not use real production credentials in a public GitHub repository.
update the configuration in all 3 application.properties of payment-server/wallet-server/auth-server(test).


### 3. Start the Application

Build and start all containers:

```bash
docker compose up -d --build
```

Check the running containers:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

To view logs for a specific service:

```bash
docker compose logs -f wallet-server
```

---

### Docker Networking

Inside Docker, services communicate using their **Docker Compose service names** rather than `localhost`.

For example:

```text
MySQL     → mysql:3306
Redis     → redis:6379
Kafka     → kafka:9092
Eureka    → discovery-server:8082
```
---

### Docker Services

| Service        | Docker Hostname    |   Port |
| -------------- | ------------------ | -----: |
| API Gateway    | `api-gateway`      | `8080` |
| Eureka         | `discovery-server` | `8082` |
| Authentication | `test`             | `8084` |
| Wallet         | `wallet-server`    | `8086` |
| Payment        | `payment-server`   | `8088` |
| MySQL          | `mysql`            | `3306` |
| Redis          | `redis`            | `6379` |
| Kafka          | `kafka`            | `9092` |

---

### MySQL Initialization

The Docker setup uses:

```text
init.sql
```

to initialize the MySQL environment.

The MySQL data is stored in a Docker volume:

```yaml
volumes:
  - mysql-data:/var/lib/mysql
```

This means that restarting the containers does not normally remove the database data.

### Stop containers

```bash
docker compose down
```

### Local vs Docker

| Component      | Local Setup      | Docker Setup            |
| -------------- | ---------------- | ----------------------- |
| MySQL          | `localhost:3306` | `mysql:3306`            |
| Redis          | `localhost:6379` | `redis:6379`            |
| Kafka          | `localhost:9092` | `kafka:9092`            |
| Eureka         | `localhost:8082` | `discovery-server:8082` |
| Auth Server    | `localhost:8084` | `test:8084`             |
| Wallet Server  | `localhost:8086` | `wallet-server:8086`    |
| Payment Server | `localhost:8088` | `payment-server:8088`   |
| API Gateway    | `localhost:8080` | `localhost:8080`        |

---

## Stopping the Application

### Local Setup

Stop the running Spring Boot applications, Kafka and Redis manually.

### Docker Setup

```bash
docker compose down
```

To remove containers and database volumes:

```bash
docker compose down -v
```

---

### Future Improvements

* Refresh token implementation
* Improved transaction consistency
* Distributed tracing
* Centralized configuration
* Monitoring and metrics
* CI/CD pipeline
* Cloud deployment
* Improved secret management

---

### Author
**Usman S**

B.E. Computer Science and Engineering

---

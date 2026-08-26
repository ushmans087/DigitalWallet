# Multi-Service Digital Wallet Platform

A modern, microservices-based Digital Wallet application supporting user authentication, wallet balance management, linked bank account simulation, peer-to-peer (P2P) transfers, and manual transaction validation (auditing).

---

## Architecture Overview

The system consists of five Spring Boot backend microservices and a lightweight Vanilla JavaScript frontend. Service registration is managed by a Eureka discovery server, and all external traffic enters via an API Gateway.

```
                  ┌──────────────────────────┐
                  │    Vanilla JS Frontend   │
                  └─────────────┬────────────┘
                                │ HTTP / REST (Port 8080)
                                ▼
                  ┌──────────────────────────┐
                  │   API Gateway (8080)     │
                  └──────┬──────┬──────┬─────┘
                         │      │      │
      ┌──────────────────┘      │      └──────────────────┐
      ▼ (lb://AUTH-SERVER)      ▼ (lb://WALLET-SERVER)    ▼ (lb://PAYMENT-SERVER)
┌───────────┐             ┌───────────┐             ┌───────────┐
│Auth Server│             │Wallet Serv│             │Payment Ser│
│  (8084)   │             │  (8086)   │             │  (8088)   │
└─────┬─────┘             └─────┬─────┘             └─────┬─────┘
      │                         │                         │
      ▼                         ▼                         ▼
 ┌─────────┐               ┌─────────┐               ┌─────────┐
 │ Auth DB │               │Wallet DB│               │Paymnt DB│
 └─────────┘               └─────────┘               └─────────┘
```

### Microservices Directory

*   **[`discovery`] Spring Cloud Eureka Discovery Server (Port `8082`)
*   **[`api-gateway`] API Gateway & OAuth2 Resource Server Router (Port `8080`)
*   **[`test`] Authentication Service (`auth-server` / Port `8084`)
*   **[`wallet-server`] Wallet & Bank Account Manager (Port `8086`)
*   **[`payment-server`] P2P Transaction Processing & History (Port `8088`)
*   **[`frontend`] Client UI (static HTML/CSS/JS files)

---

## 🛠️ Prerequisites

To run this project locally, make sure you have installed:
1. **Java Development Kit (JDK) 17** or higher
2. **Maven 3.8+** (or use the packaged `./mvnw` wrappers)
3. A basic static web server or IDE extension (such as Live Server for VS Code) to serve the frontend

---

## Getting Started (Run Guide)

To start the platform, you must launch the backend services in the correct sequence. Run the following Maven commands from the respective directories:

### Step 1: Start the Discovery Server
Go to the `discovery` folder and run:
```bash
mvn spring-boot:run
```
*Verify it is running by navigating to [http://localhost:8082](http://localhost:8082) in your browser.*

### Step 2: Start the API Gateway
Go to the `api-gateway` folder and run:
```bash
mvn spring-boot:run
```

### Step 3: Start the Auth Service
Go to the `test` folder and run:
```bash
mvn spring-boot:run
```
*(Registers with Eureka registry as `AUTH-SERVER`)*

### Step 4: Start the Wallet Service
Go to the `wallet-server` folder and run:
```bash
mvn spring-boot:run
```

### Step 5: Start the Payment Service
Go to the `payment-server` folder and run:
```bash
mvn spring-boot:run
```

### Step 6: Run the Frontend
1. Make sure your gateway port matches the configurations inside [`frontend/config.js`](file:///c:/Users/ushma/Documents/Digital_wallet/frontend/config.js):
   ```javascript
   const CONFIG = {
       API_BASE_URL: "http://localhost:8080"
   };
   ```
2. Serve the [`frontend`](file:///c:/Users/ushma/Documents/Digital_wallet/frontend) folder using any local HTTP web server. For example, using `npx`:
   ```bash
   npx serve ./frontend
   ```
3. Open the output address in your browser (e.g. `http://localhost:3000` or `http://localhost:5500`) to access the application.

---

##  Database Console Access

Each microservice runs its own isolated file-based H2 database. To inspect database records and structures:

1. Enable/verify configuration settings inside each service's `application.properties`:
   ```properties
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   ```
2. Open the browser and visit the respective H2 path:
   *   **Auth DB Console**: `http://localhost:8084/h2-console`
   *   **Wallet DB Console**: `http://localhost:8086/h2-console`
   *   **Payment DB Console**: `http://localhost:8088/h2-console`
3. Enter the following JDBC settings on the login window:
   *   **JDBC URL**: `jdbc:h2:file:./data/authdb`
   *   **Username**: `sa`
   *   **Password**: *(leave blank)*

---

## Security & Roles

Authentication utilizes JWT (JSON Web Token) authorization.
*   **Token Verification**: API Gateway validates the JWT on all paths excluding `/auth/register` and `/auth/login`. Inside individual microservices, an OAuth2 Resource Server decrypts the JWT using a built-in RSA public key.
*   **User Roles**:
    *   `USER`: Standard permissions (making transfers, checking balances, adding bank accounts).
    *   `TRANSACTION_VALIDATOR` / `VALIDATOR`: Restricted permissions. Grants access to the **Bank Transactions** audit dashboard (`/wallet/validator/**`), where pending deposit/withdrawal requests must be verified or rejected.

---

## 📝 Developer Documentation
For a deep dive into the APIs, data schemas, sequence flows, and known security anomalies (including the transaction history query bug), refer to the detailed specification report:

👉 **[Software Requirements Specification (SRS) Document](file:///c:/Users/ushma/Documents/Digital_wallet/SRS.md)**

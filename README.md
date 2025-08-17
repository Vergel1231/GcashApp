# GcashApp – Online Banking Authentication System

A modular Java application that simulates GCash-style online banking features, focusing on secure user authentication and database-backed account management.

---

## Features Implemented

### Authentication & User Management
**User Registration**: Adds new users with validated fields: ID, Name, Email, Number, PIN
**Login Authentication**: Verifies credentials securely and returns user ID for transactions
**Field Validation**: Ensures proper input format for all registration fields
**Secure Feedback**: Handles login errors without exposing sensitive data
**Change PIN**: Allows users to update their PIN securely
**Logout**: Ends session and clears user context

### Account & Balance
**Check Balance**: Authenticated users can view their current account balance via JDBC

###Transactions
**Cash In**: Adds funds to a user account and logs the transaction
**Cash Transfer**: Transfers funds between two users. Updates both balances and logs the transaction with sender and recepient ID
**View Transaction by ID** ← *New*
**View All Transactions** (internal testing)
**View User Transactions** (internal testing)

---

## Tech Stack

- Java 17
- MySQL (via JDBC)
- Maven (project structure and dependency management)
- Docker (for MySQL container setup)
- GitHub Codespaces (cloud-based dev environment)

---

## Database Schema

### Table: `users`
| Field   | Type     | Description                        |
|---------|----------|------------------------------------|
| id      | INT      | Primary key, auto-increment        |
| name    | VARCHAR  | Full name                          |
| email   | VARCHAR  | Unique email                       |
| number  | VARCHAR  | Mobile number                      |
| pin     | VARCHAR  | Encrypted PIN                      |

### Table: `balance`
| Field     | Type     | Description                        |
|-----------|----------|------------------------------------|
| id        | INT      | Primary key                        |
| user_id   | INT      | Foreign key to `users.id`          |
| amount    | DOUBLE   | Current balance                    |

### Table: `transaction`
| Field       | Type     | Description                        |
|-------------|----------|------------------------------------|
| id          | INT      | Primary key                        |
| account_id  | INT      | Foreign key to `users.id`          |
| name        | VARCHAR  | Transaction type (e.g., CashIn)    |
| amount      | DOUBLE   | Transaction amount                 |
| created_at  | DATETIME | Timestamp                          |

---

## Project Structure
GcashApp/
|── src/
|   └── main/
|    └── java/
|      └── com/
|       └── gcash/
|          ├── auth/
│            └── UserAuthentication.java
|          ├── balance/
│            └── CheckBalance.java
|          ├── db/
│            └── DBConnection.java
|          ├── main/
│            └── Main.java
|          └── transaction
|            ├── CashIn.java
|            ├── CashTransfer.java
|            └── Transactions.java
├── pom.xml
└── README.md

---

## How to Run

1. **Start MySQL Docker container**  
   Ensure your database is running and accessible.
2. Initialize schema Run SQL scripts to create users, balance, and transaction tables.
3. Compile and run the app

    mvn compile
    mvn exec:java -Dexec.mainClass="com.gcash.main.Main"

---

## Check Balance Feature

### Description
Allows authenticated users to view their current account balance, retrieved securely from the MySQL database using JDBC.

    Sample Output
    Login successful.
    Balance check successful.
    Virgilio's current balance: ₱1000.0
    User 1 logged out.

---

## Cash In

### Description
Allows users to deposit funds into their account. Updates both the balance and transaction tables.

### Implementation
- CashIn.java handles CLI input and dual insert logic.
- Verifies user ID and amount.
- Adds transaction record and updates balance.

    Sample Output
    Enter user ID: 1
    Enter amount to cash in: 500
    Cash-in successful. ₱500.0 added to account.

---

## Cash Transfer

### Description
Allows users to transfer funds from their account to another user. This operation:
- Inserts a transaction with both transfer_from_id and transfer_to_id
- Deducts from sender's balance
- Adds to recipient's balance

### Implementation
- CashTransfer.java handles CLI input and dual balance updates.
- Inserts transaction with transfer_from_id and transfer_to_id.
- Validates sufficient funds before proceeding.
- Uses JDBC PreparedStatement for inserts and updates

    Verification
    SELECT * FROM transaction WHERE transfer_from_id = [sender_id];
    SELECT amount FROM balance WHERE user_id = [sender_id];
    SELECT amount FROM balance WHERE user_id = [recipient_id];

    Sample Output
    Transfer successful. ₱250.0 sent from User 1 to User 3.

## View Transaction by ID

### Description
Retrieves and displays a single transaction using its unique ID.

### Implementation
- Transactions.java method: viewTransaction(int transactionId)
- Uses PreparedStatement to prevent SQL injection
- Displays transaction details or warning if not found

    Sample Output
    Viewing Transaction ID: 102
    --------------------------------------------------
    Account ID: 5 | Name: CashIn | Amount: 1000.00 | Date: 2025-08-17 23:59:59




GcashApp – Online Banking Authentication System
A modular Java application that simulates GCash-style online banking features, focusing on secure user authentication and database-backed account management.

Features Implemented
User Registration: Adds new users with validated fields: ID, Name, Email, Number, PIN
Login Authentication: Verifies credentials securely and returns user ID for transactions
Field Validation: Ensures proper input format for all registration fields
Secure Feedback: Handles login errors without exposing sensitive data
Change PIN: Allows users to update their PIN securely
Logout: Ends session and clears user context

Tech Stack
Java 17
MySQL (via JDBC)
Maven (project structure and dependency management)
Docker (for MySQL container setup)
GitHub Codespaces (cloud-based dev environment)

Database Schema
Table: users
Field    Type      	Description
id      	INT      	Primary key, auto-increment
name    	VARCHAR  	Full name
email    	VARCHAR  	Unique email
number  	VARCHAR  	Mobile number
pin    	  VARCHAR  	Encrypted PIN

Project Structure
GcashApp/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── gcash/
│                   ├── auth/
│                   │   └── UserAuthentication.java
│                   ├── db/
│                   │   └── DBConnection.java
│                   └── main/
│                       └── Main.java
├── pom.xml
└── README.md

How to Run
1. Start MySQL Docker container = Ensure your database is running and accessible.
2. Create users table = Run the SQL script to initialize the schema.
3. Compile and run the app

mvn compile
mvn exec:java -Dexec.mainClass="com.gcash.main.Main"

Next Steps
Add Check Balance

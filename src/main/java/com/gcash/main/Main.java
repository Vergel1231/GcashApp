package com.gcash.main;

import com.gcash.auth.UserAuthentication;
import com.gcash.balance.CheckBalance;
import com.gcash.transaction.CashIn;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        UserAuthentication auth = new UserAuthentication();

        // Login with correct PIN
        int userId = auth.login("virgilio@example.com", "4321");

        if (userId != -1) {
            // Check balance
            CheckBalance cb = new CheckBalance();
            double balance = cb.checkBalance(userId);

            if (balance >= 0) {
                System.out.println("Balance check successful.");
                System.out.println("Virgilio's current balance: ₱" + balance);

                // Cash-in logic
                try (Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/gcash", "root", "your_password")) {

                    CashIn cashIn = new CashIn(conn);

                    boolean firstTxn = cashIn.cashIn(userId, 200.00, "Initial cash-in");
                    boolean secondTxn = cashIn.cashIn(userId, 300.00, "Follow-up cash-in");

                    if (firstTxn && secondTxn) {
                        System.out.println("Both cash-in transactions successful.");
                    } else {
                        System.out.println("One or more cash-in transactions failed.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("No balance record found.");
            }

            cb.close();
            auth.logout(userId);
        } else {
            System.out.println("Login failed.");
        }

        auth.close();
    }
}

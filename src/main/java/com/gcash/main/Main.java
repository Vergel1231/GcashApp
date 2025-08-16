package com.gcash.main;

import com.gcash.auth.UserAuthentication;
import com.gcash.balance.CheckBalance;
import com.gcash.database.DBConnection;
import com.gcash.transaction.CashIn;
import com.gcash.transaction.CashTransfer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserAuthentication auth = new UserAuthentication();

        // Login with correct PIN
        int userId = auth.login("alice@example.com", "1234");

        if (userId != -1) {
            try (
                Connection conn = DBConnection.getConnection();
                Scanner scanner = new Scanner(System.in);
            ) {
                // Check balance
                CheckBalance cb = new CheckBalance(conn);
                double balance = cb.checkBalance(userId);

                if (balance >= 0) {
                    System.out.println("Balance check successful.");
                    System.out.println("Virgilio's current balance: ₱" + balance);

                    // Cash-in logic
                    CashIn cashIn = new CashIn(conn);
                    boolean firstTxn = cashIn.cashIn(userId, 200.00, "Initial cash-in");
                    boolean secondTxn = cashIn.cashIn(userId, 300.00, "Follow-up cash-in");

                    if (firstTxn && secondTxn) {
                        System.out.println("Both cash-in transactions successful.");
                    } else {
                        System.out.println("One or more cash-in transactions failed.");
                    }

                    // Cash Transfer logic (interactive)
                    CashTransfer transfer = new CashTransfer(conn);
                    transfer.startTransferPrompt(userId, scanner);

                } else {
                    System.out.println("No balance record found.");
                }

                cb.close();

            } catch (SQLException e) {
                System.out.println("Database error:");
                e.printStackTrace();
            }

            auth.logout(userId);
        } else {
            System.out.println("Login failed.");
        }

        auth.close();
    }
}

package com.gcash.main;

import com.gcash.auth.UserAuthentication;
import com.gcash.balance.CheckBalance;

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

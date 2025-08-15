package com.gcash.main;

import com.gcash.auth.UserAuthentication;

public class Main {
    public static void main(String[] args) {
        UserAuthentication auth = new UserAuthentication();

        // Registration
        auth.register("Virgilio", "virgilio@example.com", "09171234567", "1234");

        // Login
        int userId = auth.login("virgilio@example.com", "1234");

        // Change PIN
        if (userId != -1) {
            auth.changePin(userId, "4321");
            auth.logout(userId);
        }

        // Close connection
        auth.close();
    }
}

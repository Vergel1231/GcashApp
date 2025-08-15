package com.gcash.auth;

import com.gcash.database.DBConnection;
import java.sql.*;
import java.util.regex.*;

public class UserAuthentication {
    private Connection conn;

    public UserAuthentication() {
        conn = DBConnection.getConnection();
    }

    public boolean register(String name, String email, String number, String pin) {
        if (!validateName(name) || !validateEmail(email) || !validateNumber(number) || !validatePin(pin)) {
            System.out.println("Validation failed.");
            return false;
        }

        String sql = "INSERT INTO users (name, email, number, pin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, number);
            stmt.setString(4, pin);
            stmt.executeUpdate();
            System.out.println("Registration successful.");
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Email or number already registered.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int login(String email, String pin) {
        String sql = "SELECT id FROM users WHERE email = ? AND pin = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful.");
                return rs.getInt("id");
            } else {
                System.out.println("Invalid credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean changePin(int userId, String newPin) {
        if (!validatePin(newPin)) return false;
        String sql = "UPDATE users SET pin = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPin);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logout(int userId) {
        System.out.println("User " + userId + " logged out.");
    }

    // Validation methods
    private boolean validateName(String name) {
        return name != null && name.length() >= 2;
    }

    private boolean validateEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", email);
    }

    private boolean validateNumber(String number) {
        return Pattern.matches("^09\\d{9}$", number);
    }

    private boolean validatePin(String pin) {
        return Pattern.matches("^\\d{4}$", pin);
    }

    public void close() {
        DBConnection.closeConnection(conn);
    }
}

package com.gcash.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transactions {

    private Connection conn;

    public Transactions(Connection conn) {
        this.conn = conn;
    }

    public void viewAll() {
        String query = "SELECT * FROM transaction";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("All Transactions:");
            while (rs.next()) {
                int id = rs.getInt("id");
                int accountId = rs.getInt("account_id");
                String name = rs.getString("name");
                double amount = rs.getDouble("amount");
                String createdAt = rs.getString("date");

                System.out.printf("ID: %d | Account ID: %d | Name: %s | Amount: %.2f | Date: %s%n",
                                  id, accountId, name, amount, createdAt);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving transactions: " + e.getMessage());
        }
    }

    public void viewUserAll(int userId) {
        String query = "SELECT * FROM transaction WHERE account_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {

                System.out.printf("Transactions for User ID: %d%n", userId);
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double amount = rs.getDouble("amount");
                    String createdAt = rs.getString("date");

                    System.out.printf("ID: %d | Name: %s | Amount: %.2f | Date: %s%n",
                                      id, name, amount, createdAt);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user transactions: " + e.getMessage());
        }
    }

    public void viewTransaction(int transactionId) {
        String query = "SELECT * FROM transaction WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    int accountId = rs.getInt("account_id");
                    String name = rs.getString("name");
                    double amount = rs.getDouble("amount");
                    String createdAt = rs.getString("date");

                    System.out.printf("Transaction ID: %d%n", transactionId);
                    System.out.printf("Account ID: %d | Name: %s | Amount: %.2f | Date: %s%n",
                                      accountId, name, amount, createdAt);
                } else {
                    System.out.printf("No transaction found with ID: %d%n", transactionId);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transaction: " + e.getMessage());
        }
    }
}

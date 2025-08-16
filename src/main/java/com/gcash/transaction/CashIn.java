package com.gcash.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.ResultSet;

public class CashIn {
    private Connection conn;

    public CashIn(Connection conn) {
        this.conn = conn;
    }

    public boolean cashIn(int userId, double amount, String name) {
        try {
            // Step 1: Update balance
            String updateBalanceSQL = "UPDATE balance SET amount = amount + ? WHERE user_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSQL);
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, userId);
            int rowsAffected = updateStmt.executeUpdate();

            // Step 2: Log transaction
            String insertTxnSQL = "INSERT INTO transaction (amount, name, account_id, date, transfer_to_id, transfer_from_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement txnStmt = conn.prepareStatement(insertTxnSQL);
            txnStmt.setDouble(1, amount);
            txnStmt.setString(2, name);
            txnStmt.setInt(3, userId);
            txnStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            txnStmt.setNull(5, Types.INTEGER); // No transferTo for cash-in
            txnStmt.setNull(6, Types.INTEGER); // No transferFrom for cash-in
            txnStmt.executeUpdate();

            // Step 3: Query new balance
            String queryBalanceSQL = "SELECT amount FROM balance WHERE user_id = ?";
            PreparedStatement queryStmt = conn.prepareStatement(queryBalanceSQL);
            queryStmt.setInt(1, userId);
            ResultSet rs = queryStmt.executeQuery();

            if (rs.next()) {
                double newBalance = rs.getDouble("amount");
                System.out.printf("[MILESTONE] ₱%.2f cash-in successful for user %d (%s). New balance: ₱%.2f%n",
                                  amount, userId, name, newBalance);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
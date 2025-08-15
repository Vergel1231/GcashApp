package com.gcash.transaction;

import com.gcash.transaction.CashIn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CashIn {
    private Connection conn;

    public CashIn(Connection conn) {
        this.conn = conn;
    }

    public boolean cashIn(int accountId, double amount, String name) {
        try {
            // Step 1: Update balance
            String updateBalanceSQL = "UPDATE balance SET amount = amount + ? WHERE account_ID = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSQL);
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, accountId);
            int rowsAffected = updateStmt.executeUpdate();

            // Step 2: Log transaction
            String insertTxnSQL = "INSERT INTO transaction (amount, name, account_ID, date, transferToID, transferFromID) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement txnStmt = conn.prepareStatement(insertTxnSQL);
            txnStmt.setDouble(1, amount);
            txnStmt.setString(2, name);
            txnStmt.setInt(3, accountId);
            txnStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            txnStmt.setNull(5, java.sql.Types.INTEGER); // No transferTo for cash-in
            txnStmt.setNull(6, java.sql.Types.INTEGER); // No transferFrom for cash-in

            txnStmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

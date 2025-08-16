package com.gcash.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CashTransfer {
    private final Connection conn;

    public CashTransfer(Connection conn) {
        this.conn = conn;
    }

    public void startTransferPrompt(int loggedInUserId, Scanner scanner) {
        while (true) {
            System.out.print("--- Cash Transfer ---\nEnter recipient account ID (or type 'exit' to cancel): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Transfer cancelled.");
                break;
            }

            try {
                int recipientId = Integer.parseInt(input);

                System.out.print("Enter amount to transfer: ");
                double amount = Double.parseDouble(scanner.nextLine());

                boolean success = cashTransfer(loggedInUserId, recipientId, amount);
                if (success) break;

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter numeric values or 'exit'.");
            }
        }
    }

    public boolean cashTransfer(int loggedInUserId, int recipientId, double amount) {
        try {
            // Restriction 1: Prevent self-transfer
            if (loggedInUserId == recipientId) {
                System.out.println("You cannot transfer to your own account.");
                return false;
            }

            // Restriction 2: Amount must be positive
            if (amount <= 0) {
                System.out.println("Transfer amount must be greater than zero.");
                return false;
            }

            // Restriction 3: Check if recipient exists
            PreparedStatement checkRecipient = conn.prepareStatement(
                "SELECT COUNT(*) FROM user WHERE id = ?");
            checkRecipient.setInt(1, recipientId);
            ResultSet rs = checkRecipient.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                System.out.println("Recipient account does not exist.");
                return false;
            }

            conn.setAutoCommit(false);

            // Restriction 4: Check sender balance and debit
            PreparedStatement debit = conn.prepareStatement(
                "UPDATE user SET balance = balance - ? WHERE id = ? AND balance >= ?");
            debit.setDouble(1, amount);
            debit.setInt(2, loggedInUserId);
            debit.setDouble(3, amount);
            int debitRows = debit.executeUpdate();

            if (debitRows == 0) {
                conn.rollback();
                System.out.println("Insufficient funds.");
                return false;
            }

            // Credit recipient
            PreparedStatement credit = conn.prepareStatement(
                "UPDATE user SET balance = balance + ? WHERE id = ?");
            credit.setDouble(1, amount);
            credit.setInt(2, recipientId);
            credit.executeUpdate();

            // Log transaction
            PreparedStatement log = conn.prepareStatement(
                "INSERT INTO transaction (account_ID, type, amount, date, transferFromID, transferToID) " +
                "VALUES (?, 'transfer', ?, NOW(), ?, ?)");
            log.setInt(1, loggedInUserId);
            log.setDouble(2, amount);
            log.setInt(3, loggedInUserId);
            log.setInt(4, recipientId);
            log.executeUpdate();

            conn.commit();
            System.out.println("Transfer successful.");
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        }
    }
}

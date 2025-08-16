package com.gcash.balance;

import com.gcash.database.DBConnection;
import java.sql.*;

public class CheckBalance {
    private Connection conn;

    // Constructor injection: use external Connection
    public CheckBalance(Connection externalConn) {
        this.conn = externalConn;
    }

    // Default constructor: use internal DBConnection
    public CheckBalance() {
        this.conn = DBConnection.getConnection();
    }

    public double checkBalance(int userId) {
        String sql = "SELECT amount FROM balance WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("amount");
            } else {
                System.out.println("No balance record found for user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void close() {
        DBConnection.closeConnection(conn);
    }
}

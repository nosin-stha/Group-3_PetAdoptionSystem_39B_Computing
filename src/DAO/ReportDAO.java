package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportDAO {

    MySqlConnector mysql = new MySqlConnector();

    public boolean insertReport(int adopterID, int providerID, String reason) {
        Connection conn = null;
        try {
            conn = mysql.openConnection();
            conn.setAutoCommit(false);

            String insertSql = "INSERT INTO AccountReport (adopterID, providerID, reportReason) "
                             + "VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, adopterID);
                ps.setInt(2, providerID);
                ps.setString(3, reason);
                ps.executeUpdate();
            }

            String updateSql = "UPDATE Providers SET proStatus = 'Reported' WHERE providerID = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, providerID);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println(e);
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { System.out.println(ex); }
            }
            return false;

        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException e) { System.out.println(e); }
            }
        }
    }
}
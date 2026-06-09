/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Dell
 */


import java.sql.*;
import java.util.ArrayList;

public class AdminAccountReportHandleDAO {

    private final Connection connection;

    public AdminAccountReportHandleDAO(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Object[]> getReportedProviders() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT p.providerID,
                   p.shelterName,
                   p.proEmail,
                   p.proPhoneNo,
                   p.proPfp,
                   COUNT(ar.reportID) AS totalReports
            FROM Providers p
            JOIN AccountReport ar ON ar.providerID = p.providerID
            GROUP BY p.providerID, p.shelterName, p.proEmail, p.proPhoneNo, p.proPfp
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("providerID"),
                        rs.getString("shelterName"),
                        rs.getString("proEmail"),
                        rs.getString("proPhoneNo"),
                        rs.getString("proPfp"),
                        rs.getInt("totalReports")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Object[]> getReportsByProvider(int providerID) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT ar.reportID,
                   a.adpUsername,
                   a.adpEmail,
                   ar.reportReason,
                   ar.reportStatus
            FROM AccountReport ar
            JOIN Adopters a ON ar.adopterID = a.adopterID
            WHERE ar.providerID = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, providerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("reportID"),
                        rs.getString("adpUsername"),
                        rs.getString("adpEmail"),
                        rs.getString("reportReason"),
                        rs.getString("reportStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean resolveReport(int reportID, int providerID) {
        String updateReport   = "UPDATE AccountReport SET reportStatus='Resolved' WHERE reportID=?";
        String updateProvider = "UPDATE Providers SET proStatus='Active' WHERE providerID=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(updateReport)) {
                ps.setInt(1, reportID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(updateProvider)) {
                ps.setInt(1, providerID);
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    public boolean dismissReport(int reportID, int providerID) {
        // THIS row becomes 'Disabled', provider account becomes 'Disabled'
        String updateReport   = "UPDATE AccountReport SET reportStatus='Disabled' WHERE reportID=?";
        String updateProvider = "UPDATE Providers SET proStatus='Disabled' WHERE providerID=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(updateReport)) {
                ps.setInt(1, reportID);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(updateProvider)) {
                ps.setInt(1, providerID);
                ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    // Called after dismissReport to auto-resolve all remaining pending reports for this provider
    public boolean resolveAllOtherReports(int providerID, int exceptReportID) {
        String sql = "UPDATE AccountReport SET reportStatus='Resolved' " +
                     "WHERE providerID=? AND reportID != ? AND reportStatus='Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, providerID);
            ps.setInt(2, exceptReportID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
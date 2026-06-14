package DAO;

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
                   COUNT(ar.reportID) AS totalReports,
                   p.proAddress,
                   p.proMissionStatement,
                   p.proAdoptionPolicy
            FROM Providers p
            JOIN AccountReport ar ON ar.providerID = p.providerID
            GROUP BY p.providerID,
                     p.shelterName,
                     p.proEmail,
                     p.proPhoneNo,
                     p.proPfp,
                     p.proAddress,
                     p.proMissionStatement,
                     p.proAdoptionPolicy
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("providerID"),           // [0]
                    rs.getString("shelterName"),       // [1]
                    rs.getString("proEmail"),          // [2]
                    rs.getString("proPhoneNo"),        // [3]
                    rs.getString("proPfp"),            // [4]
                    rs.getInt("totalReports"),         // [5]
                    rs.getString("proAddress"),        // [6]
                    rs.getString("proMissionStatement"), // [7]
                    rs.getString("proAdoptionPolicy")  // [8]
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
                ps.setInt(1, reportID); ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(updateProvider)) {
                ps.setInt(1, providerID); ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean dismissReport(int reportID, int providerID) {
        String updateReport   = "UPDATE AccountReport SET reportStatus='Disabled' WHERE reportID=?";
        String updateProvider = "UPDATE Providers SET proStatus='Disabled' WHERE providerID=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(updateReport)) {
                ps.setInt(1, reportID); ps.executeUpdate();
            }
            try (PreparedStatement ps = connection.prepareStatement(updateProvider)) {
                ps.setInt(1, providerID); ps.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean resolveAllOtherReports(int providerID, int exceptReportID) {
        String sql = """
            UPDATE AccountReport
            SET reportStatus = 'Resolved'
            WHERE providerID = ?
              AND reportID != ?
              AND reportStatus = 'Pending'
        """;
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
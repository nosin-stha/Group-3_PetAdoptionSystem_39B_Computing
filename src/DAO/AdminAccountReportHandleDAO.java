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

    private Connection connection;

    public AdminAccountReportHandleDAO(Connection connection) {
        this.connection = connection;
    }

    // Fetch all reports with adopter info
    public ArrayList<Object[]> getAllReportsWithAdopterInfo() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT a.adpUsername, a.adpEmail, ar.reportReason, ar.reportStatus, ar.reportID " +
                     "FROM AccountReport ar " +
                     "JOIN Adopters a ON ar.adopterID = a.adopterID";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("adpUsername"),
                    rs.getString("adpEmail"),
                    rs.getString("reportReason"),
                    rs.getString("reportStatus"),
                    rs.getInt("reportID")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    // Resolve a report
    public boolean resolveReport(int reportID) {
        String sql = "UPDATE AccountReport SET reportStatus = 'Resolved' WHERE reportID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reportID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

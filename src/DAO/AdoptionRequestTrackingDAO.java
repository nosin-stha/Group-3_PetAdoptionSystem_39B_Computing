package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.AdoptionRequestData;

public class AdoptionRequestTrackingDAO {

    MySqlConnector mysql = new MySqlConnector();

    // Display Adoption Request Cards Info
    public ArrayList<AdoptionRequestData> getRequestsByAdopter(int adopterID) {
        ArrayList<AdoptionRequestData> list = new ArrayList<>();
        String sql =
            "SELECT ar.*, " +
            "p.petName, p.petType, p.petGender, p.petAge, p.imagePath " +
            "FROM AdoptionRequests ar " +
            "JOIN Pets p ON ar.petID = p.petID " +
            "WHERE ar.adopterID = ? " +
            "ORDER BY ar.adoptionID DESC";
        Connection conn = mysql.openConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AdoptionRequestData req = new AdoptionRequestData(
                    rs.getInt("adoptionID"),
                    rs.getInt("adopterID"),
                    rs.getInt("petID"),
                    rs.getString("reqFullName"),
                    rs.getString("reqEmail"),
                    rs.getString("reqPhoneNo"),
                    rs.getString("reqAddress"),
                    rs.getString("reqReason"),
                    rs.getString("adoptionStatus"),
                    rs.getString("petName"),
                    rs.getString("petType"),
                    rs.getString("petGender"),
                    rs.getString("petAge"),
                    rs.getString("imagePath")
                );
                list.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // Delete Request
    public boolean deleteRequest(int adoptionID) {
        String sql = "DELETE FROM AdoptionRequests WHERE adoptionID = ?";
        Connection conn = mysql.openConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adoptionID);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Get Total Requests Count
    public int getTotalRequestsByAdopter(int adopterID) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM AdoptionRequests WHERE adopterID = ?";
        Connection conn = mysql.openConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }
        return count;
    }
}
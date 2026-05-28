package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.AdoptionRequestData;

public class AdoptionRequestDAO {

    MySqlConnector mysql = new MySqlConnector();

    
    
    //Display Adoption Request Cards Info
    public List<AdoptionRequestData> getRequestsByAdopter(int adopterID) {
        List<AdoptionRequestData> list = new ArrayList<>();
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

                AdoptionRequestData req =
                    new AdoptionRequestData(
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
        }
        mysql.closeConnection(conn);
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
        try {
            database.MySqlConnector mysql = new database.MySqlConnector();
            java.sql.Connection conn = mysql.openConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            java.sql.ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                count = rs.getInt(1);
            }
            conn.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
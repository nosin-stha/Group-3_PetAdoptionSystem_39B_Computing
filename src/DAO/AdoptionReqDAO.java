package DAO;
import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdoptionReqDAO {
    
    public boolean hasAlreadyApplied(int adopterID, int petID) {
        MySqlConnector connector = new MySqlConnector();
        Connection conn = connector.openConnection();
        String query = "SELECT COUNT(*) FROM adoptionrequests WHERE AdopterID = " + adopterID + " AND PetID = " + petID;
        try {
            ResultSet rs = connector.runQuery(conn, query);
            if (rs != null && rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            connector.closeConnection(conn);
        }
        return false;
    }
    
    public boolean insertRequest(
            int adopterID,
            int petID,
            String fullName,
            String email,
            String phone,
            String address,
            String reason) {
        
        if (reason != null && reason.length() > 100) {
            reason = reason.substring(0, 100);
        }
        
        System.out.println("Inside insertRequest method");
        
        try {
            Connection conn = new MySqlConnector().openConnection();
            String sql = "INSERT INTO AdoptionRequests "
                       + "(adopterID, petID, reqFullName, reqEmail, "
                       + "reqPhoneNo, reqAddress, reqReason) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            ps.setInt(2, petID);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setString(7, reason);
            
            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);
            return rows > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
package DAO;
import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdoptionReqDAO {
    
    public boolean insertRequest(
            int adopterID,
            int petID,
            String fullName,
            String email,
            String phone,
            String address,
            String reason) {
        
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
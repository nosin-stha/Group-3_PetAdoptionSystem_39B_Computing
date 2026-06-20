package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdopterProfileDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ResultSet getAdopterProfile(int adopterID) {
        try {
            Connection conn = mysql.openConnection();
            String sql = "SELECT adpUsername, adpEmail, adpPfp FROM Adopters WHERE adopterID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateAdopterProfile(int adopterID, String username, String email, String pfpPath) {
        try {
            Connection conn = mysql.openConnection();
            String sql = "UPDATE Adopters SET adpUsername=?, adpEmail=?, adpPfp=? WHERE adopterID = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, username);
            pstm.setString(2, email);
            pstm.setString(3, pfpPath);
            pstm.setInt(4, adopterID);
            int rows = pstm.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
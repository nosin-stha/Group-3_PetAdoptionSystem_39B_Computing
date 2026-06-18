package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.SessionData;

public class LoginDAO {

    MySqlConnector mysql = new MySqlConnector();

    public String loginUser(String username, String password, String role) {
        Connection conn = mysql.openConnection();
        try {
            if (role.equalsIgnoreCase("Adopter")) {

                String sql = "SELECT * FROM Adopters WHERE adpUsername = ? AND adpPassword = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String status = rs.getString("adpStatus");
                    if (status.equalsIgnoreCase("Disabled")) return "disabled";

                    SessionData.userID    = rs.getInt("adopterID");
                    SessionData.username  = rs.getString("adpUsername");
                    SessionData.email     = rs.getString("adpEmail");
                    SessionData.role      = "Adopter";
                    SessionData.imagePath = rs.getString("adpPfp");
                    return "success";
                }

            } else if (role.equalsIgnoreCase("Provider")) {

                // Allow Active and Reported — block only Disabled
                String sql = "SELECT * FROM Providers WHERE proUsername = ? AND proPassword = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String status = rs.getString("proStatus");
                    if (status.equalsIgnoreCase("Disabled")) return "disabled";

                    SessionData.userID    = rs.getInt("providerID");
                    SessionData.username  = rs.getString("proUsername");
                    SessionData.email     = rs.getString("proEmail");
                    SessionData.role      = "Provider";
                    SessionData.imagePath = rs.getString("proPfp");
                    SessionData.shelterName = rs.getString("shelterName");
                    return "success";
                }

            } else if (role.equalsIgnoreCase("Admin")) {

                if (username.equals("admin") && password.equals("1234")) {
                    SessionData.userID   = 0;
                    SessionData.username = "admin";
                    SessionData.role     = "Admin";
                    return "success";
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return "failed";
    }
}
package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.SessionData;

public class LoginDAO {

    MySqlConnector mysql = new MySqlConnector();

    public boolean loginUser(String username, String password, String role) {

        Connection conn = mysql.openConnection();

        try {

            String sql = "";

            // Login as Adopter
            if (role.equalsIgnoreCase("Adopter")) {

                sql = "SELECT * FROM Adopters "
                        + "WHERE adpUsername = ? "
                        + "AND adpPassword = ? "
                        + "AND adpStatus='Active'";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    SessionData.userID = rs.getInt("adopterID");
                    SessionData.username = rs.getString("adpUsername");
                    SessionData.email = rs.getString("adpEmail");
                    SessionData.role = "Adopter";
                    SessionData.imagePath = rs.getString("adpPfp");

                    return true;
                }
            }

            // Login as Provider 
            else if (role.equalsIgnoreCase("Provider")) {

                sql = "SELECT * FROM Providers "
                        + "WHERE proUsername = ? "
                        + "AND proPassword = ? "
                        + "AND proStatus='Active'";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    SessionData.userID = rs.getInt("providerID");
                    SessionData.username = rs.getString("proUsername");
                    SessionData.email = rs.getString("proEmail");
                    SessionData.role = "Provider";
                    SessionData.imagePath = rs.getString("proPfp");

                    return true;
                }
            }

            // ================= ADMIN =================
            else if (role.equalsIgnoreCase("Admin")) {

                if (username.equals("admin")
                        && password.equals("1234")) {

                    SessionData.userID = 0;
                    SessionData.username = "admin";
                    SessionData.role = "Admin";

                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }
}
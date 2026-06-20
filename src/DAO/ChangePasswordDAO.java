/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Dell
 */

public class ChangePasswordDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    public static final String ADOPTER = "adopter";
    public static final String PROVIDER = "provider";

    // Check old password against what's stored in DB
    public boolean verifyOldPassword(String userType, int userID, String oldPassword) {
        String sql = isAdopter(userType)
            ? "SELECT adpPassword FROM Adopters WHERE adopterID = ?"
            : "SELECT proPassword FROM Providers WHERE providerID = ?";

        Connection conn = mysql.openConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String stored = isAdopter(userType)
                    ? rs.getString("adpPassword")
                    : rs.getString("proPassword");
                return stored != null && stored.equals(oldPassword);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Permanently update the password
    public boolean updatePassword(String userType, int userID, String newPassword) {
        String sql = isAdopter(userType)
            ? "UPDATE Adopters SET adpPassword = ? WHERE adopterID = ?"
            : "UPDATE Providers SET proPassword = ? WHERE providerID = ?";

        Connection conn = mysql.openConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setInt(2, userID);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    private boolean isAdopter(String userType) {
        return ADOPTER.equalsIgnoreCase(userType);
    }
}

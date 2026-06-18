/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Dell
 */

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForgotPasswordDAO {

    MySqlConnector mysql = new MySqlConnector();

    public boolean isEmailExist(String email) {
        String sql =
            "SELECT adpEmail FROM Adopters WHERE adpEmail = ? " +
            "UNION " +
            "SELECT proEmail FROM Providers WHERE proEmail = ?";

        Connection conn = mysql.openConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("ForgotPasswordDAO - Email Check Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {
        Connection conn = mysql.openConnection();

        // Try Adopters first
        String sqlAdopter = "UPDATE Adopters SET adpPassword = ? WHERE adpEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlAdopter)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            if (rows > 0) return true;
        } catch (Exception e) {
            System.out.println("ForgotPasswordDAO - Update Adopter Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        // Try Providers
        conn = mysql.openConnection();
        String sqlProvider = "UPDATE Providers SET proPassword = ? WHERE proEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlProvider)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.out.println("ForgotPasswordDAO - Update Provider Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return false;
    }
}

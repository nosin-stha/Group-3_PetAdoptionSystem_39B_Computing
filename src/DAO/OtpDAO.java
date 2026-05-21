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

public class OtpDAO {
    MySqlConnector mysql = new MySqlConnector();


    // SAVE OTP
    public boolean saveOtp(String email, String otp) {
        Connection conn = null;

        // ensures OTP is refreshed + timestamp updated every time
        String query = "REPLACE INTO otp(email, otp_code, created_time) VALUES(?, ?, CURRENT_TIMESTAMP)";

        try {
            conn = mysql.openConnection();

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, otp);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.out.println("Save OTP Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    
    
    // GET OTP

    public String getOtp(String email) {

        Connection conn = null;
        String query = "SELECT otp_code FROM otp WHERE email = ?";

        try {
            conn = mysql.openConnection();

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("otp_code");
            }
        } catch (Exception e) {
            System.out.println("Get OTP Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }


    // VERIFY OTP

    public String verifyOtp(String email, String otp) {
        Connection conn = null;
        
        String query = "SELECT otp_code, created_time FROM otp WHERE email = ?";

        try {
            conn = mysql.openConnection();

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String savedOtp = rs.getString("otp_code");
                java.sql.Timestamp createdAt = rs.getTimestamp("created_time");

                // check expiry (5 min)
                long diffMinutes =
                    (System.currentTimeMillis() - createdAt.getTime()) / (1000 * 60);

                if (diffMinutes > 5) {
                    return "EXPIRED";
                }

                if (!savedOtp.equals(otp)) {
                    return "INVALID";
                }

                return "VALID";
            }

        } catch (Exception e) {
            System.out.println("OTP Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return "INVALID";
    }


    
    // DELETE OTP

    public boolean deleteOtp(String email) {

        Connection conn = null;
        String query = "DELETE FROM otp WHERE email = ?";

        try {
            conn = mysql.openConnection();

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);

            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (Exception e) {
            System.out.println("Delete OTP Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
}
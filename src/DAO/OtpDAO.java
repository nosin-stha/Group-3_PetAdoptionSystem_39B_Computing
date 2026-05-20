/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Dell
 */
public class OtpDAO {
    MySqlConnector mysql = new MySqlConnector();

    public void saveOtp(String email, String otp) {

        Connection conn = mysql.openConnection();

        String query =
                "REPLACE INTO otp(email, otp_code) "
                + "VALUES('"+email+"', '"+otp+"')";

        mysql.executeUpdate(conn, query);

        mysql.closeConnection(conn);
    }

    public String getOtp(String email) {

        Connection conn = mysql.openConnection();

        String query =
                "SELECT otp_code FROM otp "
                + "WHERE email='"+email+"'";

        ResultSet rs = mysql.runQuery(conn, query);

        try {

            if(rs.next()) {
                return rs.getString("otp_code");
            }

        } catch(Exception e) {
            System.out.println(e);
        }

        mysql.closeConnection(conn);

        return null;
    }

    public void deleteOtp(String email) {

        Connection conn = mysql.openConnection();

        String query =
                "DELETE FROM otp "
                + "WHERE email='"+email+"'";

        mysql.executeUpdate(conn, query);

        mysql.closeConnection(conn);
    }  
}

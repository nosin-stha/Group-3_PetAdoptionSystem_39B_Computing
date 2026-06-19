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
 * @author shank
 */


public class AdopterProfileDAO {
    
    MySqlConnector mysql = new MySqlConnector();
    
    
    public ResultSet getAdopterProfile(int adopterID) {
        try {
            Connection conn = mysql.openConnection();
            String sql = "SELECT adpUsername, adpEmail FROM Adopters WHERE adopterID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean updateAdopterProfile(int adopterID, String username, String email) {
        try {
            Connection conn = mysql.openConnection();
            String sql = "UPDATE Adopters SET adpUsername=?, adpEmail=? WHERE adopterID = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, username);
            pstm.setString(2, email);
            pstm.setInt(3, adopterID);
            int rows = pstm.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
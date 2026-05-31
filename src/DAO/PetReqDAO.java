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

public class PetReqDAO {
    
    MySqlConnector mysql = new MySqlConnector();
    
    public ResultSet getPetById(int petID) {
        
        ResultSet rs = null;
        Connection conn = mysql.openConnection();
        
        try {
            String sql = "SELECT petName, petType, petGender, petAge, imagePath "
           + "FROM Pets WHERE petID = ?";
            
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, petID);
            rs = pstm.executeQuery();
            rs.getString("imagePath");
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return rs;
    }
}

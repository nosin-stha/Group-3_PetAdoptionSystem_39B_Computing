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

import model.AdopterData;
import model.ProviderData;



public class UsersDAO {

    MySqlConnector mysql = new MySqlConnector();

  
    public boolean isEmailExist(String email) {

        boolean exists = false;

        String sql =
            "SELECT adpEmail FROM Adopters WHERE adpEmail = ? " +
            "UNION " +
            "SELECT proEmail FROM Providers WHERE proEmail = ?";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, email);
            pstm.setString(2, email);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                exists = true;
            }

        } catch (Exception e) {
            System.out.println("Email Check Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return exists;
    }

   
    public boolean isUsernameExist(String username) {

        boolean exists = false;

        String sql =
            "SELECT adpUsername FROM Adopters WHERE adpUsername = ? " +
            "UNION " +
            "SELECT proUsername FROM Providers WHERE proUsername = ?";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, username);
            pstm.setString(2, username);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                exists = true;
            }

        } catch (Exception e) {
            System.out.println("Username Check Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return exists;
    }
  
   
    public boolean insertAdopter(AdopterData adopter) {

        String sql =
            "INSERT INTO Adopters(adpUsername, adpPassword, adpEmail, adpPfp) VALUES(?,?,?,?)";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, adopter.getUsername());
            pstm.setString(2, adopter.getPassword());
            pstm.setString(3, adopter.getEmail());
            pstm.setString(4, adopter.getPfp());

            int result = pstm.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            System.out.println("Insert Adopter Error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    
    public boolean insertProvider(ProviderData provider) {

        String sql =
            "INSERT INTO Providers(" +
            "shelterName, licenseID, proUsername, proPassword, proEmail, " +
            "proPhoneNo, proAddress, startWorkHour, endWorkHour, " +
            "startWorkDay, endWorkDay, proMissionStatement, proAdoptionPolicy, proPfp" +
            ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, provider.getShelterName());
            pstm.setString(2, provider.getLicenseID());
            pstm.setString(3, provider.getUsername());
            pstm.setString(4, provider.getPassword());
            pstm.setString(5, provider.getEmail());
            pstm.setString(6, provider.getPhoneNumber());
            pstm.setString(7, provider.getAddress());
            pstm.setString(8, provider.getStartWorkHour());
            pstm.setString(9, provider.getEndWorkHour());
            pstm.setString(10, provider.getStartWorkDay());
            pstm.setString(11, provider.getEndWorkDay());
            pstm.setString(12, provider.getMissionStatement());
            pstm.setString(13, provider.getAdoptionPolicy());
            pstm.setString(14, provider.getPfp());

            int result = pstm.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            System.out.println("Insert Provider Error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
}
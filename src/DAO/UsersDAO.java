/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import model.AdopterData;
import model.ProviderData;

/**
 *
 * @author Dell
 */
public class UsersDAO {
    MySqlConnector mysql = new MySqlConnector();

    public void insertAdopter(AdopterData adopter) {

        String sql =
                "INSERT INTO Adopters("
                + "adpUsername, "
                + "adpPassword, "
                + "adpEmail"
                + ") VALUES(?,?,?)";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm =
                     conn.prepareStatement(sql)) {

            pstm.setString(1,adopter.getUsername());
            pstm.setString(2,adopter.getPassword());
            pstm.setString(3,adopter.getEmail());

            int result = pstm.executeUpdate();
            System.out.println("Rows Inserted: " + result);
            System.out.println("Adopter Registered Successfully");

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // =========================
    // INSERT PROVIDER
    // =========================

    public void insertProvider(
            ProviderData provider
    ) {

        String sql =
                "INSERT INTO Providers("
                + "shelterName, "
                + "licenseID, "
                + "proUsername, "
                + "proPassword, "
                + "proEmail, "
                + "proPhoneNo, "
                + "proAddress, "
                + "startWorkHour, "
                + "endWorkHour, "
                + "startWorkDay, "
                + "endWorkDay, "
                + "proMissionStatement, "
                + "proAdoptionPolicy"
                + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = mysql.openConnection();

        try (PreparedStatement pstm =
                     conn.prepareStatement(sql)) {

            pstm.setString(1,provider.getShelterName());
            pstm.setString(2,provider.getLicenseID());
            pstm.setString(3,provider.getUsername());
            pstm.setString(4,provider.getPassword());
            pstm.setString(5,provider.getEmail());
            pstm.setString(6,provider.getPhoneNumber());
            pstm.setString(7,provider.getAddress());
            pstm.setString(8,provider.getStartWorkHour());
            pstm.setString(9,provider.getEndWorkHour());
            pstm.setString(10,provider.getStartWorkDay());
            pstm.setString(11,provider.getEndWorkDay());
            pstm.setString(12,provider.getMissionStatement());
            pstm.setString(13,provider.getAdoptionPolicy());

            int result = pstm.executeUpdate();
            System.out.println("Rows Inserted: " + result);
            System.out.println("Provider Registered Successfully");

        } catch(Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }
}

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
import model.PetsData;

import java.sql.*;
import java.util.ArrayList;

public class PetFavDAO {

    private final MySqlConnector connector = new MySqlConnector();

    public boolean addFavourite(int adopterID, int petID) {
        Connection conn = connector.openConnection();
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT IGNORE INTO PetFavourites (adopterID, petID) VALUES (?, ?)"
            );
            ps.setInt(1, adopterID);
            ps.setInt(2, petID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("addFavourite error: " + e.getMessage());
            return false;  // triggers error dialog in controller
        } finally {
            connector.closeConnection(conn);
        }
    }

    // Remove pet from favourites
    public boolean removeFavourite(int adopterID, int petID) {
        Connection conn = connector.openConnection();
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM PetFavourites WHERE adopterID = ? AND petID = ?"
            );
            ps.setInt(1, adopterID);
            ps.setInt(2, petID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("removeFavourite error: " + e.getMessage());
            return false;
        } finally {
            connector.closeConnection(conn);
        }
    }

    // Check if pet is already favourited
    public boolean isFavourite(int adopterID, int petID) {
        Connection conn = connector.openConnection();
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT favID FROM PetFavourites WHERE adopterID = ? AND petID = ?"
            );
            ps.setInt(1, adopterID);
            ps.setInt(2, petID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("isFavourite error: " + e.getMessage());
            return false;
        } finally {
            connector.closeConnection(conn);
        }
    }

    // Get all favourite pets for an adopter
    public ArrayList<PetsData> getFavouritePets(int adopterID) {
        ArrayList<PetsData> list = new ArrayList<>();
        Connection conn = connector.openConnection();
        if (conn == null) return list;
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT p.* FROM Pets p " +
                "JOIN PetFavourites f ON p.petID = f.petID " +
                "WHERE f.adopterID = ?"
            );
            ps.setInt(1, adopterID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PetsData pet = new PetsData();
                pet.setPetID(rs.getInt("petID"));
                pet.setProviderID(rs.getInt("providerID"));
                pet.setPetName(rs.getString("petName"));
                pet.setPetType(rs.getString("petType"));
                pet.setPetGender(rs.getString("petGender"));
                pet.setPetAge(rs.getString("petAge"));
                pet.setImagePath(rs.getString("imagePath"));
                pet.setHouseTrained(rs.getString("houseTrained"));
                pet.setSpayed(rs.getString("spayed"));
                pet.setVaccinated(rs.getString("vaccinated"));
                pet.setSpecialNeeds(rs.getString("specialNeeds"));
                pet.setPetAdoptionStatus(rs.getString("petAdoptionStatus"));
                list.add(pet);
            }
        } catch (SQLException e) {
            System.out.println("getFavouritePets error: " + e.getMessage());
        } finally {
            connector.closeConnection(conn);
        }
        return list;
    }
}

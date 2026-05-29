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
import model.PetsData;

public class PetDAO {

    MySqlConnector mysql = new MySqlConnector();

    // ADD PET
    public boolean addPet(PetsData pet) {

        String sql = "INSERT INTO Pets(providerID, petName, petType, petGender, petAge, houseTrained, spayed, vaccinated, specialNeeds, imagePath) "
                   + "VALUES(?,?,?,?,?,?,?,?,?,?)";

        Connection conn = mysql.openConnection();

        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, pet.getProviderID());
            pst.setString(2, pet.getPetName());
            pst.setString(3, pet.getPetType());
            pst.setString(4, pet.getPetGender());
            pst.setString(5, pet.getPetAge());
            pst.setString(6, pet.getHouseTrained());
            pst.setString(7, pet.getSpayed());
            pst.setString(8, pet.getVaccinated());
            pst.setString(9, pet.getSpecialNeeds());
            pst.setString(10, pet.getImagePath());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Add Pet Error: " + e.getMessage());
            return false;

        } finally {

            mysql.closeConnection(conn);
        }
    }

    
    
    
    // UPDATE PET
    public boolean updatePet(PetsData pet) {

        String sql = "UPDATE Pets SET petName=?, petType=?, petGender=?, petAge=?, "
                   + "houseTrained=?, spayed=?, vaccinated=?, specialNeeds=?, imagePath=? "
                   + "WHERE petID=?";

        Connection conn = mysql.openConnection();

        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, pet.getPetName());
            pst.setString(2, pet.getPetType());
            pst.setString(3, pet.getPetGender());
            pst.setString(4, pet.getPetAge());
            pst.setString(5, pet.getHouseTrained());
            pst.setString(6, pet.getSpayed());
            pst.setString(7, pet.getVaccinated());
            pst.setString(8, pet.getSpecialNeeds());
            pst.setString(9, pet.getImagePath());
            pst.setInt(10, pet.getPetID());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Update Pet Error: " + e.getMessage());
            return false;

        } finally {

            mysql.closeConnection(conn);
        }
    }
    
    
    

    // DELETE PET
    public boolean deletePet(int petID) {

        String sql = "DELETE FROM Pets WHERE petID=?";

        Connection conn = mysql.openConnection();

        if (conn == null) return false;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, petID);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Delete Pet Error: " + e.getMessage());
            return false;

        } finally {

            mysql.closeConnection(conn);
        }
    }
    
    
    
    
    // GET PET BY ID  ← new method
    public PetsData getPetById(int petID) {
        String sql = "SELECT * FROM Pets WHERE petID = ?";
        Connection conn = mysql.openConnection();
        if (conn == null) return null;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, petID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new PetsData(
                    rs.getInt("petID"),
                    rs.getInt("providerID"),
                    rs.getString("petName"),
                    rs.getString("petType"),
                    rs.getString("petGender"),
                    rs.getString("petAge"),
                    rs.getString("houseTrained"),
                    rs.getString("spayed"),
                    rs.getString("vaccinated"),
                    rs.getString("specialNeeds"),
                    rs.getString("petAdoptionStatus"),
                    rs.getString("imagePath")
                );
            }
        } catch (Exception e) {
            System.out.println("Get Pet By ID Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
}
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
import model.AdoptionRequestData;
import model.PetsData;
import java.sql.*;
import java.util.*;

public class AdoptionRequestsBoardDAO {

    private final MySqlConnector connector = new MySqlConnector();

    public Map<PetsData, List<AdoptionRequestData>> getRequestsGroupedByPet(int providerID) {
        Map<PetsData, List<AdoptionRequestData>> map = new LinkedHashMap<>();
        Connection con = null;

        String sql = "SELECT "
                + "p.petID, p.providerID, p.petName, p.petType, p.petGender, "
                + "p.petAge, p.houseTrained, p.spayed, p.vaccinated, "
                + "p.specialNeeds, p.petAdoptionStatus, p.imagePath, "
                + "ar.adoptionID, ar.adopterID, ar.reqFullName, ar.reqEmail, "
                + "ar.reqPhoneNo, ar.reqAddress, ar.reqReason, ar.adoptionStatus "
                + "FROM Pets p "
                + "LEFT JOIN AdoptionRequests ar ON p.petID = ar.petID "
                + "WHERE p.providerID = " + providerID + " "
                + "ORDER BY p.petID, ar.adoptionID DESC";

        try {
            con = connector.openConnection();
            ResultSet rs = connector.runQuery(con, sql);
            Map<Integer, PetsData> petCache = new LinkedHashMap<>();

            while (rs != null && rs.next()) {
                int petID = rs.getInt("petID");

                
                PetsData pet = petCache.computeIfAbsent(petID, id -> {
                    try {
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
                    } catch (SQLException e) {
                        System.out.println("PetsData mapping error: " + e);
                        return null;
                    }
                });

                if (pet == null) continue;
                map.putIfAbsent(pet, new ArrayList<>());

                
                if (rs.getObject("adoptionID") != null) {
                    map.get(pet).add(new AdoptionRequestData(
                        rs.getInt("adoptionID"),
                        rs.getInt("adopterID"),
                        petID,
                        rs.getString("reqFullName"),
                        rs.getString("reqEmail"),
                        rs.getString("reqPhoneNo"),
                        rs.getString("reqAddress"),
                        rs.getString("reqReason"),
                        rs.getString("adoptionStatus"),
                        rs.getString("petName"),
                        rs.getString("petType"),
                        rs.getString("petGender"),
                        rs.getString("petAge"),
                        rs.getString("imagePath")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("getRequestsGroupedByPet error: " + e);
        } finally {
            connector.closeConnection(con);
        }

        return map;
    }

    public boolean acceptRequest(int adoptionID, int petID) {
    Connection con = null;
    try {
        con = connector.openConnection();
        con.setAutoCommit(false);

        String updateRequest = "UPDATE AdoptionRequests SET adoptionStatus = 'Accepted' "
                             + "WHERE adoptionID = " + adoptionID;
        String updatePet     = "UPDATE Pets SET petAdoptionStatus = 'Adopted' "
                             + "WHERE petID = " + petID;
     
        String declineOthers = "UPDATE AdoptionRequests SET adoptionStatus = 'Declined' "
                             + "WHERE petID = " + petID + " "
                             + "AND adoptionID != " + adoptionID;

        int r1 = connector.executeUpdate(con, updateRequest);
        int r2 = connector.executeUpdate(con, updatePet);
        connector.executeUpdate(con, declineOthers); 

        if (r1 > 0 && r2 > 0) {
            con.commit();
            return true;
        } else {
            con.rollback();
            return false;
        }

    } catch (SQLException e) {
        System.out.println("acceptRequest error: " + e);
        try { if (con != null) con.rollback(); } catch (SQLException ex) { System.out.println(ex); }
        return false;
    } finally {
        connector.closeConnection(con);
    }
}

    public boolean declineRequest(int adoptionID) {
        Connection con = null;
        try {
            con = connector.openConnection();
            String sql = "UPDATE AdoptionRequests SET adoptionStatus = 'Declined' "
                       + "WHERE adoptionID = " + adoptionID;
            int result = connector.executeUpdate(con, sql);
            return result > 0;

        } catch (Exception e) {
            System.out.println("declineRequest error: " + e);
            return false;
        } finally {
            connector.closeConnection(con);
        }
    }
}

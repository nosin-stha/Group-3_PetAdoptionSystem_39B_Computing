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
import java.util.ArrayList;

public class AdoptionRequestsBoardDAO {

    private final MySqlConnector connector = new MySqlConnector();

    public ArrayList<PetsData> getPetsByProvider(int providerID) {
        ArrayList<PetsData> pets = new ArrayList<>();
        Connection con = null;

        String sql = "SELECT petID, providerID, petName, petType, petGender, "
                   + "petAge, houseTrained, spayed, vaccinated, "
                   + "specialNeeds, petAdoptionStatus, imagePath "
                   + "FROM Pets "
                   + "WHERE providerID = " + providerID
                   + " ORDER BY petID";

        try {
            con = connector.openConnection();
            ResultSet rs = connector.runQuery(con, sql);

            while (rs != null && rs.next()) {
                pets.add(new PetsData(
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
                ));
            }

        } catch (SQLException e) {
            System.out.println("getPetsByProvider error: " + e);
        } finally {
            connector.closeConnection(con);
        }

        return pets;
    }

    public ArrayList<AdoptionRequestData> getRequestsByPet(int petID) {
        ArrayList<AdoptionRequestData> requests = new ArrayList<>();
        Connection con = null;

        String sql = "SELECT ar.adoptionID, ar.adopterID, ar.petID, "
                   + "ar.reqFullName, ar.reqEmail, ar.reqPhoneNo, "
                   + "ar.reqAddress, ar.reqReason, ar.adoptionStatus, "
                   + "p.petName, p.petType, p.petGender, p.petAge, p.imagePath "
                   + "FROM AdoptionRequests ar "
                   + "JOIN Pets p ON ar.petID = p.petID "
                   + "WHERE ar.petID = " + petID
                   + " ORDER BY ar.adoptionID DESC";

        try {
            con = connector.openConnection();
            ResultSet rs = connector.runQuery(con, sql);

            while (rs != null && rs.next()) {
                requests.add(new AdoptionRequestData(
                    rs.getInt("adoptionID"),
                    rs.getInt("adopterID"),
                    rs.getInt("petID"),
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

        } catch (SQLException e) {
            System.out.println("getRequestsByPet error: " + e);
        } finally {
            connector.closeConnection(con);
        }

        return requests;
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
                                 + "WHERE petID = " + petID
                                 + " AND adoptionID != " + adoptionID;

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

    public AdoptionRequestData getRequestByID(int adoptionID) {
        Connection con = null;
        AdoptionRequestData data = null;

        String sql = "SELECT ar.adoptionID, ar.adopterID, ar.petID, "
                   + "ar.reqFullName, ar.reqEmail, ar.reqPhoneNo, "
                   + "ar.reqAddress, ar.reqReason, ar.adoptionStatus, "
                   + "p.petName, p.petType, p.petGender, p.petAge, p.imagePath "
                   + "FROM AdoptionRequests ar "
                   + "JOIN Pets p ON ar.petID = p.petID "
                   + "WHERE ar.adoptionID = " + adoptionID;

        try {
            con = connector.openConnection();
            ResultSet rs = connector.runQuery(con, sql);

            // Read all data out of ResultSet while connection is still open
            if (rs != null && rs.next()) {
                data = new AdoptionRequestData(
                    rs.getInt("adoptionID"),
                    rs.getInt("adopterID"),
                    rs.getInt("petID"),
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
                );
                System.out.println("getRequestByID found: adoptionID=" + adoptionID
                    + " email=" + data.getReqEmail()
                    + " petName=" + data.getPetName());
            } else {
                System.out.println("getRequestByID: NO ROW found for adoptionID=" + adoptionID);
            }

        } catch (SQLException e) {
            System.out.println("getRequestByID error: " + e.getMessage());
        } finally {
            
            connector.closeConnection(con);
        }

        return data;
    }
}
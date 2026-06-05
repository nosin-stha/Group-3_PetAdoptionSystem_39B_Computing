package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.PetsData;

public class ProviderHomePageDao {

    public ArrayList<PetsData> getPetsByProvider(int providerID) {
        ArrayList<PetsData> petList = new ArrayList<>();
        MySqlConnector db = new MySqlConnector();
        Connection conn = db.openConnection();
        String query = "SELECT * FROM Pets WHERE providerID = ? AND petAdoptionStatus = 'Available'";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, providerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PetsData pet = new PetsData(
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
                petList.add(pet);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println("Provider DAO Error: " + e);
        } finally {
            db.closeConnection(conn);
        }

        return petList;
    }
}
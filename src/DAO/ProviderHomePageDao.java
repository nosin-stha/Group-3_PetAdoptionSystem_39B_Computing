package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.PetsData;

public class ProviderHomePageDao {

    public List<PetsData> getPetsByProvider(int providerID) {

    List<PetsData> petList = new ArrayList<>();

    MySqlConnector db = new MySqlConnector();
    Connection conn = db.openConnection();

    String query = "SELECT * FROM Pets WHERE providerID = ?";

    try {

        java.sql.PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, providerID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            PetsData pet = new PetsData(
                rs.getInt("petID"),
                rs.getInt("providerID"),
                rs.getString("petName"),
                rs.getString("petBreed"),
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
        db.closeConnection(conn);

    } catch (Exception e) {
        System.out.println("Provider DAO Error: " + e);
    }

    return petList;
}
}
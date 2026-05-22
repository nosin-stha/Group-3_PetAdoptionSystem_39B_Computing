package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.PetsData;

public class AdopterHomePageDao {

    public List<PetsData> getAvailablePets() {

        List<PetsData> petList = new ArrayList<>();

        MySqlConnector db = new MySqlConnector();
        Connection conn = db.openConnection();

        String query = "SELECT * FROM Pets WHERE petAdoptionStatus = 'Available'";

        try {

            ResultSet rs = db.runQuery(conn, query);

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

            db.closeConnection(conn);

        } catch (Exception e) {
            System.out.println("Adopter DAO Error: " + e);
        }

        return petList;
    }
}

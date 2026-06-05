package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.PetsData;
import model.ProviderData;

public class ShelterViewPageDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ArrayList<PetsData> getPetsByProvider(int providerID) {
        ArrayList<PetsData> petList = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM Pets WHERE providerID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, providerID);
            ResultSet rs = pst.executeQuery();
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
        } catch (Exception e) {
            System.out.println("ShelterViewPageDAO getPetsByProvider Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return petList;
    }

    public ProviderData getProviderByID(int providerID) {
        ProviderData provider = null;
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM Providers WHERE providerID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, providerID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                provider = new ProviderData();
                provider.setProviderID(rs.getInt("providerID"));
                provider.setShelterName(rs.getString("shelterName"));
                provider.setLicenseID(rs.getString("licenseID"));
                provider.setUsername(rs.getString("proUsername"));
                provider.setPassword(rs.getString("proPassword"));
                provider.setEmail(rs.getString("proEmail"));
                provider.setPhoneNumber(rs.getString("proPhoneNo"));
                provider.setAddress(rs.getString("proAddress"));
                provider.setStartWorkHour(rs.getString("startWorkHour"));
                provider.setEndWorkHour(rs.getString("endWorkHour"));
                provider.setStartWorkDay(rs.getString("startWorkDay"));
                provider.setEndWorkDay(rs.getString("endWorkDay"));
                provider.setMissionStatement(rs.getString("proMissionStatement"));
                provider.setAdoptionPolicy(rs.getString("proAdoptionPolicy"));
                provider.setPfp(rs.getString("proPfp"));
            }
        } catch (Exception e) {
            System.out.println("ShelterViewPageDAO getProviderByID Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return provider;
    }
}
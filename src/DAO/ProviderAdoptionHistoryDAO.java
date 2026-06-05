package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.AdoptionRequestData;

public class ProviderAdoptionHistoryDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ArrayList<AdoptionRequestData> getProviderAdoptionHistory(int providerID) {
        ArrayList<AdoptionRequestData> list = new ArrayList<>();
        Connection conn = mysql.openConnection();
        try {
            String sql = "SELECT ar.adoptionID, ar.adopterID, ar.petID, "
                       + "ar.reqFullName, ar.reqEmail, ar.reqPhoneNo, "
                       + "ar.reqAddress, ar.reqReason, ar.adoptionStatus, "
                       + "p.petName, p.petType, p.petGender, p.petAge, p.imagePath "
                       + "FROM AdoptionRequests ar "
                       + "JOIN Pets p ON ar.petID = p.petID "
                       + "WHERE p.providerID = ? "
                       + "AND p.petAdoptionStatus = 'Adopted' "
                       + "AND ar.adoptionStatus = 'Accepted'";

            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, providerID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                AdoptionRequestData data = new AdoptionRequestData(
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
                list.add(data);
            }

            System.out.println("History list size: " + list.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }

        return list;
    }

    public int getTotalAdoptionRequests(int providerID) {
        int count = 0;
        Connection conn = mysql.openConnection();
        try {
            String sql = "SELECT COUNT(*) FROM AdoptionRequests ar "
                       + "JOIN Pets p ON ar.petID = p.petID "
                       + "WHERE p.providerID = ? "
                       + "AND p.petAdoptionStatus = 'Adopted' "
                       + "AND ar.adoptionStatus = 'Accepted'";

            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, providerID);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) count = rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }

        return count;
    }
}
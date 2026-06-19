package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProviderProfileDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ResultSet getProviderByID(int providerID){

        ResultSet rs = null;

        try {
            Connection conn = mysql.openConnection();

            String sql = "SELECT proUsername, shelterName, licenseID, "
                       + "proPhoneNo, proEmail, proAddress, "
                       + "startWorkHour, endWorkHour, "
                       + "startWorkDay, endWorkDay, "
                       + "proMissionStatement, proAdoptionPolicy "
                       + "FROM Providers WHERE providerID = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, providerID);

            rs = ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

   
    public boolean updateProviderProfile(
            int providerID,
            String username,
            String shelterName,
            String licenseID,
            String phone,
            String email,
            String address,
            String startTime,
            String endTime,
            String startDay,
            String endDay,
            String mission,
            String policy,
            String imagePath){

        try {
            Connection conn = mysql.openConnection();

            String sql = "UPDATE Providers SET "
                       + "proUsername=?, shelterName=?, licenseID=?, "
                       + "proPhoneNo=?, proEmail=?, proAddress=?, "
                       + "startWorkHour=?, endWorkHour=?, "
                       + "startWorkDay=?, endWorkDay=?, "
                       + "proMissionStatement=?, proAdoptionPolicy=?, "
                       + "proPfp=? "
                       + "WHERE providerID=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, shelterName);
            ps.setString(3, licenseID);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, address);
            ps.setString(7, startTime);
            ps.setString(8, endTime);
            ps.setString(9, startDay);
            ps.setString(10, endDay);
            ps.setString(11, mission);
            ps.setString(12, policy);
            ps.setString(13, imagePath);
            ps.setInt(14, providerID);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
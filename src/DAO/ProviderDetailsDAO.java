/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Dell
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;  // removed List import
import model.ProviderData;

public class ProviderDetailsDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ProviderData getProviderById(int providerID) {
        String sql = "SELECT * FROM Providers WHERE providerID = ?";
        Connection conn = mysql.openConnection();
        if (conn == null) return null;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, providerID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (Exception e) {
            System.out.println("Get Provider By ID Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public ProviderData getProviderByPetId(int petID) {
        String sql = "SELECT p.* FROM Providers p "
                   + "JOIN Pets pt ON pt.providerID = p.providerID "
                   + "WHERE pt.petID = ?";
        Connection conn = mysql.openConnection();
        if (conn == null) return null;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, petID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (Exception e) {
            System.out.println("Get Provider By Pet ID Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public ArrayList<ProviderData> getAllProviders() { 
        String sql = "SELECT * FROM Providers";
        ArrayList<ProviderData> list = new ArrayList<>(); 
        Connection conn = mysql.openConnection();
        if (conn == null) return list;
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Get All Providers Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    private ProviderData mapRow(ResultSet rs) throws Exception {
        ProviderData provider = new ProviderData();
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
        return provider;
    }
}
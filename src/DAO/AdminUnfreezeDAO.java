package DAO;

import database.MySqlConnector;
import java.sql.*;
import java.util.ArrayList;
import model.UnfreezeRequestData;

public class AdminUnfreezeDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ArrayList<UnfreezeRequestData> getAllRequests() {

        ArrayList<UnfreezeRequestData> list = new ArrayList<>();
        Connection conn = mysql.openConnection();

        String sql = "SELECT * FROM UnfreezeRequests"; // adjust if needed

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                UnfreezeRequestData data = new UnfreezeRequestData();
                data.setProviderID(rs.getInt("providerID"));
                data.setProviderName(rs.getString("providerName"));
                data.setEmail(rs.getString("email"));
                data.setReports(rs.getInt("reports"));
                data.setRequestDetail(rs.getString("requestDetail"));
                data.setStatus(rs.getString("status"));
                list.add(data);
            }

        } catch (Exception e) {
            System.out.println("AdminUnfreezeDAO Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return list;
    }

    public void updateStatus(int providerID, String status) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE UnfreezeRequests SET status=? WHERE providerID=?";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setInt(2, providerID);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update Status Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
    }
    public UnfreezeRequestData findByProviderName(String name) {

    Connection conn = mysql.openConnection();
    String sql = "SELECT * FROM UnfreezeRequests WHERE providerName = ?";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, name);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            UnfreezeRequestData data = new UnfreezeRequestData();
            data.setProviderID(rs.getInt("providerID"));
            data.setProviderName(rs.getString("providerName"));
            data.setEmail(rs.getString("email"));
            data.setReports(rs.getInt("reports"));
            data.setRequestDetail(rs.getString("requestDetail"));
            data.setStatus(rs.getString("status"));
            return data;
        }

    } catch (Exception e) {
        System.out.println("Find provider error: " + e.getMessage());
    } finally {
        mysql.closeConnection(conn);
    }

    return null;
}
}
package DAO;

import database.MySqlConnector;
import java.sql.*;
import java.util.ArrayList;
import model.UnfreezeRequestData;

public class AdminUnfreezeDAO {

    MySqlConnector mysql = new MySqlConnector();

    public ArrayList<UnfreezeRequestData> getAllRequests() {

        ArrayList<UnfreezeRequestData> list = new ArrayList<>();

        Connection conn = null;

        String sql = """
            SELECT r.recoverID, r.providerID, p.shelterName, p.proEmail,
                   r.recoverReqReason, r.recoverReqStatus
            FROM RecoverRequest r
            INNER JOIN Providers p ON r.providerID = p.providerID
            ORDER BY r.recoverID DESC
        """;

        try {
            conn = mysql.openConnection();

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                UnfreezeRequestData data = new UnfreezeRequestData();
                data.setRecoverID(rs.getInt("recoverID"));
                data.setProviderID(rs.getInt("providerID"));
                data.setProviderName(rs.getString("shelterName"));
                data.setEmail(rs.getString("proEmail"));
                data.setRequestDetail(rs.getString("recoverReqReason"));
                data.setStatus(rs.getString("recoverReqStatus"));
                list.add(data);
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            System.out.println("getAllRequests Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return list;
    }

    public boolean updateStatus(int recoverID, String status) {

        Connection conn = null;

        String sql = """
            UPDATE RecoverRequest
            SET recoverReqStatus = ?
            WHERE recoverID = ?
        """;

        try {
            conn = mysql.openConnection();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, status);
            pst.setInt(2, recoverID);

            boolean result = pst.executeUpdate() > 0;
            pst.close();

            return result;

        } catch (Exception e) {
            System.out.println("updateStatus Error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public boolean activateProvider(int providerID) {

        Connection conn = null;

        String sql = """
            UPDATE Providers
            SET proStatus = 'Active'
            WHERE providerID = ?
        """;

        try {
            conn = mysql.openConnection();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, providerID);

            boolean result = pst.executeUpdate() > 0;
            pst.close();

            return result;

        } catch (Exception e) {
            System.out.println("activateProvider Error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public UnfreezeRequestData findByProviderName(String name) {

        Connection conn = null;

        String sql = """
            SELECT r.recoverID, r.providerID, p.shelterName, p.proEmail,
                   r.recoverReqReason, r.recoverReqStatus
            FROM RecoverRequest r
            INNER JOIN Providers p ON r.providerID = p.providerID
            WHERE p.shelterName = ?
        """;

        try {
            conn = mysql.openConnection();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UnfreezeRequestData data = new UnfreezeRequestData();
                data.setRecoverID(rs.getInt("recoverID"));
                data.setProviderID(rs.getInt("providerID"));
                data.setProviderName(rs.getString("shelterName"));
                data.setEmail(rs.getString("proEmail"));
                data.setRequestDetail(rs.getString("recoverReqReason"));
                data.setStatus(rs.getString("recoverReqStatus"));

                rs.close();
                pst.close();

                return data;
            }

            rs.close();
            pst.close();

        } catch (Exception e) {
            System.out.println("findByProviderName Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }

        return null;
    }
}
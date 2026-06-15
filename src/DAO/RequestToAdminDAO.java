package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestToAdminDAO {

    /**
     * Checks if an account exists and returns its status.
     * Returns: -1 = not found, 0 = active, 1 = disabled
     */
    public int getAccountStatus(String email, String password) {
        String sql = "SELECT proStatus FROM Providers WHERE proEmail = ? AND proPassword = ?";

        try (Connection conn = new MySqlConnector().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return -1;
            }

            String status = rs.getString("proStatus");
            return status.equalsIgnoreCase("disabled") ? 1 : 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns the providerID only if the account is disabled.
     * Returns -1 if not found or not disabled.
     */
    public int getDisabledProviderID(String email, String password) {
        String sql = "SELECT providerID FROM Providers WHERE proEmail = ? AND proPassword = ? AND proStatus = 'disabled'";

        try (Connection conn = new MySqlConnector().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("providerID");
            }
            return -1;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Checks if a provider already has a pending recovery request.
     */
    public boolean hasPendingRequest(int providerID) {
        String sql = "SELECT COUNT(*) FROM RecoverRequest WHERE providerID = ? AND recoverReqStatus = 'pending'";

        try (Connection conn = new MySqlConnector().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, providerID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new recovery request for the given provider.
     */
    public boolean insertRecoverRequest(int providerID, String reason) {
        String sql = "INSERT INTO RecoverRequest (providerID, recoverReqReason, recoverReqStatus) VALUES (?, ?, 'pending')";

        try (Connection conn = new MySqlConnector().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, providerID);
            ps.setString(2, reason);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
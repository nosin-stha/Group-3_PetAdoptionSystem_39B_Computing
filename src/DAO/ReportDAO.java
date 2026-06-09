package DAO;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportDAO {

    MySqlConnector mysql = new MySqlConnector();

    public boolean insertReport(int adopterID, int providerID, String reason) {

        try {
            Connection conn = mysql.openConnection();

            String sql = "INSERT INTO AccountReport "
                       + "(adopterID, providerID, reportReason) "
                       + "VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, adopterID);
            ps.setInt(2, providerID);
            ps.setString(3, reason);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
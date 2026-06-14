/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Dell
 */


import java.sql.*;
import java.util.ArrayList;

public class AdminAllAccountsDAO {

    private Connection connection;

    public AdminAllAccountsDAO(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Object[]> getAllAccounts() {
    ArrayList<Object[]> accounts = new ArrayList<>();

    if (connection == null) {
        System.err.println("ERROR: Database connection is null!");
        return accounts;
    }
    
    String sql =
    "SELECT 'Adopter' AS accountType, " +
    "a.adpUsername AS username, " +
    "a.adpEmail AS email, " +
    "0 AS reports, " +                        
    "a.adpStatus AS status " +
    "FROM Adopters a " +
    "UNION ALL " +
    "SELECT 'Provider' AS accountType, " +
    "p.shelterName AS username, " +
    "p.proEmail AS email, " +
    "COUNT(r.reportID) AS reports, " +         
    "p.proStatus AS status " +
    "FROM Providers p " +
    "LEFT JOIN AccountReport r " +             
    "    ON p.providerID = r.providerID " +
    "GROUP BY p.providerID, p.shelterName, p.proEmail, p.proStatus";
    
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        System.out.println("Query executed successfully");
        while (rs.next()) {
            Object[] row = {
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getInt(4),
                rs.getString(5)
            };
            System.out.println("Row fetched: " + row[0] + " | " + row[1]);
            accounts.add(row);
        }
        System.out.println("Total rows fetched: " + accounts.size());
    } catch (SQLException e) {
        System.err.println("SQL Error in getAllAccounts(): " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
        e.printStackTrace();
    }
    return accounts;
}
}
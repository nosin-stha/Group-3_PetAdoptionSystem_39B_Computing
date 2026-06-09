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
    
    // Guard against null connection
    if (connection == null) {
        System.err.println("ERROR: Database connection is null!");
        return accounts;
    }
    
    String sql =
        "SELECT 'Adopter' AS accountType, " +
        "adpUsername AS username, " +
        "adpEmail AS email, " +
        "'0' AS reports, " +
        "adpStatus AS status " +
        "FROM Adopters " +
        "UNION ALL " +
        "SELECT 'Provider' AS accountType, " +
        "shelterName AS username, " +
        "proEmail AS email, " +
        "'0' AS reports, " +
        "proStatus AS status " +
        "FROM Providers";
    
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        System.out.println("Query executed successfully");
        while (rs.next()) {
            Object[] row = {
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
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
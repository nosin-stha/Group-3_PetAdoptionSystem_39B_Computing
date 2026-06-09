/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */



import DAO.AdminAllAccountsDAO;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import view.AdminAllAccountStatus;

public class AdminAllAccountsController {

    private AdminAllAccountStatus view;
    private AdminAllAccountsDAO dao;

    public AdminAllAccountsController(AdminAllAccountStatus view, Connection connection) {
        this.view = view;
        this.dao = new AdminAllAccountsDAO(connection);
        configureTable();
        loadAllAccounts();
    }

    private void configureTable() {
        JTable table = view.getTable();
        table.setRowHeight(32);
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
    }

    private void loadAllAccounts() {
        ArrayList<Object[]> accounts = dao.getAllAccounts();
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);

        if (accounts.isEmpty())
            System.err.println("WARNING: No accounts returned from DAO.");

        for (Object[] row : accounts)
            model.addRow(row);

        System.out.println("Accounts loaded into table: " + accounts.size());
    }

    
    private static class StatusCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);

            String status = value != null ? value.toString().trim().toLowerCase() : "";

            if (!isSelected) {
                switch (status) {
                    case "active" -> { setBackground(new Color(198, 239, 206)); setForeground(new Color(0, 97, 0)); }
                    case "reported" -> { setBackground(new Color(255, 235, 156)); setForeground(new Color(156, 87, 0)); }
                    case "disabled" -> { setBackground(new Color(255, 199, 206)); setForeground(new Color(156, 0, 6)); }
                    default -> { setBackground(Color.WHITE);setForeground(Color.BLACK); }
                }
            } else {
                setForeground(Color.WHITE);
            }

            return this;
        }
    }
}
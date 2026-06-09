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
import java.awt.Font;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JLabel;
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
        this.dao  = new AdminAllAccountsDAO(connection);
        configureTable();
        loadAllAccounts();
    }

    private void configureTable() {
        JTable table = view.getTable();

        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(new Color(255, 153, 51));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setRowHeight(40);


        table.getColumnModel().getColumn(1).setCellRenderer(new WrappedTextRenderer());

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

    private static class WrappedTextRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            int colWidth = table.getColumnModel().getColumn(column).getWidth();
            String text  = value == null ? "" : value.toString();
            String html  = "<html><body style='width:" + Math.max(colWidth - 10, 60)
                           + "px; padding:2px;'>" + text + "</body></html>";

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, html, isSelected, hasFocus, row, column);
            label.setOpaque(true);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            label.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
            return label;
        }
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
            setFont(new Font("Segoe UI", Font.BOLD, 13));

            String status = value != null ? value.toString().trim().toLowerCase() : "";

        
            setBackground(Color.WHITE);

            switch (status) {
                case "active"   -> setForeground(new Color(0, 130, 0));
                case "reported" -> setForeground(new Color(200, 120, 0));
                case "disabled" -> setForeground(new Color(198, 40, 40));
                default         -> setForeground(Color.BLACK);
            }

            return this;
        }
    }
}
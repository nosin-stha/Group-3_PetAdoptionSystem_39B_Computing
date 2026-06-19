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
    private ArrayList<Object[]> allAccounts;

    private DefaultTableModel dataModel;   
    private boolean showingEmptyState = false;

    public AdminAllAccountsController(AdminAllAccountStatus view, Connection connection) {
        this.view = view;
        this.dao  = new AdminAllAccountsDAO(connection);
        view.initSearchPlaceholder();
        configureTable();
        loadAllAccounts();
        attachListeners();
    }

    private void attachListeners() {
        view.addSearchListener(e -> applyFilters());
        view.addStatusFilterListener(e -> applyFilters());
        view.addResetListener(e -> resetFilters());
    }

    private void resetFilters() {
        view.resetFilters();

        dataModel.setRowCount(0);
        for (Object[] row : allAccounts)
            dataModel.addRow(row);

        updateEmptyState(allAccounts.isEmpty());
    }

    
    private void applyFilters() {
        String keyword      = view.getSearchText().trim().toLowerCase();
        String statusFilter = view.getSelectedStatus().trim().toLowerCase();

        ArrayList<Object[]> filtered = new ArrayList<>();

        for (Object[] row : allAccounts) {
            String username = row[1] != null ? row[1].toString().toLowerCase() : "";
            String email    = row[2] != null ? row[2].toString().toLowerCase() : "";
            String status   = row[4] != null ? row[4].toString().toLowerCase() : "";

            boolean matchesSearch = keyword.isEmpty()
                    || username.contains(keyword)
                    || email.contains(keyword);

            boolean matchesStatus = statusFilter.isEmpty()
                    || statusFilter.equals("status")  
                    || status.equals(statusFilter);

            if (matchesSearch && matchesStatus) {
                filtered.add(row);
            }
        }

        if (filtered.isEmpty()) {
            updateEmptyState(true);
            return;
        }

        updateEmptyState(false);
        dataModel.setRowCount(0);
        for (Object[] row : filtered)
            dataModel.addRow(row);
    }

    private void configureTable() {
        JTable table = view.getTable();
        dataModel = view.getTableModel();

        styleTableAppearance(table);

        table.getColumnModel().getColumn(1).setCellRenderer(new WrappedTextRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setResizable(false);
    }

    private void styleTableAppearance(JTable table) {
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(new Color(255, 153, 51));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setRowHeight(40);
    }

    private void loadAllAccounts() {
        allAccounts = dao.getAllAccounts(); 
        if (allAccounts == null) allAccounts = new ArrayList<>();

        if (allAccounts.isEmpty())
            System.err.println("WARNING: No accounts returned from DAO.");

        dataModel.setRowCount(0);
        for (Object[] row : allAccounts)
            dataModel.addRow(row);

        updateEmptyState(allAccounts.isEmpty());

        System.out.println("Accounts loaded into table: " + allAccounts.size());
    }

    
    private void updateEmptyState(boolean empty) {
        JTable table = view.getTable();

        if (empty && !showingEmptyState) {
            DefaultTableModel emptyModel = new DefaultTableModel(
                new String[]{"Message"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            emptyModel.addRow(new Object[]{"No accounts found"});

            table.setModel(emptyModel);
            table.getTableHeader().setVisible(false);
            table.setRowHeight(0, 200);

            table.getColumnModel().getColumn(0).setCellRenderer(new EmptyMessageRenderer());

            showingEmptyState = true;

        } else if (!empty && showingEmptyState) {
            table.setModel(dataModel);
            table.getTableHeader().setVisible(true);

            styleTableAppearance(table);
            table.getColumnModel().getColumn(1).setCellRenderer(new WrappedTextRenderer());
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
            for (int i = 0; i < table.getColumnCount(); i++)
                table.getColumnModel().getColumn(i).setResizable(false);

            showingEmptyState = false;
        }
    }

    private static class EmptyMessageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 16));
            label.setForeground(new Color(180, 180, 180));
            label.setBackground(Color.WHITE);
            label.setOpaque(true);
            return label;
        }
    }

    private static class WrappedTextRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
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
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setBackground(Color.WHITE);
            String status = value != null ? value.toString().trim().toLowerCase() : "";
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
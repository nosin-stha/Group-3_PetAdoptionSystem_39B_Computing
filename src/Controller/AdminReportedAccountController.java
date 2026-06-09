/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */


import DAO.AdminAccountReportHandleDAO;
import view.AdminReportedAccountManagement;
import view.AdminReportedProvidersHandleBoardPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AdminReportedAccountController {

    private AdminReportedAccountManagement view;
    private AdminAccountReportHandleDAO dao;

    public AdminReportedAccountController(AdminReportedAccountManagement view,
                                          java.sql.Connection connection) {
        this.view = view;
        this.dao  = new AdminAccountReportHandleDAO(connection);
        loadAllReportedProviders();
    }

    public void loadAllReportedProviders() {
        ArrayList<Object[]> providers = dao.getReportedProviders();

        JPanel container = view.getScrollPanel();
        container.removeAll();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        for (Object[] p : providers) {
            int    providerID = Integer.parseInt(p[0].toString());
            String name       = p[1].toString();
            String email      = p[2].toString();
            String phone      = p[3].toString();
            String logo       = p[4] == null ? "" : p[4].toString();
            int    total      = Integer.parseInt(p[5].toString());

            AdminReportedProvidersHandleBoardPanel board =
                    new AdminReportedProvidersHandleBoardPanel();

            board.setShelterName(name);
            board.setShelterEmail(email);
            board.setShelterPhone(phone);
            board.setTotalReportCount(total);

            if (!logo.isEmpty()) {
                ImageIcon icon = new ImageIcon(logo);
                Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                board.setShelterLogo(new ImageIcon(img));
            }

            loadReports(board, providerID);

            container.add(board);
            container.add(Box.createVerticalStrut(15));
        }

        container.revalidate();
        container.repaint();
    }

    private void loadReports(AdminReportedProvidersHandleBoardPanel board, int providerID) {
        JTable table = board.getAdminReportsHandleTable();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"reportID", "providerID", "Adopter Name", "Email", "Report Reason", "Status", "Action"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6;
            }
        };

        ArrayList<Object[]> reports = dao.getReportsByProvider(providerID);

        for (Object[] r : reports) {
            int    reportID = Integer.parseInt(r[0].toString());
            String username = r[1].toString();
            String email    = r[2].toString();
            String reason   = r[3].toString();
            String status   = r[4].toString();

            boolean done = status.equalsIgnoreCase("Resolved") || status.equalsIgnoreCase("Disabled");

            model.addRow(new Object[]{
                    reportID,
                    providerID,
                    username,
                    email,
                    reason,        
                    status,
                    done ? "done" : "pending"
            });
        }

        table.setModel(model);
        styleTable(table);
        hideColumn(table, 0);
        hideColumn(table, 1);


        table.getColumn("Report Reason").setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                            boolean isSelected, boolean hasFocus,
                                                            int row, int col) {
                int colWidth = tbl.getColumnModel()
                                  .getColumn(tbl.getColumnModel().getColumnIndex("Report Reason"))
                                  .getWidth();
                String html = "<html><body style='width:" + Math.max(colWidth - 10, 100)
                              + "px; padding:4px;'>"
                              + (value == null ? "" : value.toString())
                              + "</body></html>";
                JLabel label = new JLabel(html);
                label.setOpaque(true);
                label.setVerticalAlignment(SwingConstants.TOP);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                label.setBackground(isSelected ? new Color(230, 240, 255) : Color.WHITE);
                return label;
            }
        });

        table.getColumn("Status").setCellRenderer(new StatusRenderer());
        table.getColumn("Action").setCellRenderer(new ActionRenderer());
        table.getColumn("Action").setCellEditor(new ActionEditor(table));


        updateRowHeights(table);

        table.getColumnModel().addColumnModelListener(
            new javax.swing.event.TableColumnModelListener() {
                @Override
                public void columnMarginChanged(javax.swing.event.ChangeEvent e) { updateRowHeights(table); }
                @Override
                public void columnAdded(javax.swing.event.TableColumnModelEvent e) {}
                @Override
                public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {}
                @Override
                public void columnMoved(javax.swing.event.TableColumnModelEvent e) {}
                @Override
                public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {}
            }
        );
    }

 
    private void updateRowHeights(JTable table) {
        int reasonColIndex = -1;
        for (int c = 0; c < table.getColumnCount(); c++) {
            if ("Report Reason".equals(table.getColumnName(c))) {
                reasonColIndex = c;
                break;
            }
        }
        if (reasonColIndex < 0) return;

        for (int row = 0; row < table.getRowCount(); row++) {
            Object val  = table.getValueAt(row, reasonColIndex);
            String text = val == null ? "" : val.toString();
            int    lines = (int) Math.ceil(text.length() / 80.0);
            lines = Math.max(lines, 1);
            int height = Math.max(60, lines * 20 + 20);
            table.setRowHeight(row, height);
        }
    }

    private void styleTable(JTable table) {
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(255, 153, 51));
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void hideColumn(JTable table, int col) {
        table.getColumnModel().getColumn(col).setMinWidth(0);
        table.getColumnModel().getColumn(col).setMaxWidth(0);
        table.getColumnModel().getColumn(col).setWidth(0);
    }



    private class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                        boolean isSelected, boolean hasFocus,
                                                        int row, int col) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            String status = value == null ? "" : value.toString().toLowerCase();
            switch (status) {
                case "pending"  -> label.setForeground(new Color(255, 193, 7));
                case "resolved" -> label.setForeground(new Color(56, 142, 60));
                case "disabled" -> label.setForeground(new Color(198, 40, 40));
                default         -> label.setForeground(Color.BLACK);
            }
            return label;
        }
    }

    private class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                        boolean isSelected, boolean hasFocus,
                                                        int row, int col) {
            JPanel outer = new JPanel(new GridBagLayout()); // GridBagLayout auto-centers
            outer.setBackground(Color.WHITE);

            if ("done".equals(value)) {
                String status = table.getValueAt(row, 5).toString();
                JLabel label  = new JLabel(status);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (status.equalsIgnoreCase("Resolved"))
                    label.setForeground(new Color(56, 142, 60));
                else if (status.equalsIgnoreCase("Disabled"))
                    label.setForeground(new Color(198, 40, 40));
                outer.add(label);
            } else {
                JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
                btns.setBackground(Color.WHITE);
                btns.add(makeBtn("Resolve", new Color(56, 142, 60)));
                btns.add(makeBtn("Disable", new Color(198, 40, 40)));
                outer.add(btns);
            }
            return outer;
        }
    }

    private class ActionEditor extends DefaultCellEditor {
        private final JTable table;
        private int editingRow;

        public ActionEditor(JTable table) {
            super(new JTextField());
            this.table = table;
            setClickCountToStart(1);
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object value,
                                                      boolean isSelected, int row, int col) {
            this.editingRow = row;

            JPanel outer = new JPanel(new GridBagLayout());
            outer.setBackground(Color.WHITE);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            btns.setBackground(Color.WHITE);

            JButton resolve = makeBtn("Resolve", new Color(56, 142, 60));
            JButton disable = makeBtn("Disable", new Color(198, 40, 40));

            resolve.addActionListener(e -> {
                fireEditingStopped();
                handleResolve(table, editingRow);
            });

            disable.addActionListener(e -> {
                fireEditingStopped();
                handleDismiss(table, editingRow);
            });

            btns.add(resolve);
            btns.add(disable);
            outer.add(btns);
            return outer;
        }

        @Override
        public Object getCellEditorValue() { return "pending"; }
    }


    private void handleResolve(JTable table, int row) {
        int reportID   = Integer.parseInt(table.getValueAt(row, 0).toString());
        int providerID = Integer.parseInt(table.getValueAt(row, 1).toString());

        int confirm = JOptionPane.showConfirmDialog(view,
                "Resolve this report? Provider account remains ACTIVE.",
                "Confirm Resolve",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.resolveReport(reportID, providerID)) {
                table.setValueAt("Resolved", row, 5);
                table.setValueAt("done", row, 6);
                table.repaint();
            }
        }
    }

    private void handleDismiss(JTable table, int row) {
        int reportID   = Integer.parseInt(table.getValueAt(row, 0).toString());
        int providerID = Integer.parseInt(table.getValueAt(row, 1).toString());

        int confirm = JOptionPane.showConfirmDialog(view,
                "Disable provider? This will block their login and resolve all other reports.",
                "Confirm Disable",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.dismissReport(reportID, providerID)) {
                // Mark THIS report as Disabled
                table.setValueAt("Disabled", row, 5);
                table.setValueAt("done", row, 6);

                // Cascade: resolve all other pending reports for this provider in DB and UI
                dao.resolveAllOtherReports(providerID, reportID);

                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (i == row) continue;
                    if ("Pending".equalsIgnoreCase(model.getValueAt(i, 5).toString())) {
                        model.setValueAt("Resolved", i, 5);
                        model.setValueAt("done", i, 6);
                    }
                }
                table.repaint();
            }
        }
    }



    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(80, 30));
        return btn;
    }
}

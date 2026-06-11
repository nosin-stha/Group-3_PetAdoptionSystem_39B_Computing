package Controller;

import DAO.AdminUnfreezeDAO;
import model.UnfreezeRequestData;
import view.AdminUnfreezeRequestManagement;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class AdminUnfreezeController {

    private final AdminUnfreezeRequestManagement view;
    private final AdminUnfreezeDAO dao = new AdminUnfreezeDAO();

    public AdminUnfreezeController(AdminUnfreezeRequestManagement view) {
        this.view = view;
        setupTable();
        loadRequests();
    }

    private void setupTable() {
        JTable table = view.getPetRequestsTable();

        table.setRowHeight(70);
        table.setFillsViewportHeight(false);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(170);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setMinWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setMinWidth(150);
        table.getColumnModel().getColumn(4).setCellRenderer(new WrapTextRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumn("Status").setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(6).setPreferredWidth(170);
        table.getColumnModel().getColumn(6).setMinWidth(170);
        table.getColumn("Action").setCellRenderer(new ActionRenderer());
        table.getColumn("Action").setCellEditor(new ActionEditor(new JCheckBox()));

        int rowHeight = 70;
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        table.setPreferredScrollableViewportSize(
            new java.awt.Dimension(900, rowHeight * 3 + headerHeight)
        );
    }

    private void loadRequests() {
        DefaultTableModel model = (DefaultTableModel) view.getPetRequestsTable().getModel();
        model.setRowCount(0);

        ArrayList<UnfreezeRequestData> list = dao.getAllRequests();
        for (UnfreezeRequestData data : list) {
            model.addRow(new Object[]{
                data.getProviderID(),
                data.getProviderName(),
                data.getEmail(),
                data.getReports(),
                data.getRequestDetail(),
                data.getStatus(),
                "Action"
            });
        }
        view.setTotalProviders(String.valueOf(list.size()));
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            String status = value != null ? value.toString().toLowerCase() : "";
            switch (status) {
                case "pending"  -> label.setForeground(new Color(204, 153, 0));
                case "approved" -> label.setForeground(new Color(0, 153, 0));
                case "declined" -> label.setForeground(new Color(204, 0, 0));
                default         -> label.setForeground(table.getForeground());
            }
            label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD));
            return label;
        }
    }

    class ActionRenderer extends JPanel implements TableCellRenderer {
        JButton accept = new JButton("Accept");
        JButton decline = new JButton("Decline");

        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            accept.setBackground(new Color(0, 153, 0));
            accept.setForeground(Color.WHITE);
            decline.setBackground(new Color(204, 0, 0));
            decline.setForeground(Color.WHITE);
            add(accept);
            add(decline);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        JPanel panel = new JPanel();
        JButton accept = new JButton("Accept");
        JButton decline = new JButton("Decline");

        public ActionEditor(JCheckBox checkBox) {
            panel.setLayout(new FlowLayout());
            accept.setBackground(new Color(0, 153, 0));
            accept.setForeground(Color.WHITE);
            decline.setBackground(new Color(204, 0, 0));
            decline.setForeground(Color.WHITE);
            panel.add(accept);
            panel.add(decline);
            accept.addActionListener(e -> handleAction("approved"));
            decline.addActionListener(e -> handleAction("declined"));
        }

        private void handleAction(String status) {
            int row = view.getPetRequestsTable().getEditingRow();
            if (row == -1) row = view.getPetRequestsTable().getSelectedRow();
            if (row == -1) return;

            int providerID = (int) view.getPetRequestsTable().getModel().getValueAt(row, 0);
            dao.updateStatus(providerID, status);
            loadRequests();
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return ""; }
    }

    class WrapTextRenderer extends JTextArea implements TableCellRenderer {
        public WrapTextRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            setText(value != null ? value.toString() : "");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
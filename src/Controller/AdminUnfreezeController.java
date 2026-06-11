package Controller;

import DAO.AdminUnfreezeDAO;
import model.UnfreezeRequestData;
import view.AdminUnfreezeRequestManagement;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminUnfreezeController {

    private final AdminUnfreezeRequestManagement view;
    private final AdminUnfreezeDAO dao = new AdminUnfreezeDAO();

    private static final String PENDING = "Pending";
    private static final String ACCEPTED = "Accepted";
    private static final String DENIED = "Denied";

    public AdminUnfreezeController(AdminUnfreezeRequestManagement view) {
        this.view = view;
        setupTable();
        loadRequests();
    }

    private void setupTable() {

        JTable table = view.getPetRequestsTable();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(70);

        TableColumnModel cm = table.getColumnModel();

        if (cm.getColumnCount() < 8) return;

        hideColumn(table, 0);
        hideColumn(table, 1);

        cm.getColumn(2).setPreferredWidth(180);
        cm.getColumn(3).setPreferredWidth(200);
        cm.getColumn(4).setPreferredWidth(120);
        cm.getColumn(5).setPreferredWidth(300);
        cm.getColumn(6).setPreferredWidth(100);
        cm.getColumn(7).setPreferredWidth(180);

        cm.getColumn(5).setCellRenderer(new WrapTextRenderer());
        cm.getColumn(6).setCellRenderer(new StatusRenderer());
        cm.getColumn(7).setCellRenderer(new ActionRenderer());
        cm.getColumn(7).setCellEditor(new ActionEditor(new JCheckBox()));
    }

    private void hideColumn(JTable table, int col) {
        TableColumn c = table.getColumnModel().getColumn(col);
        c.setMinWidth(0);
        c.setMaxWidth(0);
        c.setPreferredWidth(0);
        c.setWidth(0);
    }

    private void loadRequests() {

        DefaultTableModel model = (DefaultTableModel) view.getPetRequestsTable().getModel();
        model.setRowCount(0);

        ArrayList<UnfreezeRequestData> list = dao.getAllRequests();

        for (int i = 0; i < list.size(); i++) {

            UnfreezeRequestData d = list.get(i);

            model.addRow(new Object[]{
                    d.getRecoverID(),
                    d.getProviderID(),
                    d.getProviderName(),
                    d.getEmail(),
                    "Report",
                    d.getRequestDetail(),
                    d.getStatus(),
                    "Action"
            });
        }

        view.setTotalProviders(String.valueOf(list.size()));
    }

    private boolean isPending(JTable table, int row) {
        Object v = table.getModel().getValueAt(row, 6);
        return v != null && PENDING.equalsIgnoreCase(v.toString());
    }

    class StatusRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel l = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setOpaque(true);
            l.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            String s = value == null ? "" : value.toString();

            if (PENDING.equalsIgnoreCase(s)) {
                l.setForeground(new Color(204, 153, 0));
            } else if (ACCEPTED.equalsIgnoreCase(s)) {
                l.setForeground(new Color(0, 153, 0));
            } else if (DENIED.equalsIgnoreCase(s)) {
                l.setForeground(new Color(204, 0, 0));
            } else {
                l.setForeground(table.getForeground());
            }

            l.setFont(l.getFont().deriveFont(Font.BOLD));
            return l;
        }
    }

    class ActionRenderer extends JPanel implements TableCellRenderer {

        private final JButton accept = new JButton("Accept");
        private final JButton deny = new JButton("Deny");
        private final JLabel done = new JLabel("—");

        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 18));
            done.setForeground(Color.GRAY);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            removeAll();

            if (isPending(table, row)) {
                add(accept);
                add(deny);
            } else {
                add(done);
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            setSize(table.getColumnModel().getColumn(column).getWidth(),
                    table.getRowHeight(row));

            doLayout();

            return this;
        }
    }

    class ActionEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 18));
        private final JButton accept = new JButton("Accept");
        private final JButton deny = new JButton("Deny");
        private final JLabel done = new JLabel("—");

        public ActionEditor(JCheckBox cb) {

            done.setForeground(Color.GRAY);

            accept.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handle(ACCEPTED);
                }
            });

            deny.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handle(DENIED);
                }
            });
        }

        private void handle(String status) {

            int row = view.getPetRequestsTable().getEditingRow();
            if (row == -1) row = view.getPetRequestsTable().getSelectedRow();
            if (row == -1) {
                fireEditingStopped();
                return;
            }

            TableModel model = view.getPetRequestsTable().getModel();

            int recoverID = 0;
            int providerID = 0;

            try {
                recoverID = Integer.parseInt(model.getValueAt(row, 0).toString());
                providerID = Integer.parseInt(model.getValueAt(row, 1).toString());
            } catch (Exception ex) {
                fireEditingStopped();
                return;
            }

            if (!dao.updateStatus(recoverID, status)) {
                JOptionPane.showMessageDialog(view, "Update failed");
                fireEditingStopped();
                return;
            }

            if (ACCEPTED.equals(status)) {
                dao.activateProvider(providerID);
            }

            fireEditingStopped();
            loadRequests();
        }

        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            panel.removeAll();

            if (isPending(table, row)) {
                panel.add(accept);
                panel.add(deny);
            } else {
                panel.add(done);
            }

            panel.setSize(table.getColumnModel().getColumn(column).getWidth(),
                    table.getRowHeight(row));

            panel.doLayout();

            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }

    class WrapTextRenderer extends JTextArea implements TableCellRenderer {

        public WrapTextRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            setText(value == null ? "" : value.toString());
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
}
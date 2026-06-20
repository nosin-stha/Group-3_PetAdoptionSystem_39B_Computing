package Controller;
 
import DAO.AdminUnfreezeDAO;
import model.UnfreezeRequestData;
import view.AdminUnfreezeRequestManagement;
 
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
 
public class AdminUnfreezeController {
 
    private final AdminUnfreezeRequestManagement view;
    private final AdminUnfreezeDAO dao = new AdminUnfreezeDAO();
    private ArrayList<Object[]> allRequests;
 
    private static final String PENDING  = "Pending";
    private static final String ACCEPTED = "Accepted";
    private static final String DENIED   = "Denied";
 
    private static final String MSG_NO_RECORDS = "No unfreeze requests found";
    private static final String MSG_NO_MATCH   = "No records match your search or filter";
 
    
    private JScrollPane tableScrollPane;
    private JLabel      emptyLabel;
 
    public AdminUnfreezeController(AdminUnfreezeRequestManagement view) {
        this.view = view;
        setupTable();
        view.initSearchPlaceholder();
 
        view.addSearchTextListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e)  { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
 
        view.addSearchListener(e -> applyFilters());
        view.addStatusFilterListener(e -> applyFilters());
        view.addResetListener(e -> resetFilters());
        loadRequests();
    }
 
    // ── Data loading ──────────────────────────────────────────────────────────
 
    private void loadRequests() {
        ArrayList<UnfreezeRequestData> list = dao.getAllRequests();
        allRequests = new ArrayList<>();
        for (UnfreezeRequestData d : list) {
            allRequests.add(new Object[]{
                d.getRecoverID(),
                d.getProviderID(),
                "<html><div style='width:150px'>" + d.getProviderName() + "</div></html>",
                d.getEmail(),
                "Report",
                d.getRequestDetail(),
                d.getStatus(),
                "Action"
            });
        }
        applyFilters();
    }
 
    // ── Filtering ─────────────────────────────────────────────────────────────
 
    private void applyFilters() {
        if (allRequests == null) return;
 
        String keyword      = view.getSearchText() == null ? "" : view.getSearchText().trim().toLowerCase();
        String statusFilter = view.getSelectedStatus() == null ? "" : view.getSelectedStatus().trim().toLowerCase();
 
        boolean filtersActive = !keyword.isEmpty()
                || (!statusFilter.isEmpty() && !statusFilter.equals("status"));
 
        ArrayList<Object[]> filtered = new ArrayList<>();
        for (Object[] row : allRequests) {
            String rawName = row[2] != null ? row[2].toString() : "";
            String name    = rawName.replaceAll("<[^>]*>", "").trim().toLowerCase();
            String email   = row[3] != null ? row[3].toString().toLowerCase() : "";
            String status  = row[6] != null ? row[6].toString().toLowerCase() : "";
 
            boolean matchesSearch = keyword.isEmpty()
                    || name.contains(keyword)
                    || email.contains(keyword);
 
            boolean matchesStatus = statusFilter.isEmpty()
                    || statusFilter.equals("status")
                    || status.equals(statusFilter);
 
            if (matchesSearch && matchesStatus)
                filtered.add(row);
        }
 
        String emptyMsg = filtersActive ? MSG_NO_MATCH : MSG_NO_RECORDS;
        populateTable(filtered, emptyMsg);
    }
 
    private void resetFilters() {
        view.resetFilters();
        applyFilters();
    }
 
    // ── Table population ──────────────────────────────────────────────────────
 
    private void populateTable(ArrayList<Object[]> rows, String emptyMessage) {
        JTable table = view.getPetRequestsTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : rows)
            model.addRow(row);
 
        view.setTotalProviders(String.valueOf(rows.size()));
 
        // Swap viewport: show label when empty, table when rows exist
        if (tableScrollPane != null && emptyLabel != null) {
            if (rows.isEmpty()) {
                emptyLabel.setText(emptyMessage);
                tableScrollPane.setViewportView(emptyLabel);
            } else {
                tableScrollPane.setViewportView(table);
            }
            tableScrollPane.revalidate();
            tableScrollPane.repaint();
        }
    }
 
    // ── Table setup ───────────────────────────────────────────────────────────
 
    private void setupTable() {
        JTable table = view.getPetRequestsTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(70);
 
        
        Component parent = table.getParent();
        while (parent != null && !(parent instanceof JScrollPane)) {
            parent = parent.getParent();
        }
        if (parent instanceof JScrollPane) {
            tableScrollPane = (JScrollPane) parent;
        }
 
        
        emptyLabel = new JLabel("", SwingConstants.CENTER);
        emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emptyLabel.setForeground(new Color(160, 160, 160));
        emptyLabel.setOpaque(true);
        emptyLabel.setBackground(table.getBackground());
 
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
 
        cm.getColumn(2).setCellRenderer(new HtmlRenderer());
        cm.getColumn(5).setCellRenderer(new WrapTextRenderer());
        cm.getColumn(6).setCellRenderer(new StatusRenderer());
        cm.getColumn(7).setCellRenderer(new ActionRenderer());
        cm.getColumn(7).setCellEditor(new ActionEditor(new JCheckBox()));
 
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(255, 190, 86));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
 
        for (int i = 0; i < cm.getColumnCount(); i++)
            cm.getColumn(i).setResizable(false);
    }
 
    private void hideColumn(JTable table, int col) {
        TableColumn c = table.getColumnModel().getColumn(col);
        c.setMinWidth(0);
        c.setMaxWidth(0);
        c.setPreferredWidth(0);
        c.setWidth(0);
    }
 
    private boolean isPending(JTable table, int row) {
        Object v = table.getModel().getValueAt(row, 6);
        return v != null && PENDING.equalsIgnoreCase(v.toString());
    }
 
    // ── Renderers & Editors ───────────────────────────────────────────────────
 
    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setOpaque(true);
            l.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            String s = value == null ? "" : value.toString();
            if      (PENDING.equalsIgnoreCase(s))  l.setForeground(new Color(204, 153, 0));
            else if (ACCEPTED.equalsIgnoreCase(s)) l.setForeground(new Color(0, 153, 0));
            else if (DENIED.equalsIgnoreCase(s))   l.setForeground(new Color(204, 0, 0));
            else                                   l.setForeground(table.getForeground());
            l.setFont(l.getFont().deriveFont(Font.BOLD));
            return l;
        }
    }
 
    class ActionRenderer extends JPanel implements TableCellRenderer {
        private final JButton accept = new JButton("Accept");
        private final JButton deny   = new JButton("Deny");
        private final JLabel  done   = new JLabel("—");
 
        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 18));
            done.setForeground(Color.GRAY);
        }
 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            if (isPending(table, row)) { add(accept); add(deny); }
            else                        add(done);
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));
            doLayout();
            return this;
        }
    }
 
    class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel  panel  = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 18));
        private final JButton accept = new JButton("Accept");
        private final JButton deny   = new JButton("Deny");
        private final JLabel  done   = new JLabel("—");
 
        public ActionEditor(JCheckBox cb) {
            done.setForeground(Color.GRAY);
            accept.addActionListener(e -> handle(ACCEPTED));
            deny.addActionListener(e -> handle(DENIED));
        }
 
        private void handle(String status) {
            int row = view.getPetRequestsTable().getEditingRow();
            if (row == -1) row = view.getPetRequestsTable().getSelectedRow();
            if (row == -1) { fireEditingStopped(); return; }
 
            TableModel model = view.getPetRequestsTable().getModel();
            int recoverID, providerID;
            try {
                recoverID  = Integer.parseInt(model.getValueAt(row, 0).toString());
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
 
            String providerEmail = model.getValueAt(row, 3).toString();
            String providerName  = model.getValueAt(row, 2).toString()
                                       .replaceAll("<[^>]*>", "").trim();
 
            if (ACCEPTED.equals(status)) {
                dao.activateProvider(providerID);
                new Thread(() ->
                    utils.EmailService.sendUnfreezeAccepted(providerEmail, providerName)
                ).start();
            } else if (DENIED.equals(status)) {
                new Thread(() ->
                    utils.EmailService.sendUnfreezeDenied(providerEmail, providerName)
                ).start();
            }
 
            fireEditingStopped();
            loadRequests();
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.removeAll();
            if (isPending(table, row)) { panel.add(accept); panel.add(deny); }
            else                        panel.add(done);
            panel.setSize(table.getColumnModel().getColumn(column).getWidth(),
                          table.getRowHeight(row));
            panel.doLayout();
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
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }
 
    class HtmlRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            return label;
        }
    }
}
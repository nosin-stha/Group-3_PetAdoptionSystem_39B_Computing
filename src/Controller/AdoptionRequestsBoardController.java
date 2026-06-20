package Controller;
 
import DAO.AdoptionRequestsBoardDAO;
import model.AdoptionRequestData;
import model.PetsData;
import view.AdoptionRequestManagement_ProviderPage;
import view.PetRequestsBoard;
 
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
 
public class AdoptionRequestsBoardController {
 
    private AdoptionRequestManagement_ProviderPage view;
    private AdoptionRequestsBoardDAO dao;
    private int providerID;
 
    private static final int REASON_ROW_HEIGHT = 100;
 
    public AdoptionRequestsBoardController(
            AdoptionRequestManagement_ProviderPage view, int providerID) {
        this.view       = view;
        this.dao        = new AdoptionRequestsBoardDAO();
        this.providerID = providerID;
 
        loadAllRequests();
 
        // Pass 'this' so the filter can call populateScrollPanel() directly —
        // avoids duplicating PetRequestsBoard-building logic in the filter.
        new ProviderRequestsSearchFilter(view, providerID, this);
    }
 
    public void loadAllRequests() {
        ArrayList<PetsData> pets = dao.getPetsByProvider(providerID);
        if (pets == null) pets = new ArrayList<>();
 
        ArrayList<ArrayList<AdoptionRequestData>> allRequests = new ArrayList<>();
        for (PetsData pet : pets) {
            allRequests.add(dao.getRequestsByPet(pet.getPetID()));
        }
 
        populateScrollPanel(pets, allRequests);
    }
 
    
void populateScrollPanel(ArrayList<PetsData> pets,
                         ArrayList<ArrayList<AdoptionRequestData>> allRequests) {

    JPanel scrollPanel = view.getAllPetsRequests_ScrollPanel();
    scrollPanel.removeAll();

    if (pets.isEmpty()) {
        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel noData = new JLabel("No Requests Found", SwingConstants.CENTER);
        noData.setFont(new Font("Segoe UI", Font.BOLD, 18));
        noData.setForeground(new Color(180, 180, 180));
        scrollPanel.add(noData, BorderLayout.CENTER);

        scrollPanel.revalidate();
        scrollPanel.repaint();
        return;
    }

    scrollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));

    for (int i = 0; i < pets.size(); i++) {
        PetsData pet    = pets.get(i);
        ArrayList<AdoptionRequestData> reqs = allRequests.get(i);

        PetRequestsBoard board = new PetRequestsBoard();
        board.setPetName   (pet.getPetName());
        board.setPetType   (pet.getPetType());
        board.setPetGender (pet.getPetGender());
        board.setPetAge    (pet.getPetAge());
        board.setRequestCount(reqs.size());

        if (pet.getImagePath() != null && !pet.getImagePath().isEmpty()) {
            ImageIcon icon = new ImageIcon(pet.getImagePath());
            Image scaled   = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            board.setPetImage(new ImageIcon(scaled));
        }

        setupTable(board.getRequestsTable(), pet, reqs);

        // Size the table viewport to fit all rows without an inner scroll bar
        JTable t           = board.getRequestsTable();
        int    totalHeight = 0;
        for (int r = 0; r < t.getRowCount(); r++) totalHeight += t.getRowHeight(r);
        totalHeight += t.getTableHeader().getPreferredSize().height;
        t.setPreferredScrollableViewportSize(
            new Dimension(t.getPreferredSize().width, totalHeight));

        board.setPreferredSize(new Dimension(937, 428));
        board.setMaximumSize (new Dimension(937, 428));
        board.setMinimumSize (new Dimension(937, 428));
        board.setAlignmentX (Component.LEFT_ALIGNMENT);

        scrollPanel.add(board);
        scrollPanel.add(Box.createVerticalStrut(16));
    }

    scrollPanel.revalidate();
    scrollPanel.repaint();
}
 
    // ── table setup ──────────────────────────────────────────────────────────
 
    private void setupTable(JTable table, PetsData pet,
                            ArrayList<AdoptionRequestData> requests) {
 
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"adoptionID", "petID", "Adopter Name", "Email",
                         "Phone Number", "Reason for Adoption", "Status", "Action"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 7; }
            @Override public Class<?> getColumnClass(int c)       { return Object.class; }
        };
 
        for (AdoptionRequestData r : requests) {
            boolean actedOn = r.getAdoptionStatus().equalsIgnoreCase("Accepted")
                           || r.getAdoptionStatus().equalsIgnoreCase("Declined");
            model.addRow(new Object[]{
                r.getAdoptionID(), pet.getPetID(),
                r.getReqFullName(), r.getReqEmail(), r.getReqPhoneNo(),
                r.getReqReason(), r.getAdoptionStatus(),
                actedOn ? "done" : "pending"
            });
        }
 
        table.setModel(model);
        table.setRowHeight(REASON_ROW_HEIGHT);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(255, 153, 51));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
 
        hideColumn(table, 0);
        hideColumn(table, 1);
 
        table.getColumn("Adopter Name")        .setPreferredWidth(150);
        table.getColumn("Email")               .setPreferredWidth(180);
        table.getColumn("Phone Number")        .setPreferredWidth(120);
        table.getColumn("Reason for Adoption") .setPreferredWidth(160);
        table.getColumn("Status")              .setPreferredWidth(90);
        table.getColumn("Action")              .setPreferredWidth(190);
 
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setResizable(false);
 
        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor  (new ActionCellEditor(table));
 
        wrapReasonColumn(table);
    }
 
    private void wrapReasonColumn(JTable table) {
        table.getColumn("Reason for Adoption").setCellRenderer(
            (tbl, value, selected, focus, row, col) -> {
                JTextArea area = new JTextArea(value == null ? "" : value.toString());
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                area.setOpaque(true);
                area.setBackground(selected
                    ? tbl.getSelectionBackground() : tbl.getBackground());
                area.setForeground(selected
                    ? tbl.getSelectionForeground() : tbl.getForeground());
                return area;
            }
        );
    }
 
    private void hideColumn(JTable table, int col) {
        TableColumn tc = table.getColumnModel().getColumn(col);
        tc.setMinWidth(0);
        tc.setMaxWidth(0);
        tc.setWidth(0);
    }
 
    // ── accept / decline ─────────────────────────────────────────────────────
 
    private void handleAccept(JTable table, int row) {
        int adoptionID = (int) table.getModel().getValueAt(row, 0);
        int petID = (int) table.getModel().getValueAt(row, 1);
        String adopterEmail = table.getModel().getValueAt(row, 3).toString();
        String adopterName  = table.getModel().getValueAt(row, 2).toString();
 
        AdoptionRequestData req = dao.getRequestByID(adoptionID);
 
        int confirm = JOptionPane.showConfirmDialog(view,
            "Accept this adoption request?", "Confirm Accept",
            JOptionPane.YES_NO_OPTION);
 
        if (confirm != JOptionPane.YES_OPTION) return;
 
        if (dao.acceptRequest(adoptionID, petID)) {
            table.getModel().setValueAt("Accepted", row, 6);
            table.getModel().setValueAt("done",     row, 7);
 
            
            for (int i = 0; i < table.getModel().getRowCount(); i++) {
                if (i == row) continue;
                if (table.getModel().getValueAt(i, 6).toString()
                        .equalsIgnoreCase("Pending")) {
                    table.getModel().setValueAt("Declined", i, 6);
                    table.getModel().setValueAt("done",     i, 7);
                }
            }
            table.repaint();
 
            String petName   = req != null ? req.getPetName() : "";
            String petType   = req != null ? req.getPetType() : "";
            String petGender = req != null ? req.getPetGender() : "";
            String petAge    = req != null ? req.getPetAge() : "";
 
            new Thread(() -> {
                try {
                    utils.EmailService.sendAdoptionAccepted(
                        adopterEmail, adopterName,
                        petName, petType, petGender, petAge,
                        model.SessionData.shelterName);
                } catch (Exception ex) {
                    System.out.println("Email error (accept): " + ex.getMessage());
                }
            }).start();
 
            JOptionPane.showMessageDialog(view,
                "Request Accepted! Pet marked as Adopted.");
        } else {
            JOptionPane.showMessageDialog(view, "Failed. Try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void handleDecline(JTable table, int row) {
        int    adoptionID   = (int) table.getModel().getValueAt(row, 0);
        String adopterEmail = table.getModel().getValueAt(row, 3).toString();
        String adopterName  = table.getModel().getValueAt(row, 2).toString();
 
        AdoptionRequestData req = dao.getRequestByID(adoptionID);
 
        int confirm = JOptionPane.showConfirmDialog(view,
            "Decline this adoption request?", "Confirm Decline",
            JOptionPane.YES_NO_OPTION);
 
        if (confirm != JOptionPane.YES_OPTION) return;
 
        if (dao.declineRequest(adoptionID)) {
            table.getModel().setValueAt("Declined", row, 6);
            table.getModel().setValueAt("done",     row, 7);
            table.repaint();
 
            String petName   = req != null ? req.getPetName()   : "";
            String petType   = req != null ? req.getPetType()   : "";
            String petGender = req != null ? req.getPetGender() : "";
            String petAge    = req != null ? req.getPetAge()    : "";
 
            new Thread(() -> {
                try {
                    utils.EmailService.sendAdoptionDeclined(
                        adopterEmail, adopterName,
                        petName, petType, petGender, petAge,
                        model.SessionData.shelterName);
                } catch (Exception ex) {
                    System.out.println("Email error (decline): " + ex.getMessage());
                }
            }).start();
 
            JOptionPane.showMessageDialog(view,
                "Declined. Pet remains Available.");
        } else {
            JOptionPane.showMessageDialog(view, "Failed. Try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // ── renderer & editor ────────────────────────────────────────────────────
 
    private class ActionCellRenderer implements TableCellRenderer {
 
        private final JPanel  panel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private final JButton accept  = new JButton("Accept");
        private final JButton decline = new JButton("Decline");
        private final JLabel  done    = new JLabel();
 
        ActionCellRenderer() {
            styleAccept(accept);
            styleDecline(decline);
            done.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.setOpaque(true);
        }
 
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean selected, boolean focus, int row, int col) {
            panel.removeAll();
            panel.setBackground(
                selected ? table.getSelectionBackground() : table.getBackground());
 
            if ("done".equals(value)) {
                String status = table.getModel().getValueAt(row, 6).toString();
                done.setText(status);
                done.setForeground(status.equalsIgnoreCase("Accepted")
                    ? new Color(56, 142, 60) : new Color(198, 40, 40));
                panel.add(done);
            } else {
                panel.add(accept);
                panel.add(decline);
            }
            return panel;
        }
    }
 
    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
 
        private final JPanel  panel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private final JButton accept  = new JButton("Accept");
        private final JButton decline = new JButton("Decline");
        private final JLabel  done    = new JLabel();
        private JTable currentTable;
        private int    currentRow;
        private String currentValue;
 
        ActionCellEditor(JTable table) {
            styleAccept(accept);
            styleDecline(decline);
            done.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.setOpaque(true);
 
            accept.addActionListener(e -> {
                fireEditingStopped();
                handleAccept(currentTable, currentRow);
            });
            decline.addActionListener(e -> {
                fireEditingStopped();
                handleDecline(currentTable, currentRow);
            });
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean selected, int row, int col) {
            currentTable = table;
            currentRow   = row;
            currentValue = (value == null) ? "pending" : value.toString();
 
            panel.removeAll();
            panel.setBackground(table.getSelectionBackground());
 
            if ("done".equals(currentValue)) {
                String status = table.getModel().getValueAt(row, 6).toString();
                done.setText(status);
                done.setForeground(status.equalsIgnoreCase("Accepted")
                    ? new Color(56, 142, 60) : new Color(198, 40, 40));
                panel.add(done);
            } else {
                panel.add(accept);
                panel.add(decline);
            }
            return panel;
        }
 
        @Override
        public Object getCellEditorValue() { return currentValue; }
    }
 
    private void styleAccept(JButton btn) {
        btn.setBackground(new Color(76, 175, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
    }
 
    private void styleDecline(JButton btn) {
        btn.setBackground(new Color(244, 67, 54));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
    }
}
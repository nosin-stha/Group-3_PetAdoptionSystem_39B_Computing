/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */


import DAO.AdoptionRequestsBoardDAO;
import model.AdoptionRequestData;
import model.PetsData;
import view.AdoptionRequestManagement_ProviderPage;
import view.PetRequestsBoard;
 
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
 
public class AdoptionRequestsBoardController {
 
    private AdoptionRequestManagement_ProviderPage view;
    private AdoptionRequestsBoardDAO dao;
    private int providerID;
 

    private static final int REASON_ROW_HEIGHT = 100;
 
    public AdoptionRequestsBoardController(AdoptionRequestManagement_ProviderPage view, int providerID) {
        this.view = view;
        this.dao = new AdoptionRequestsBoardDAO();
        this.providerID = providerID;
        loadAllRequests();
    }
 
 
 
 
    public void loadAllRequests() {
        
        Map<PetsData, List<AdoptionRequestData>> grouped = dao.getRequestsGroupedByPet(providerID);
        populateScrollPanel(grouped);
    }
 
 
 
 
    private void populateScrollPanel(Map<PetsData, List<AdoptionRequestData>> grouped) {
        JPanel scrollPanel = view.getAllPetsRequests_ScrollPanel();
        scrollPanel.removeAll();
        scrollPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
 
        for (Map.Entry<PetsData, List<AdoptionRequestData>> entry : grouped.entrySet()) {
            PetsData pet = entry.getKey();
            List<AdoptionRequestData> reqs = entry.getValue();
 
            PetRequestsBoard board = new PetRequestsBoard();
            board.setPetName(pet.getPetName());
            board.setPetType(pet.getPetType());
            board.setPetGender(pet.getPetGender());
            board.setPetAge(pet.getPetAge());
            board.setRequestCount(reqs.size());
 
            if (pet.getImagePath() != null && !pet.getImagePath().isEmpty()) {
                ImageIcon icon = new ImageIcon(pet.getImagePath());
                Image scaled = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                board.setPetImage(new ImageIcon(scaled));
            }
 
            setupTable(board.getRequestsTable(), pet, reqs);
 

            JTable t = board.getRequestsTable();
            int totalHeight = 0;
            for (int i = 0; i < t.getRowCount(); i++) {
                totalHeight += t.getRowHeight(i);
            }
            totalHeight += t.getTableHeader().getPreferredSize().height;
            t.setPreferredScrollableViewportSize(
                new Dimension(t.getPreferredSize().width, totalHeight)
            );
 
            board.setPreferredSize(new Dimension(937, 428));
            board.setMaximumSize(new Dimension(937, 428));
            board.setMinimumSize(new Dimension(937, 428));
            board.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            scrollPanel.add(board);
            scrollPanel.add(Box.createVerticalStrut(16));
        }
 
        scrollPanel.revalidate();
        scrollPanel.repaint();
    }
 
 
 
 
    private void setupTable(JTable table, PetsData pet, List<AdoptionRequestData> requests) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"adoptionID", "petID", "Adopter Name", "Email", 
                "Phone Number", "Reason for Adoption", "Status", "Action"}, 0)
            {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return c == 7;
            }
                @Override
                public Class<?> getColumnClass(int c) {
                    return Object.class;
                }
            };
 
            for (AdoptionRequestData r : requests) {
                boolean actedOn = r.getAdoptionStatus().equalsIgnoreCase("Accepted")
                               || r.getAdoptionStatus().equalsIgnoreCase("Declined");
                model.addRow(new Object[]{
                    r.getAdoptionID(),
                    pet.getPetID(),
                    r.getReqFullName(),
                    r.getReqEmail(),
                    r.getReqPhoneNo(),
                    r.getReqReason(),
                    r.getAdoptionStatus(),
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
 
            table.getColumn("Adopter Name").setPreferredWidth(150);
            table.getColumn("Email").setPreferredWidth(180);
            table.getColumn("Phone Number").setPreferredWidth(120);
            table.getColumn("Reason for Adoption").setPreferredWidth(160);
            table.getColumn("Status").setPreferredWidth(90);
            table.getColumn("Action").setPreferredWidth(190);
 

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setResizable(false);
            }
 
            table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
            table.getColumn("Action").setCellEditor(new ActionCellEditor(table));
 
            wrapReasonColumn(table);
        }
 
 
 
 
    private void wrapReasonColumn(JTable table) {
        table.getColumn("Reason for Adoption").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean selected, boolean focus, int row, int col) {
                JTextArea area = new JTextArea(value == null ? "" : value.toString());
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                area.setOpaque(true);
                if (selected) {
                    area.setBackground(tbl.getSelectionBackground());
                    area.setForeground(tbl.getSelectionForeground());
                } else {
                    area.setBackground(tbl.getBackground());
                    area.setForeground(tbl.getForeground());
                }
                return area;
            }
        });
    }
 
 
 
 
    private void hideColumn(JTable table, int col) {
        TableColumn tc = table.getColumnModel().getColumn(col);
        tc.setMinWidth(0);
        tc.setMaxWidth(0);
        tc.setWidth(0);
    }
 
 
 
 
    private void handleAccept(JTable table, int row) {
        int adoptionID = (int) table.getModel().getValueAt(row, 0);
        int petID = (int) table.getModel().getValueAt(row, 1);
 
        int confirm = JOptionPane.showConfirmDialog(view,
                "Accept this adoption request?", "Confirm Accept", JOptionPane.YES_NO_OPTION);
 
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.acceptRequest(adoptionID, petID)) {
                table.getModel().setValueAt("Accepted", row, 6);
                table.getModel().setValueAt("done", row, 7);
 
                for (int i = 0; i < table.getModel().getRowCount(); i++) {
                    if (i == row) continue;
                    String status = table.getModel().getValueAt(i, 6).toString();
                    if (status.equalsIgnoreCase("Pending")) {
                        table.getModel().setValueAt("Declined", i, 6);
                        table.getModel().setValueAt("done", i, 7);
                    }
                }
 
                table.repaint();
                JOptionPane.showMessageDialog(view, "Request Accepted! Pet marked as Adopted.");
            } else {
                JOptionPane.showMessageDialog(view, "Failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
 
 
 
    private void handleDecline(JTable table, int row) {
        int adoptionID = (int) table.getModel().getValueAt(row, 0);
 
        int confirm = JOptionPane.showConfirmDialog(view,
                "Decline this adoption request?", "Confirm Decline", JOptionPane.YES_NO_OPTION);
 
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.declineRequest(adoptionID)) {
                table.getModel().setValueAt("Declined", row, 6);
                table.getModel().setValueAt("done", row, 7);
                table.repaint();
                JOptionPane.showMessageDialog(view, "Declined. Pet remains Available.");
            } else {
                JOptionPane.showMessageDialog(view, "Failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
 
 
 
    private class ActionCellRenderer implements TableCellRenderer {
 
        private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private JButton accept = new JButton("Accept");
        private JButton decline = new JButton("Decline");
        private JLabel done = new JLabel();
 
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
            panel.setBackground(selected ? table.getSelectionBackground() : table.getBackground());
 
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
 
        private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private JButton accept = new JButton("Accept");
        private JButton decline = new JButton("Decline");
        private JLabel done = new JLabel();
        private JTable currentTable;
        private int currentRow;
        private String currentValue;
 
        ActionCellEditor(JTable table) {
            styleAccept(accept);
            styleDecline(decline);
            done.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.setOpaque(true);
 
            accept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    handleAccept(currentTable, currentRow);
                }
            });
 
            decline.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    handleDecline(currentTable, currentRow);
                }
            });
        }
 
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean selected, int row, int col) {
            currentTable = table;
            currentRow = row;
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
        public Object getCellEditorValue() {
            return currentValue;
        }
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
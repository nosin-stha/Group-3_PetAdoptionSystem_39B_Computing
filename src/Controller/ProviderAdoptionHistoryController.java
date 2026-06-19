package Controller;

import DAO.ProviderAdoptionHistoryDAO;
import java.util.ArrayList;
import model.AdoptionRequestData;
import model.SessionData;
import view.AdoptionHistoryCard;
import view.ProviderAdoptionHistory;

public class ProviderAdoptionHistoryController {

    private ProviderAdoptionHistory view;
    private ProviderAdoptionHistoryDAO dao;

    public ProviderAdoptionHistoryController(ProviderAdoptionHistory view) {
        this.view = view;
        this.dao = new ProviderAdoptionHistoryDAO();
        view.getHistoryScrollPane().setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        loadAdoptionHistory();
        loadTotalRequestCount();
        new AdoptionHistorySearchFilter(view); 
    }

    private void loadAdoptionHistory() {
        ArrayList<AdoptionRequestData> list =
            dao.getProviderAdoptionHistory(SessionData.userID);

        javax.swing.JPanel panel = view.getSpnlAdoptionHistory();
        panel.removeAll();

        
        if (list.isEmpty()) {
            panel.setLayout(new java.awt.BorderLayout());
            javax.swing.JLabel noData = new javax.swing.JLabel(
                "No History", javax.swing.SwingConstants.CENTER);
            noData.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
            noData.setForeground(new java.awt.Color(180, 180, 180));
            panel.add(noData, java.awt.BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
            return;
        }

        // ── Cards layout (left-aligned) ──────────────────────────────────────
        panel.setLayout(new java.awt.GridBagLayout());

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 8, 10, 8);
        gbc.fill = java.awt.GridBagConstraints.NONE;
        gbc.anchor = java.awt.GridBagConstraints.NORTHWEST; // left-align cards

        int col = 0;
        int row = 0;

        for (int i = 0; i < list.size(); i++) {
            AdoptionRequestData data = list.get(i);
            AdoptionHistoryCard card = new AdoptionHistoryCard();

            card.setPreferredSize(new java.awt.Dimension(280, 320));
            card.getlbl_PetName().setText(data.getPetName());
            card.getlbl_PetBreed_fill().setText(data.getPetType());
            card.getlbl_PetGender_fill().setText(data.getPetGender());
            card.getlbl_PetAge_fill().setText(data.getPetAge());
            card.getlbl_AdopterName_fill().setText(data.getReqFullName());
            card.getlbl_AdopterPhoneNum_fill().setText(data.getReqPhoneNo());
            card.getlbl_AdopterAddress_fill().setText(data.getReqAddress());

            javax.swing.JLabel status = card.getlbl_Adopt_Status();
            status.setText("Adopted");
            status.setOpaque(true);
            status.setBackground(new java.awt.Color(0, 180, 0));
            status.setForeground(java.awt.Color.WHITE);
            status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            String imagePath = data.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    java.awt.Image img = new javax.swing.ImageIcon(imagePath)
                        .getImage()
                        .getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
                    card.getLblPetPicture().setIcon(new javax.swing.ImageIcon(img));
                    card.getLblPetPicture().setText("");
                } catch (Exception e) {
                    card.getLblPetPicture().setText("No Image");
                }
            }

            gbc.gridx = col;
            gbc.gridy = row;

            
            if (i == list.size() - 1) {
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
            }

            panel.add(card, gbc);

            col++;
            if (col == 3) {
                col = 0;
                row++;
                gbc.weightx = 0;
                gbc.weighty = 0;
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private void loadTotalRequestCount() {
        int count = dao.getTotalAdoptionRequests(SessionData.userID);
        view.getlblPetCount().setText(String.valueOf(count));
    }
}
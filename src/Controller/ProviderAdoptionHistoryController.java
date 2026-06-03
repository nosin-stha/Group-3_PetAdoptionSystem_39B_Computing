package Controller;

import DAO.ProviderAdoptionHistoryDAO;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
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
    }

    private void loadAdoptionHistory() {

    List<AdoptionRequestData> list =
            dao.getProviderAdoptionHistory(SessionData.userID);

    javax.swing.JPanel panel = view.getSpnlAdoptionHistory();
    panel.removeAll();

    java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
    panel.setLayout(layout);

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(10, 8, 10, 8);
    gbc.fill = java.awt.GridBagConstraints.NONE;

    int col = 0;
    int row = 0;

    for (AdoptionRequestData data : list) {

        AdoptionHistoryCard card = new AdoptionHistoryCard();

        card.setPreferredSize(new java.awt.Dimension(280, 320));

        card.getlbl_PetName().setText(data.getPetName());
        card.getlbl_PetBreed_fill().setText(data.getPetBreed());
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
                        .getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);

                card.getLblPetPicture().setIcon(new javax.swing.ImageIcon(img));
                card.getLblPetPicture().setText("");
            } catch (Exception e) {
                card.getLblPetPicture().setText("No Image");
            }
        }

        gbc.gridx = col;
        gbc.gridy = row;

        panel.add(card, gbc);

        col++;
        if (col == 3) {
            col = 0;
            row++;
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
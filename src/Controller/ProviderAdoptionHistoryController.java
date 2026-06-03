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
        loadAdoptionHistory();
        loadTotalRequestCount();
    }

    private void loadAdoptionHistory() {
        List<AdoptionRequestData> list = dao.getProviderAdoptionHistory(SessionData.userID);

        javax.swing.JPanel panel = view.getSpnlAdoptionHistory();
        panel.removeAll();
        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));

        if (list.isEmpty()) {
            panel.add(new javax.swing.JLabel("No adoption history found."));
        }

        for (AdoptionRequestData data : list) {
            AdoptionHistoryCard card = new AdoptionHistoryCard();

            card.getlbl_PetName().setText(data.getPetName());
            card.getlbl_PetBreed_fill().setText(data.getPetBreed());
            card.getlbl_PetGender_fill().setText(data.getPetGender());
            card.getlbl_PetAge_fill().setText(data.getPetAge());
            card.getlbl_AdopterName_fill().setText(data.getReqFullName());
            card.getlbl_AdopterPhoneNum_fill().setText(data.getReqPhoneNo());
            card.getlbl_AdopterAddress_fill().setText(data.getReqAddress());

            card.getlbl_Adopt_Status().setText("Adopted");
            card.getlbl_Adopt_Status().setOpaque(true);
            card.getlbl_Adopt_Status().setBackground(new java.awt.Color(0, 180, 0));
            card.getlbl_Adopt_Status().setForeground(java.awt.Color.WHITE);
            card.getlbl_Adopt_Status().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            card.getlbl_Adopt_Status().setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            card.getlbl_Adopt_Status().setBorder(
                javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8)
            );

            String imagePath = data.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imgURL = getClass().getResource(imagePath);
                if (imgURL != null) {
                    Image img = new ImageIcon(imgURL).getImage()
                        .getScaledInstance(95, 95, Image.SCALE_SMOOTH);
                    card.getLblPetPicture().setIcon(new ImageIcon(img));
                } else {
                    card.getLblPetPicture().setText("No Image");
                }
            }

            panel.add(card);
        }

        panel.revalidate();
        panel.repaint();
    }

    private void loadTotalRequestCount() {
        int count = dao.getTotalAdoptionRequests(SessionData.userID);
        view.getlblPetCount().setText(String.valueOf(count));
    }
}
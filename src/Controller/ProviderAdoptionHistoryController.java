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
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        if (list.isEmpty()) {
            javax.swing.JLabel empty = new javax.swing.JLabel("No adoption history found.");
            empty.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            panel.add(empty);
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
            card.getlbl_Adopt_Status().setText(data.getAdoptionStatus());

            String imagePath = data.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imgURL = getClass().getResource(imagePath);
                if (imgURL != null) {
                    Image scaled = new ImageIcon(imgURL).getImage().getScaledInstance(95, 95, Image.SCALE_SMOOTH);
                    card.getLblPetPicture().setIcon(new ImageIcon(scaled));
                } else {
                    card.getLblPetPicture().setText("No Image");
                }
            }

            panel.add(card);
            panel.add(javax.swing.Box.createVerticalStrut(10));
        }

        panel.revalidate();
        panel.repaint();
    }

    private void loadTotalRequestCount() {
        int count = dao.getTotalAdoptionRequests(SessionData.userID);
        System.out.println("Total adopted count: " + count);
        view.getlblPetCount().setText(String.valueOf(count));
    }
}
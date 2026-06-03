package Controller;

import DAO.AdoptionRequestTrackingDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import model.AdoptionRequestData;
import model.PetsData;
import model.SessionData;
import view.AdopterRequestCard;
import view.AdoptionRequestTrackingPage;

public class AdoptionRequestTrackingController {

    private final AdoptionRequestTrackingDAO dao = new AdoptionRequestTrackingDAO();
    private JPanel requestPanel;
    private AdoptionRequestTrackingPage view;

    // Constructor for full view setup
    public AdoptionRequestTrackingController(AdoptionRequestTrackingPage view) {
        this.view = view;
        this.requestPanel = view.getRequestContainerPanel();

        view.getAdopterAdoptionRequestsScroll()
            .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        view.getAdopterAdoptionRequestsScroll()
            .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        requestPanel.setLayout(new javax.swing.BoxLayout(
            requestPanel, javax.swing.BoxLayout.Y_AXIS));
        requestPanel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loadAdopterRequests();
        view.getAdopterTotalRequestsCount().setText(
            String.valueOf(getTotalRequests()));

        requestPanel.revalidate();
        requestPanel.repaint();
    }

    public AdoptionRequestTrackingController(JPanel requestPanel) {
        this.requestPanel = requestPanel;
    }

    public void loadAdopterRequests() {
        requestPanel.removeAll();

        List<AdoptionRequestData> requests =
            dao.getRequestsByAdopter(SessionData.userID);

        for (AdoptionRequestData req : requests) {
            AdopterRequestCard card = new AdopterRequestCard(req);

            // set size
            card.setPreferredSize(new java.awt.Dimension(905, 340));
            card.setMaximumSize(new java.awt.Dimension(905, 340));
            card.setMinimumSize(new java.awt.Dimension(905, 340));

            // set data
            card.getLblRequestCardPetName().setText(req.getPetName());
            card.getLblRequestCardPetBreed().setText(req.getPetBreed());
            card.getLblRequestCardPetAge().setText(req.getPetAge());
            card.getLblRequestCardPetGender().setText(req.getPetGender());
            card.getLblRequestStatus().setText(req.getAdoptionStatus());

            // set status color
            setStatusColor(card, req.getAdoptionStatus());

            // load image
            loadImageOnCard(card.getPetImgRequestCard(), req.getImagePath());

            // show/hide buttons based on status
            String status = req.getAdoptionStatus();
            if (status.equalsIgnoreCase("Pending")) {
                card.getBtnCancelRequestCard().setVisible(true);
                card.getBtnDeleteRequestCard().setVisible(false);
            } else if (status.equalsIgnoreCase("Declined")) {
                card.getBtnCancelRequestCard().setVisible(false);
                card.getBtnDeleteRequestCard().setVisible(true);
            } else if (status.equalsIgnoreCase("Accepted")) {
                card.getBtnCancelRequestCard().setVisible(false);
                card.getBtnDeleteRequestCard().setVisible(false);
            }

            // both cancel and delete use same listener — same job
            card.addCancelListener(new RemoveRequestListener(req.getAdoptionID()));
            card.addDeleteListener(new RemoveRequestListener(req.getAdoptionID()));
            
            PetsData pet = new PetsData();
            pet.setPetID(req.getPetID());
            new PetDetailsController(card, pet, "adopter", view);

            requestPanel.add(card);
            requestPanel.add(javax.swing.Box.createVerticalStrut(10));
        }

        requestPanel.revalidate();
        requestPanel.repaint();

        // update count if view is available
        if (view != null) {
            view.getAdopterTotalRequestsCount().setText(
                String.valueOf(getTotalRequests()));
        }
    }

    
    
    private void setStatusColor(AdopterRequestCard card, String status) {
        if (status == null) return;
        status = status.toLowerCase();
        card.getLblRequestStatus().setOpaque(true);
        if (status.equals("pending")) {
            card.getLblRequestStatus().setBackground(
                new java.awt.Color(255, 204, 0));
        } else if (status.equals("accepted")) {
            card.getLblRequestStatus().setBackground(
                new java.awt.Color(46, 204, 113));
        } else if (status.equals("declined") || status.equals("rejected")) {
            card.getLblRequestStatus().setBackground(
                new java.awt.Color(231, 76, 60));
        }
    }

    
    
    
    private void loadImageOnCard(javax.swing.JLabel imgLabel, String path) {
        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(path);
            java.awt.Image img = icon.getImage().getScaledInstance(
                120, 120, java.awt.Image.SCALE_SMOOTH);
            imgLabel.setIcon(new javax.swing.ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }

    
    
    public int getTotalRequests() {
        return dao.getTotalRequestsByAdopter(SessionData.userID);
    }


    
    
    class RemoveRequestListener implements ActionListener {
        private int adoptionID;
        public RemoveRequestListener(int adoptionID) {
            this.adoptionID = adoptionID;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to remove this request?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = dao.deleteRequest(adoptionID);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Request removed.");
                    loadAdopterRequests(); // refresh cards
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove request.");
                }
            }
        }
    }
}
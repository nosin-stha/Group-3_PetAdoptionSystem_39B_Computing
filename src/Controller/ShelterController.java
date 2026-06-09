package Controller;

import DAO.ProviderDetailsDAO;
import model.ProviderData;
import view.ShelterCard;
import view.ShelterListingDisplay;
import view.AdopterView_Shelter_Detail;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShelterController {

    private final ShelterListingDisplay shelterView;
    private final ProviderDetailsDAO providerDAO = new ProviderDetailsDAO();
    private JPanel shelterPanel;

    public ShelterController(ShelterListingDisplay shelterView) {
        this.shelterView = shelterView;
        new NavigationController(shelterView);

        JScrollPane scrollPane = shelterView.getShelterScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        shelterPanel = new JPanel(new GridLayout(0, 3, 25, 25));
        shelterPanel.setBackground(Color.WHITE);

        wrapperPanel.add(shelterPanel, BorderLayout.CENTER);
        scrollPane.setViewportView(wrapperPanel);

        loadProviderShelters();
    }

    public void loadProviderShelters() {
        shelterPanel.removeAll();

        ArrayList<ProviderData> shelters = providerDAO.getAllProviders();

        if (shelters != null && !shelters.isEmpty()) {
            for (ProviderData shelter : shelters) {
                ShelterCard card = buildShelterCard(shelter);
                shelterPanel.add(card);
            }
        } else {
            JLabel emptyLabel = new JLabel("No shelters available.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            shelterPanel.add(emptyLabel);
        }

        shelterPanel.revalidate();
        shelterPanel.repaint();
    }

    private ShelterCard buildShelterCard(final ProviderData shelter) {
        final ShelterCard card = new ShelterCard();

        card.setShelterName(shelter.getShelterName());
        loadImageOnCard(card.getShelterImageLabel(), shelter.getPfp());

        card.addViewMoreListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                AdopterView_Shelter_Detail detailFrame =
                        new AdopterView_Shelter_Detail(shelter, shelterView);
                detailFrame.setLocationRelativeTo(null);
                detailFrame.setVisible(true);
                shelterView.setVisible(false);
            }
        });

        ReportPetProviderController.attachToShelterCard(card, shelter.getProviderID());

        return card;
    }

    private void loadImageOnCard(JLabel imgLabel, String path) {
        try {
            if (path == null || path.trim().isEmpty()) return;
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }
}
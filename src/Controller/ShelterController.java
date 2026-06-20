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

        attachListeners();
        loadProviderShelters();
    }

    private void attachListeners() {
        shelterView.addSearchListener(e -> handleSearch());
        shelterView.addResetListener(e -> handleReset());
    }

    private void handleSearch() {
        String keyword = shelterView.getSearchText();
        if (keyword.isEmpty()) {
            loadProviderShelters();
        } else {
            ArrayList<ProviderData> results = providerDAO.searchProviders(keyword);
            renderShelters(results);
        }
    }
    
    private void handleReset() {
        shelterView.resetSearchBar();
        loadProviderShelters();
    }

    public void loadProviderShelters() {
        ArrayList<ProviderData> shelters = providerDAO.getAllProviders();
        renderShelters(shelters);
    }

    private void renderShelters(ArrayList<ProviderData> shelters) {
        shelterPanel.removeAll();
        if (shelters != null && !shelters.isEmpty()) {
            for (ProviderData shelter : shelters) {
                ShelterCard card = buildShelterCard(shelter);
                shelterPanel.add(card);
            }
        } else {
            JLabel emptyLabel = new JLabel("No shelters found.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            shelterPanel.add(emptyLabel);
        }
        shelterPanel.revalidate();
        shelterPanel.repaint();
    }

    private ShelterCard buildShelterCard(final ProviderData shelter) {
        final ShelterCard card = new ShelterCard();
        card.setShelterName(
            "<html><div style='text-align:center; width:120px;'>"
            + shelter.getShelterName()
            + "</div></html>"
        );

        loadImageOnCard(card.getShelterImageLabel(), shelter.getPfp());
        card.addViewMoreListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                AdopterView_Shelter_Detail detailFrame = new AdopterView_Shelter_Detail(shelter, shelterView);
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
            java.io.File f = new java.io.File(path);
            if (!f.exists()) return;
            Image img = new ImageIcon(path).getImage()
                        .getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
            imgLabel.setText("");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            System.out.println("Shelter image load error: " + e.getMessage());
        }
    }
}
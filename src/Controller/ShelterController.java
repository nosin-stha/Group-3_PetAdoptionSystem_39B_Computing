package Controller;

import DAO.ProviderDetailsDAO;
import model.ProviderData;
import view.ShelterCard;
import view.ShelterListingDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

        // Wrapper panel with padding
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Grid: 3 columns, 25px horizontal, 25px vertical gap
        shelterPanel = new JPanel(new GridLayout(0, 3, 25, 25));
        shelterPanel.setBackground(Color.WHITE);

        wrapperPanel.add(shelterPanel, BorderLayout.CENTER);
        scrollPane.setViewportView(wrapperPanel);

        loadProviderShelters();
    }


    public void loadProviderShelters() {
        if (shelterPanel == null) return;

        shelterPanel.removeAll();

        List<ProviderData> shelters = providerDAO.getAllProviders();

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

    // ─────────────────────────────────────────────
    // BUILD SHELTER CARD
    // ─────────────────────────────────────────────

    private ShelterCard buildShelterCard(ProviderData shelter) {
    ShelterCard card = new ShelterCard();

    card.setShelterName(shelter.getShelterName());
    loadImageOnCard(card.getShelterImageLabel(), shelter.getPfp());

    card.addViewMoreListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            // TODO: open ShelterDetailsDisplay and pass shelter data
            System.out.println("View more clicked: " + shelter.getShelterName());
        }
    });

    return card;
}

    private void loadImageOnCard(JLabel imgLabel, String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            java.awt.Image img = icon.getImage().getScaledInstance(
                120, 120, java.awt.Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }
}
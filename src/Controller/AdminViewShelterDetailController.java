package Controller;

import DAO.ShelterViewPageDAO;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.PetsData;
import model.ProviderData;
import view.Admin_ViewShelterDetail;
import view.SheltViewPetCard;

public class AdminViewShelterDetailController {

    private final Admin_ViewShelterDetail view;
    private final ProviderData provider;
    private final JFrame parentFrame;

    private final ShelterViewPageDAO shelterDAO = new ShelterViewPageDAO();

    private ArrayList<PetsData> cachedPets;

    private static final Color ACTIVE_TAB  = new Color(255, 255, 204);
    private static final Color DEFAULT_TAB = new Color(255, 204, 102);

    public AdminViewShelterDetailController(Admin_ViewShelterDetail view,
                                            ProviderData basicProvider,
                                            JFrame parentFrame) {
        this.view        = view;
        this.parentFrame = parentFrame;

        // Fetch full provider data from DB using the providerID from the reported card
        ProviderData full = shelterDAO.getProviderByID(basicProvider.getProviderID());
        this.provider = (full != null) ? full : basicProvider;

        setMainPanelBackground();
        loadShelterInfo();
        loadShelterImage();
        cachePets();
        initListeners();
        setActiveTab("ABOUT");
        loadAbout();
    }

    // ── set jPanel1 (the root panel) to white ──
    private void setMainPanelBackground() {
        view.getMainPanel().setBackground(Color.WHITE);
    }

    private void loadShelterInfo() {
        view.setShelterName(provider.getShelterName());
        view.setPhone(provider.getPhoneNumber());
        view.setEmail(provider.getEmail());
        view.setAddress(provider.getAddress());
    }

    private void loadShelterImage() {
        String path = provider.getPfp();
        if (path == null || path.trim().isEmpty()) return;
        if (!new File(path).exists()) return;
        Image scaled = new ImageIcon(path).getImage()
                           .getScaledInstance(110, 100, Image.SCALE_SMOOTH);
        view.setShelterImage(new ImageIcon(scaled));
    }

    private void cachePets() {
        ArrayList<PetsData> list = shelterDAO.getPetsByProvider(provider.getProviderID());
        cachedPets = (list == null) ? new ArrayList<>() : list;
    }

    private void initListeners() {
        view.getAboutButton().addActionListener(e -> { setActiveTab("ABOUT");     loadAbout(); });
        view.getAvailableButton().addActionListener(e -> { setActiveTab("AVAILABLE"); loadPets("available"); });
        view.getAdoptedButton().addActionListener(e -> { setActiveTab("ADOPTED");   loadPets("adopted"); });

        view.getExitButton().addActionListener(e -> {
            view.dispose();
            if (parentFrame != null) {
                parentFrame.toFront();
                parentFrame.requestFocus();
            }
        });
    }

    private void setActiveTab(String tab) {
        view.getAboutButton().setBackground(DEFAULT_TAB);
        view.getAvailableButton().setBackground(DEFAULT_TAB);
        view.getAdoptedButton().setBackground(DEFAULT_TAB);
        if ("ABOUT".equals(tab))     view.getAboutButton().setBackground(ACTIVE_TAB);
        if ("AVAILABLE".equals(tab)) view.getAvailableButton().setBackground(ACTIVE_TAB);
        if ("ADOPTED".equals(tab))   view.getAdoptedButton().setBackground(ACTIVE_TAB);
    }

    private void loadAbout() {
        JPanel panel = view.getContentPanel();
        panel.removeAll();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        JLabel missionTitle = new JLabel("Mission Statement");
        missionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        missionTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel missionText = new JLabel(
            "<html><div style='width:460px'>"
            + safe(provider.getMissionStatement()).replace("\n", "<br>")
            + "</div></html>");
        missionText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        missionText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel gap = new JPanel();
        gap.setOpaque(false);
        gap.setPreferredSize(new java.awt.Dimension(0, 15));
        gap.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 15));

        JLabel policyTitle = new JLabel("Adoption Policy");
        policyTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        policyTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 5, 15));

        JLabel policyText = new JLabel(
            "<html><div style='width:460px'>"
            + safe(provider.getAdoptionPolicy()).replace("\n", "<br>")
            + "</div></html>");
        policyText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        policyText.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 15, 15));

        panel.add(missionTitle);
        panel.add(missionText);
        panel.add(gap);
        panel.add(policyTitle);
        panel.add(policyText);

        view.getScrollPane().setHorizontalScrollBarPolicy(
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.getScrollPane().setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.revalidate();
        panel.repaint();
    }

    private void loadPets(String status) {
        JPanel panel = view.getContentPanel();
        panel.removeAll();
        panel.setLayout(new GridBagLayout());

        view.getScrollPane().setHorizontalScrollBarPolicy(
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        view.getScrollPane().setVerticalScrollBarPolicy(
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets  = new Insets(5, 5, 5, 5);

        int row = 0;
        for (PetsData pet : cachedPets) {
            if (!status.equalsIgnoreCase(pet.getPetAdoptionStatus())) continue;
            SheltViewPetCard card = new SheltViewPetCard();
            populateCard(card, pet);
            gbc.gridy = row;
            panel.add(card, gbc);
            row++;
        }

        // filler to push cards to top
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridx   = 0;
        filler.gridy   = row;
        filler.weighty = 1.0;
        filler.fill    = GridBagConstraints.VERTICAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        panel.add(spacer, filler);

        panel.revalidate();
        panel.repaint();
    }

    private void populateCard(SheltViewPetCard card, PetsData pet) {
        card.getPetNameLabel().setText(safe(pet.getPetName()));
        card.getBreedLabel().setText(safe(pet.getPetType()));
        card.getGenderLabel().setText(safe(pet.getPetGender()));
        card.getAgeLabel().setText(safe(pet.getPetAge()));

        // Disable the heart/favourite button — not applicable in admin view
        card.getFavButton().setEnabled(false);
        card.getFavButton().setVisible(false);

        String imgPath = pet.getImagePath();
        if (imgPath != null && !imgPath.trim().isEmpty() && new File(imgPath).exists()) {
            Image scaled = new ImageIcon(imgPath).getImage()
                               .getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            card.getPetImgLabel().setIcon(new ImageIcon(scaled));
            card.getPetImgLabel().setText("");
        }

        card.addViewMoreListener(e ->
            new PetDetailsController(pet, "admin", view).actionPerformed(e));
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
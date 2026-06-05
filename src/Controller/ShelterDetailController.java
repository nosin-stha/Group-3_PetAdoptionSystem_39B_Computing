package Controller;

import DAO.ShelterViewPageDAO;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.ProviderData;
import model.PetsData;
import view.AdopterView_Shelter_Detail;
import view.PetCardPanel;

public class ShelterDetailController {

    private final AdopterView_Shelter_Detail view;
    private final ProviderData provider;
    private final JFrame parentFrame;
    private final ShelterViewPageDAO shelterDAO = new ShelterViewPageDAO();

    private JPanel aboutPanel;
    private JPanel availablePetsPanel;
    private JPanel adoptedPetsPanel;
    private JPanel contentPanel;

    private JLabel missionStatementLabel;
    private JLabel adoptionPolicyLabel;

    private ArrayList<PetsData> cachedPets;

    public ShelterDetailController(AdopterView_Shelter_Detail view,
                                   ProviderData provider,
                                   JFrame parentFrame) {
        this.view = view;
        this.provider = provider;
        this.parentFrame = parentFrame;

        setupUI();
        loadShelterInfo();
        loadShelterImage();
        cachePets();
        initListeners();
    }

    private void setupUI() {
        JPanel rightPanel = view.getRightContentPanel();
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());

        contentPanel = new JPanel(new CardLayout());

        aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel missionTitle = new JLabel("Mission Statement");
        missionTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        missionStatementLabel = new JLabel();
        missionStatementLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        JLabel policyTitle = new JLabel("Adoption Policy");
        policyTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));

        adoptionPolicyLabel = new JLabel();
        adoptionPolicyLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        aboutPanel.add(missionTitle);
        aboutPanel.add(Box.createVerticalStrut(10));
        aboutPanel.add(missionStatementLabel);
        aboutPanel.add(Box.createVerticalStrut(25));
        aboutPanel.add(policyTitle);
        aboutPanel.add(Box.createVerticalStrut(10));
        aboutPanel.add(adoptionPolicyLabel);

        availablePetsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        adoptedPetsPanel   = new JPanel(new GridLayout(0, 1, 10, 10));

        contentPanel.add(aboutPanel,                        "ABOUT");
        contentPanel.add(new JScrollPane(availablePetsPanel), "AVAILABLE");
        contentPanel.add(new JScrollPane(adoptedPetsPanel),   "ADOPTED");

        rightPanel.add(view.getTopSmallPanel(), BorderLayout.NORTH);
        rightPanel.add(contentPanel,            BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void cachePets() {
        ArrayList<PetsData> list = shelterDAO.getPetsByProvider(provider.getProviderID());
        cachedPets = (list == null) ? new ArrayList<>() : new ArrayList<>(list);
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadShelterInfo() {
        view.setShelterName(provider.getShelterName());
        view.setPhone(provider.getPhoneNumber());
        view.setEmail(provider.getEmail());
        view.setAddress(provider.getAddress());

        missionStatementLabel.setText("<html>" + safe(provider.getMissionStatement()) + "</html>");
        adoptionPolicyLabel.setText("<html>" + safe(provider.getAdoptionPolicy()) + "</html>");
    }

    private String safe(String text) {
        return (text == null) ? "" : text;
    }

    private void loadShelterImage() {
        String imagePath = provider.getPfp();
        if (imagePath == null || imagePath.trim().isEmpty()) return;

        File file = new File(imagePath);
        if (!file.exists()) return;

        ImageIcon icon   = new ImageIcon(imagePath);
        Image    scaled  = icon.getImage().getScaledInstance(110, 100, Image.SCALE_SMOOTH);
        view.setShelterImage(new ImageIcon(scaled));
    }

    private void loadPetImage(PetCardPanel card, String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) return;
        try {
            File file = new File(imagePath);
            if (!file.exists()) return;

            ImageIcon icon  = new ImageIcon(imagePath);
            Image    scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            card.getPetImg().setIcon(new ImageIcon(scaled));
            card.getPetImg().setText("");
        } catch (Exception ex) {
            System.out.println("Pet image load error: " + ex.getMessage());
        }
    }

    private void initListeners() {

        // --- Shelter-specific navigation only ---
        view.getAboutButton().addActionListener(e -> showCard("ABOUT"));

        view.getAvailableButton().addActionListener(e -> {
            loadAvailablePets();
            showCard("AVAILABLE");
        });

        view.getAdoptedButton().addActionListener(e -> {
            loadAdoptedPets();
            showCard("ADOPTED");
        });

        view.getExitButton().addActionListener(e -> {
            view.dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true);
                parentFrame.toFront();
            }
        });

        // Home and Logout are handled by NavigationController
    }

    private void loadAvailablePets() {
        availablePetsPanel.removeAll();

        for (PetsData pet : cachedPets) {
            if ("available".equalsIgnoreCase(pet.getPetAdoptionStatus())) {
                PetCardPanel card = new PetCardPanel(pet);
                card.hideActionButtons();
                loadPetImage(card, pet.getImagePath());
                new PetDetailsController(card, pet, "adopter", view);
                availablePetsPanel.add(card);
            }
        }

        availablePetsPanel.revalidate();
        availablePetsPanel.repaint();
    }

    private void loadAdoptedPets() {
        adoptedPetsPanel.removeAll();

        for (PetsData pet : cachedPets) {
            if ("adopted".equalsIgnoreCase(pet.getPetAdoptionStatus())) {
                PetCardPanel card = new PetCardPanel(pet);
                card.hideActionButtons();
                loadPetImage(card, pet.getImagePath());
                new PetDetailsController(card, pet, "adopter", view);
                adoptedPetsPanel.add(card);
            }
        }

        adoptedPetsPanel.revalidate();
        adoptedPetsPanel.repaint();
    }
}
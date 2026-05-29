package Controller;

import DAO.AdopterHomePageDao;
import DAO.ProviderHomePageDao;
import DAO.PetDAO;

import java.awt.Cursor;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.PetsData;
import model.SessionData;

import view.PetCardPanel;
import view.Add_Update_Pet;
import view.AdopterHomePage;
import view.ProviderHomePage;

public class PetController {
    private AdopterHomePage adopterHomeView; 
    private final AdopterHomePageDao adopterDAO = new AdopterHomePageDao();
    private final ProviderHomePageDao providerDAO = new ProviderHomePageDao();
    private final PetDAO petDAO = new PetDAO();

    private JPanel adopterPanel;
    private JPanel providerPanel;
    private JLabel adopterCountLabel;
    private JLabel providerCountLabel;

    private Add_Update_Pet addUpdateView;
    private ProviderHomePage providerHomeView;

    private boolean isUpdateMode = false;
    private int updatePetID = 0;

    private PetController parentController;
    private String existingImagePath = null;

    // ─────────────────────────────────────────────
    // CONSTRUCTORS
    // ─────────────────────────────────────────────

    // ADOPTER HOME PAGE
    public PetController(AdopterHomePage adopterHomeView) {
        this.adopterHomeView = adopterHomeView;
        this.adopterPanel = adopterHomeView.getPetContainerPanel();
        this.adopterCountLabel = adopterHomeView.getAdopterTotalPetCountLabel();

        // scroll setup
        adopterHomeView.getAdopterAllPetScrollPane()
            .setHorizontalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        adopterHomeView.getAdopterAllPetScrollPane()
            .setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        adopterPanel.setLayout(new java.awt.GridLayout(0, 3, 20, 20));
        adopterHomeView.getAdopterAllPetScrollPane().setViewportView(adopterPanel);

        loadAdopterPets();
    }

    // PROVIDER HOME PAGE
    // PROVIDER HOME PAGE constructor — replace logout line
public PetController(ProviderHomePage providerHomeView) {
    this.providerHomeView = providerHomeView;
    this.providerPanel = providerHomeView.getProviderPetContainerPanel();
    this.providerCountLabel = providerHomeView.getTotalPetCountLabel();

    // scroll setup
    providerHomeView.getProviderPetsScrollPane()
        .setHorizontalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    providerHomeView.getProviderPetsScrollPane()
        .setVerticalScrollBarPolicy(
            javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    providerPanel.setLayout(new java.awt.GridLayout(0, 3, 20, 20));
    providerHomeView.getProviderPetsScrollPane().setViewportView(providerPanel);

    // register listeners
    providerHomeView.addPetButtonListener(new AddPetButtonListener());

    // reuse NavigationController's logout instead of separate LogoutListener
    providerHomeView.addLogoutListener(
        new NavigationController(providerHomeView).new LogoutListener()
    );

    loadProviderPets();
}

    // ADD FORM
    public PetController(Add_Update_Pet addUpdateView, ProviderHomePage providerHomeView, PetController parentController) {
        this.addUpdateView = addUpdateView;
        this.providerHomeView = providerHomeView;
        this.isUpdateMode = false;
        this.parentController = parentController;

        setupForm();
        addUpdateView.addSavePetListener(new SavePetListener());
        addUpdateView.addImageUploadListener(new ImageUploadListener());
    }

    // ADD FORM (backwards compatibility)
    public PetController(Add_Update_Pet addUpdateView, ProviderHomePage providerHomeView) {
        this(addUpdateView, providerHomeView, null);
    }

    // UPDATE FORM
    public PetController(Add_Update_Pet addUpdateView, PetsData pet, PetController parentController) {
        this.addUpdateView = addUpdateView;
        this.isUpdateMode = true;
        this.updatePetID = pet.getPetID();
        this.existingImagePath = pet.getImagePath();
        this.parentController = parentController;

        setupForm();
        addUpdateView.addSavePetListener(new SavePetListener());
        addUpdateView.addImageUploadListener(new ImageUploadListener());
    }

    // UPDATE FORM (backwards compatibility)
    public PetController(Add_Update_Pet addUpdateView, PetsData pet) {
        this(addUpdateView, pet, null);
    }

    // ─────────────────────────────────────────────
    // SETUP
    // ─────────────────────────────────────────────

    private void setupForm() {
        if (addUpdateView != null) {
            addUpdateView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────

    private String getRadio(JRadioButton yes, JRadioButton no) {
        return yes.isSelected() ? "Yes" : "No";
    }

    private void setRadio(JRadioButton yes, JRadioButton no, String value) {
        if ("Yes".equalsIgnoreCase(value)) {
            yes.setSelected(true);
        } else {
            no.setSelected(true);
        }
    }

    private boolean validate() {
        if (addUpdateView.getTxtPetName().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pet Name required!");
            return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────
    // SCROLL PANE REFRESH
    // ─────────────────────────────────────────────

    private void revalidateScrollPane(JPanel panel) {
        Container parent = panel.getParent();
        while (parent != null) {
            if (parent instanceof JScrollPane) {
                parent.revalidate();
                parent.repaint();
                break;
            }
            parent = parent.getParent();
        }
    }

    // ─────────────────────────────────────────────
    // REFRESH
    // ─────────────────────────────────────────────

    public void refresh() {
        if (parentController != null) {
            parentController.refresh();
            return;
        }
        if (providerPanel != null) loadProviderPets();
        if (adopterPanel != null) loadAdopterPets();
    }

    // ─────────────────────────────────────────────
    // LOAD PROVIDER PETS
    // ─────────────────────────────────────────────

    public void loadProviderPets() {
        if (providerPanel == null) return;

        providerPanel.removeAll();

        List<PetsData> pets = providerDAO.getPetsByProvider(SessionData.userID);

        if (pets != null) {
            for (PetsData pet : pets) {
                PetCardPanel card = new PetCardPanel(pet);

                card.setPreferredSize(new java.awt.Dimension(250, 355));
                card.setMinimumSize(new java.awt.Dimension(250, 355));
                card.setMaximumSize(new java.awt.Dimension(250, 355));

                card.getPetName().setText(pet.getPetName());
                card.getPetType().setText(pet.getPetType());
                card.getPetAge().setText(pet.getPetAge());
                card.getPetGender().setText(pet.getPetGender());
                loadImageOnCard(card.getPetImg(), pet.getImagePath());

                card.addUpdateListener(new UpdatePetListener(pet));
                card.addDeleteListener(new DeletePetListener(pet.getPetID()));
                new PetDetailsController(card, pet, "provider", providerHomeView);
                providerPanel.add(card);
            }
        }

        providerPanel.revalidate();
        providerPanel.repaint();
        revalidateScrollPane(providerPanel);

        if (providerCountLabel != null && pets != null) {
            providerCountLabel.setText(String.valueOf(pets.size()));
        }
    }

    // ─────────────────────────────────────────────
    // LOAD ADOPTER PETS
    // ─────────────────────────────────────────────

    public void loadAdopterPets() {
        if (adopterPanel == null) return;

        adopterPanel.removeAll();

        List<PetsData> pets = adopterDAO.getAvailablePets();

        if (pets != null) {
            for (PetsData pet : pets) {
                PetCardPanel card = new PetCardPanel(pet);

                card.setPreferredSize(new java.awt.Dimension(250, 355));
                card.setMinimumSize(new java.awt.Dimension(250, 355));
                card.setMaximumSize(new java.awt.Dimension(250, 355));

                card.getPetName().setText(pet.getPetName());
                card.getPetType().setText(pet.getPetType());
                card.getPetAge().setText(pet.getPetAge());
                card.getPetGender().setText(pet.getPetGender());
                loadImageOnCard(card.getPetImg(), pet.getImagePath());

                card.hideActionButtons();
                new PetDetailsController(card, pet, "adopter", adopterHomeView);
                adopterPanel.add(card);
            }
        }

        adopterPanel.revalidate();
        adopterPanel.repaint();
        revalidateScrollPane(adopterPanel);

        if (adopterCountLabel != null && pets != null) {
            adopterCountLabel.setText(String.valueOf(pets.size()));
        }
    }

    // ─────────────────────────────────────────────
    // IMAGE HELPER
    // ─────────────────────────────────────────────

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

    // ─────────────────────────────────────────────
    // INNER CLASS: LOGOUT
    // ─────────────────────────────────────────────

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                providerHomeView,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                new SessionController().logout(providerHomeView);
            }
        }
    }

    // ─────────────────────────────────────────────
    // INNER CLASS: IMAGE UPLOAD
    // ─────────────────────────────────────────────

    class ImageUploadListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Pet Image");
            chooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "bmp"
            ));

            int result = chooser.showOpenDialog(addUpdateView);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                addUpdateView.setPetImage(selectedFile.getAbsolutePath());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            addUpdateView.getPetImgLabel().setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            addUpdateView.getPetImgLabel().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    // ─────────────────────────────────────────────
    // INNER CLASS: SAVE
    // ─────────────────────────────────────────────

    class SavePetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validate()) return;

            PetsData pet = new PetsData();

            pet.setProviderID(SessionData.userID);
            pet.setPetName(addUpdateView.getTxtPetName().getText());
            pet.setPetType(addUpdateView.getCbPetType().getSelectedItem().toString());
            pet.setPetGender(addUpdateView.getCbPetGender().getSelectedItem().toString());
            pet.setPetAge(addUpdateView.getCbPetAge().getSelectedItem().toString());

            pet.setHouseTrained(getRadio(addUpdateView.getRbYesHouseTrained(), addUpdateView.getRbNoHouseTrained()));
            pet.setSpayed(getRadio(addUpdateView.getRbYesSpayed(), addUpdateView.getRbNoSpayed()));
            pet.setVaccinated(getRadio(addUpdateView.getRbYesVaccinated(), addUpdateView.getRbNoVaccinated()));
            pet.setSpecialNeeds(getRadio(addUpdateView.getRbYesSpecialNeeds(), addUpdateView.getRbNoSpecialNeeds()));

            if (isUpdateMode) {
                String currentPath = addUpdateView.getCurrentImagePath();
                pet.setImagePath(currentPath != null ? currentPath : existingImagePath);
            } else {
                pet.setImagePath(addUpdateView.getCurrentImagePath());
            }

            boolean success;

            if (isUpdateMode) {
                pet.setPetID(updatePetID);
                success = petDAO.updatePet(pet);
            } else {
                success = petDAO.addPet(pet);
            }

            if (success) {
                JOptionPane.showMessageDialog(null, isUpdateMode ? "Updated!" : "Added!");
                addUpdateView.dispose();

                if (providerHomeView != null) {
                    providerHomeView.setVisible(true);
                    providerHomeView.toFront();
                }

                refresh();
            } else {
                JOptionPane.showMessageDialog(null, "Failed!");
            }
        }
    }

    // ─────────────────────────────────────────────
    // INNER CLASS: UPDATE CLICK
    // ─────────────────────────────────────────────

    class UpdatePetListener implements ActionListener {

        private PetsData pet;

        public UpdatePetListener(PetsData pet) {
            this.pet = pet;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Add_Update_Pet form = new Add_Update_Pet();

            new PetController(form, pet, PetController.this);

            form.setLocationRelativeTo(providerHomeView);
            form.getTxtPetName().setText(pet.getPetName());
            form.getCbPetType().setSelectedItem(pet.getPetType());
            form.getCbPetGender().setSelectedItem(pet.getPetGender());
            form.getCbPetAge().setSelectedItem(pet.getPetAge());

            setRadio(form.getRbYesHouseTrained(), form.getRbNoHouseTrained(), pet.getHouseTrained());
            setRadio(form.getRbYesSpayed(), form.getRbNoSpayed(), pet.getSpayed());
            setRadio(form.getRbYesVaccinated(), form.getRbNoVaccinated(), pet.getVaccinated());
            setRadio(form.getRbYesSpecialNeeds(), form.getRbNoSpecialNeeds(), pet.getSpecialNeeds());

            form.setPetImage(pet.getImagePath());
            form.setVisible(true);
        }
    }

    // ─────────────────────────────────────────────
    // INNER CLASS: DELETE
    // ─────────────────────────────────────────────

    class DeletePetListener implements ActionListener {

        private int petID;

        public DeletePetListener(int petID) {
            this.petID = petID;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(null,
                    "Delete?", "Confirm",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

            if (petDAO.deletePet(petID)) {
                JOptionPane.showMessageDialog(null, "Deleted");
                refresh();
            } else {
                JOptionPane.showMessageDialog(null, "Failed");
            }
        }
    }

    // ─────────────────────────────────────────────
    // INNER CLASS: ADD BUTTON
    // ─────────────────────────────────────────────

    class AddPetButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Add_Update_Pet form = new Add_Update_Pet();
            new PetController(form, providerHomeView, PetController.this);
            form.setLocationRelativeTo(providerHomeView);
            form.setVisible(true);
        }
    }
}
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
import java.util.ArrayList;

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
    final PetDAO petDAO = new PetDAO();  
    private JPanel adopterPanel;
    private JPanel providerPanel;
    private JLabel adopterCountLabel;
    private JLabel providerCountLabel;

    private Add_Update_Pet addUpdateView;
    ProviderHomePage providerHomeView;

    private boolean isUpdateMode = false;
    private int updatePetID = 0;

    PetController parentController;
    private String existingImagePath = null;

    // ─── Adopter home constructor ─────────────────────────────────────────────
    public PetController(AdopterHomePage adopterHomeView) {
        this.adopterHomeView   = adopterHomeView;
        this.adopterPanel      = adopterHomeView.getPetContainerPanel();
        this.adopterCountLabel = adopterHomeView.getAdopterTotalPetCountLabel();

        adopterHomeView.getAdopterAllPetScrollPane()
            .setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        adopterHomeView.getAdopterAllPetScrollPane()
            .setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        adopterPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
        adopterHomeView.getAdopterAllPetScrollPane().setViewportView(adopterPanel);

      
        new AdopterHomeSearchFilter(adopterHomeView, this);
    }

    // ─── Provider home constructor ────────────────────────────────────────────
    public PetController(ProviderHomePage providerHomeView) {
        this.providerHomeView  = providerHomeView;
        this.providerPanel     = providerHomeView.getProviderPetContainerPanel();
        this.providerCountLabel = providerHomeView.getTotalPetCountLabel();

        providerHomeView.getProviderPetsScrollPane()
            .setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        providerHomeView.getProviderPetsScrollPane()
            .setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        providerPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
        providerHomeView.getProviderPetsScrollPane().setViewportView(providerPanel);

        providerHomeView.addPetButtonListener(new AddPetButtonListener());

       
        new ProviderHomeSearchFilter(providerHomeView, this);
    }

    // ─── Add pet form constructor ─────────────────────────────────────────────
    public PetController(Add_Update_Pet addUpdateView, ProviderHomePage providerHomeView,
                         PetController parentController) {
        this.addUpdateView    = addUpdateView;
        this.providerHomeView = providerHomeView;
        this.isUpdateMode     = false;
        this.parentController = parentController;

        setupForm();
        addUpdateView.addSavePetListener(new SavePetListener());
        addUpdateView.addImageUploadListener(new ImageUploadListener());
    }

    public PetController(Add_Update_Pet addUpdateView, ProviderHomePage providerHomeView) {
        this(addUpdateView, providerHomeView, null);
    }

    // ─── Update pet form constructor ──────────────────────────────────────────
    public PetController(Add_Update_Pet addUpdateView, PetsData pet,
                         PetController parentController) {
        this.addUpdateView    = addUpdateView;
        this.isUpdateMode     = true;
        this.updatePetID      = pet.getPetID();
        this.existingImagePath = pet.getImagePath();
        this.parentController = parentController;

        setupForm();
        addUpdateView.addSavePetListener(new SavePetListener());
        addUpdateView.addImageUploadListener(new ImageUploadListener());
    }

    public PetController(Add_Update_Pet addUpdateView, PetsData pet) {
        this(addUpdateView, pet, null);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private void setupForm() {
        if (addUpdateView != null) {
            addUpdateView.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    private String getRadio(JRadioButton yes, JRadioButton no) {
        return yes.isSelected() ? "Yes" : "No";
    }

    private boolean validate() {
        if (addUpdateView.getTxtPetName().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pet Name required!");
            return false;
        }
        return true;
    }

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

    // ─── Refresh ──────────────────────────────────────────────────────────────
    public void refresh() {
        if (parentController != null) {
            parentController.refresh();
            return;
        }
        if (providerHomeView != null) {
            new ProviderHomeSearchFilter(providerHomeView, this);
        }
        if (adopterHomeView != null) {
            new AdopterHomeSearchFilter(adopterHomeView, this);
        }
    }

    public void loadProviderPets() {
        if (providerPanel == null) return;

        providerPanel.removeAll();
        providerPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
        providerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ArrayList<PetsData> pets = providerDAO.getPetsByProvider(SessionData.userID);

        if (pets != null) {
            for (PetsData pet : pets) {
                providerPanel.add(buildProviderCard(pet));
            }
        }

        providerPanel.revalidate();
        providerPanel.repaint();
        revalidateScrollPane(providerPanel);

        if (providerCountLabel != null && pets != null) {
            providerCountLabel.setText(String.valueOf(pets.size()));
        }
    }

    // ─── Build provider card (reused by ProviderHomeSearchFilter) ─────────────
    public PetCardPanel buildProviderCard(PetsData pet) {
        PetCardPanel card = new PetCardPanel(pet);

        card.setPreferredSize(new java.awt.Dimension(250, 355));
        card.setMinimumSize  (new java.awt.Dimension(250, 355));
        card.setMaximumSize  (new java.awt.Dimension(250, 355));

        card.hideFavButton();
        card.getPetName()  .setText(pet.getPetName());
        card.getPetType()  .setText(pet.getPetType());
        card.getPetAge()   .setText(pet.getPetAge());
        card.getPetGender().setText(pet.getPetGender());
        loadImageOnCard(card.getPetImg(), pet.getImagePath());

        card.addUpdateListener(new UpdatePetListener(pet, this));
        card.addDeleteListener(new DeletePetListener(pet.getPetID(), this));
        new PetDetailsController(card, pet, "provider", providerHomeView);

        return card;
    }

    // ─── Load adopter pets (fallback; search filter is preferred path) ─────────
    public void loadAdopterPets() {
        if (adopterPanel == null) return;

        adopterPanel.removeAll();
        adopterPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
        adopterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ArrayList<PetsData> pets = adopterDAO.getAvailablePets();

        if (pets != null) {
            for (PetsData pet : pets) {
                adopterPanel.add(buildAdopterCard(pet));
            }
        }

        adopterPanel.revalidate();
        adopterPanel.repaint();
        revalidateScrollPane(adopterPanel);

        if (adopterCountLabel != null && pets != null) {
            adopterCountLabel.setText(String.valueOf(pets.size()));
        }
    }

    // ─── Build adopter card (reused by AdopterHomeSearchFilter) ───────────────
    public PetCardPanel buildAdopterCard(PetsData pet) {
        PetCardPanel card = new PetCardPanel(pet);

        card.setPreferredSize(new java.awt.Dimension(250, 355));
        card.setMinimumSize  (new java.awt.Dimension(250, 355));
        card.setMaximumSize  (new java.awt.Dimension(250, 355));

        card.getPetName()  .setText(pet.getPetName());
        card.getPetType()  .setText(pet.getPetType());
        card.getPetAge()   .setText(pet.getPetAge());
        card.getPetGender().setText(pet.getPetGender());
        loadImageOnCard(card.getPetImg(), pet.getImagePath());

        card.hideActionButtons();
        card.getFavHomePetCard().addActionListener(e ->
            PetFavController.handleFavToggle(card, pet)
        );
        new PetDetailsController(card, pet, "adopter", adopterHomeView);

        return card;
    }

    // ─── Shared image loader ──────────────────────────────────────────────────
    public void loadImageOnCard(JLabel imgLabel, String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            java.awt.Image img = icon.getImage()
                .getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }

    // ─── Inner classes ────────────────────────────────────────────────────────

    class ImageUploadListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Pet Image");
            chooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif", "bmp"));
            if (chooser.showOpenDialog(addUpdateView) == JFileChooser.APPROVE_OPTION) {
                addUpdateView.setPetImage(chooser.getSelectedFile().getAbsolutePath());
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
            pet.setHouseTrained(getRadio(addUpdateView.getRbYesHouseTrained(),
                addUpdateView.getRbNoHouseTrained()));
            pet.setSpayed(getRadio(addUpdateView.getRbYesSpayed(),
                addUpdateView.getRbNoSpayed()));
            pet.setVaccinated(getRadio(addUpdateView.getRbYesVaccinated(),
                addUpdateView.getRbNoVaccinated()));
            pet.setSpecialNeeds(getRadio(addUpdateView.getRbYesSpecialNeeds(),
                addUpdateView.getRbNoSpecialNeeds()));

            if (isUpdateMode) {
                String currentPath = addUpdateView.getCurrentImagePath();
                pet.setImagePath(currentPath != null ? currentPath : existingImagePath);
                pet.setPetID(updatePetID);
                if (petDAO.updatePet(pet)) {
                    JOptionPane.showMessageDialog(null, "Updated!");
                    addUpdateView.dispose();
                    if (providerHomeView != null) providerHomeView.toFront();
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed!");
                }
            } else {
                pet.setImagePath(addUpdateView.getCurrentImagePath());
                if (petDAO.addPet(pet)) {
                    JOptionPane.showMessageDialog(null, "Added!");
                    addUpdateView.dispose();
                    if (providerHomeView != null) providerHomeView.toFront();
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed!");
                }
            }
        }
    }

    // ─── Static inner classes (usable by both search filters) ─────────────────

    public static class UpdatePetListener implements ActionListener {
        private final PetsData pet;
        private final PetController petController;

        public UpdatePetListener(PetsData pet, PetController petController) {
            this.pet           = pet;
            this.petController = petController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Add_Update_Pet form = new Add_Update_Pet();
            new PetController(form, pet, petController);
            form.setLocationRelativeTo(petController.providerHomeView);
            form.getTxtPetName()  .setText(pet.getPetName());
            form.getCbPetType()   .setSelectedItem(pet.getPetType());
            form.getCbPetGender() .setSelectedItem(pet.getPetGender());
            form.getCbPetAge()    .setSelectedItem(pet.getPetAge());
            setRadio(form.getRbYesHouseTrained(), form.getRbNoHouseTrained(),
                pet.getHouseTrained());
            setRadio(form.getRbYesSpayed(),       form.getRbNoSpayed(),
                pet.getSpayed());
            setRadio(form.getRbYesVaccinated(),   form.getRbNoVaccinated(),
                pet.getVaccinated());
            setRadio(form.getRbYesSpecialNeeds(), form.getRbNoSpecialNeeds(),
                pet.getSpecialNeeds());
            form.setPetImage(pet.getImagePath());
            form.setVisible(true);
        }

        private void setRadio(JRadioButton yes, JRadioButton no, String value) {
            if ("Yes".equalsIgnoreCase(value)) yes.setSelected(true);
            else no.setSelected(true);
        }
    }

    public static class DeletePetListener implements ActionListener {
        private final int petID;
        private final PetController petController;

        public DeletePetListener(int petID, PetController petController) {
            this.petID          = petID;
            this.petController  = petController;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(null, "Delete?", "Confirm",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            if (petController.petDAO.deletePet(petID)) {
                JOptionPane.showMessageDialog(null, "Deleted");
                petController.refresh();
            } else {
                JOptionPane.showMessageDialog(null, "Failed");
            }
        }
    }

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
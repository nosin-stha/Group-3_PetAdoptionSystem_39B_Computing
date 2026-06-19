package Controller;

import DAO.ProviderHomePageDao;
import model.PetsData;
import model.SessionData;
import view.PetCardPanel;
import view.ProviderHomePage;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProviderHomeSearchFilter extends SearchFilterController {

    private static final String PLACEHOLDER = "pet name, house-trained, kathmandu etc ";
    private static final Color  PLACEHOLDER_COLOR = new Color(204, 204, 204);
    private static final Color  TEXT_COLOR = Color.BLACK;

    private final ProviderHomePage    view;
    private final ProviderHomePageDao dao;
    private final PetController       petController;

    public ProviderHomeSearchFilter(ProviderHomePage view, PetController petController) {
        super(view.getSearchbar(), view.getPetTypeFilter(),
              view.getGenderFilter(), view.getPetAgeFilter(),
              view.getClearFilter());
        this.view          = view;
        this.dao           = new ProviderHomePageDao();
        this.petController = petController;

        setupPlaceholder();
        init(); // attaches listeners, sets ready=true, fires first load
        setupSearchButton();
    }

    // ── placeholder focus behaviour ─────────────────────────────────────────
    private void setupPlaceholder() {
        JTextField searchbar = view.getSearchbar();

        searchbar.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchbar.getText().equals(PLACEHOLDER)) {
                    searchbar.setText("");
                    searchbar.setForeground(TEXT_COLOR);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchbar.getText().isBlank()) {
                    searchbar.setText(PLACEHOLDER);
                    searchbar.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    // ── search button ────────────────────────────────────────────────────────
    private void setupSearchButton() {
        view.addSearchButtonListener(e -> applyFilters());
    }

    @Override
    protected void reset() {
        JTextField searchbar = view.getSearchbar();
        searchbar.setText(PLACEHOLDER);
        searchbar.setForeground(PLACEHOLDER_COLOR);

        petTypeFilter.setSelectedIndex(0);
        genderFilter .setSelectedIndex(0);
        ageFilter    .setSelectedIndex(0);

        applyFilters();
    }

    @Override
    protected ArrayList<PetsData> fetchData() {
        ArrayList<PetsData> pets = dao.getPetsByProvider(SessionData.userID);
        return pets != null ? pets : new ArrayList<>();
    }

    @Override
    protected void applyFilters() {
        String query  = cleanQuery();
        String type   = petTypeFilter.getSelectedItem().toString();
        String gender = genderFilter .getSelectedItem().toString();
        String age    = ageFilter    .getSelectedItem().toString();

        ArrayList<PetsData> results = new ArrayList<>();

        for (PetsData pet : fetchData()) {
            if (!type  .equals("Pet Type") && !pet.getPetType()  .equals(type))   continue;
            if (!gender.equals("Gender")   && !pet.getPetGender().equals(gender)) continue;
            if (!age   .equals("Age")      && !pet.getPetAge()   .equals(age))    continue;

            boolean textMatch = query.isBlank()
                || matchesText(pet.getPetName(), query)
                || (query.equalsIgnoreCase("yes") && "Yes".equals(pet.getHouseTrained()));

            if (textMatch) results.add(pet);
        }

        renderResults(results);
    }

    @Override
protected void renderResults(ArrayList<?> data) {
    javax.swing.JPanel panel = view.getProviderPetContainerPanel();
    panel.removeAll();

    if (data.isEmpty()) {
        panel.setLayout(new BorderLayout());
        JLabel noData = new JLabel("No Pets Found", SwingConstants.CENTER);
        noData.setFont(new Font("Segoe UI", Font.BOLD, 18));
        noData.setForeground(new Color(180, 180, 180));
        panel.add(noData, BorderLayout.CENTER);
        view.getTotalPetCountLabel().setText("0");
        panel.revalidate();
        panel.repaint();
        return;
    }


    if (!(panel.getLayout() instanceof java.awt.GridLayout)) {
        panel.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
    }

    for (Object obj : data) {
        PetsData pet = (PetsData) obj;

        PetCardPanel card = new PetCardPanel(pet);
        card.setPreferredSize(new java.awt.Dimension(250, 355));
        card.setMinimumSize  (new java.awt.Dimension(250, 355));
        card.setMaximumSize  (new java.awt.Dimension(250, 355));

        card.hideFavButton();
        card.getPetName()  .setText(pet.getPetName());
        card.getPetType()  .setText(pet.getPetType());
        card.getPetAge()   .setText(pet.getPetAge());
        card.getPetGender().setText(pet.getPetGender());
        loadImage(card.getPetImg(), pet.getImagePath());

        card.addUpdateListener(
            new PetController.UpdatePetListener(pet, petController));
        card.addDeleteListener(
            new PetController.DeletePetListener(pet.getPetID(), petController));
        new PetDetailsController(card, pet, "provider", view);

        panel.add(card);
    }

    view.getTotalPetCountLabel().setText(String.valueOf(data.size()));
    panel.revalidate();
    panel.repaint();
}
    private void loadImage(javax.swing.JLabel imgLabel, String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Image load error: " + e.getMessage());
        }
    }
}
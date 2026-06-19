package Controller;

import DAO.AdopterHomePageDao;
import DAO.ProviderDetailsDAO;
import model.PetsData;
import model.ProviderData;
import view.AdopterHomePage;
import view.PetCardPanel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdopterHomeSearchFilter extends SearchFilterController {

    private static final String PLACEHOLDER = "pet name, house-trained, kathmandu etc ";
    private static final Color  PLACEHOLDER_COLOR = new Color(204, 204, 204);
    private static final Color  TEXT_COLOR = Color.BLACK;

    private final AdopterHomePage    view;
    private final AdopterHomePageDao dao;
    private final ProviderDetailsDAO providerDao;
    private final PetController      petController;

    public AdopterHomeSearchFilter(AdopterHomePage view, PetController petController) {
        super(view.getSearchbar(), view.getPetTypeFilter(),
              view.getGenderFilter(), view.getPetAgeFilter(),
              view.getClearFilter());
        this.view          = view;
        this.dao           = new AdopterHomePageDao();
        this.providerDao   = new ProviderDetailsDAO();
        this.petController = petController;

        setupPlaceholder();
        init(); 
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
        ArrayList<PetsData> pets = dao.getAvailablePets();
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

            ProviderData provider = providerDao.getProviderById(pet.getProviderID());
            String location = (provider != null) ? provider.getAddress() : "";

            boolean textMatch = query.isBlank()
                || matchesText(pet.getPetName(), query)
                || matchesText(location, query)
                || (query.equalsIgnoreCase("yes") && "Yes".equals(pet.getHouseTrained()));

            if (textMatch) results.add(pet);
        }

        renderResults(results);
    }

    @Override
protected void renderResults(ArrayList<?> data) {
    javax.swing.JPanel container = view.getPetContainerPanel();
    container.removeAll();

    if (data.isEmpty()) {
        container.setLayout(new BorderLayout());
        JLabel noData = new JLabel("No Pets Found", SwingConstants.CENTER);
        noData.setFont(new Font("Segoe UI", Font.BOLD, 18));
        noData.setForeground(new Color(180, 180, 180));
        container.add(noData, BorderLayout.CENTER);
        view.getAdopterTotalPetCountLabel().setText("0");
        container.revalidate();
        container.repaint();
        return;
    }

    // Ensure layout is set (defensive – also set in PetController constructor)
    if (!(container.getLayout() instanceof java.awt.GridLayout)) {
        container.setLayout(new java.awt.GridLayout(0, 3, 5, 5));
    }

    for (Object obj : data) {
        PetsData pet = (PetsData) obj;

        PetCardPanel card = new PetCardPanel(pet);
        card.setPreferredSize(new java.awt.Dimension(250, 355));
        card.setMinimumSize  (new java.awt.Dimension(250, 355));
        card.setMaximumSize  (new java.awt.Dimension(250, 355));

        card.getPetName()  .setText(pet.getPetName());
        card.getPetType()  .setText(pet.getPetType());
        card.getPetAge()   .setText(pet.getPetAge());
        card.getPetGender().setText(pet.getPetGender());
        petController.loadImageOnCard(card.getPetImg(), pet.getImagePath());

        card.hideActionButtons();
        card.getFavHomePetCard().addActionListener(e ->
            PetFavController.handleFavToggle(card, pet));
        new PetDetailsController(card, pet, "adopter", view);

        container.add(card);
    }

    container.revalidate();
    container.repaint();
    view.getAdopterTotalPetCountLabel().setText(String.valueOf(data.size()));
}
}
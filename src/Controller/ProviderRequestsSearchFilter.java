package Controller;

import DAO.AdoptionRequestsBoardDAO;
import model.AdoptionRequestData;
import model.PetsData;
import view.AdoptionRequestManagement_ProviderPage;
import view.PetRequestsBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProviderRequestsSearchFilter extends SearchFilterController {

    private static final String PLACEHOLDER = "pet name, house-trained, kathmandu etc ";
    private static final Color  PLACEHOLDER_COLOR = new Color(204, 204, 204);
    private static final Color  TEXT_COLOR = Color.BLACK;

    private final AdoptionRequestManagement_ProviderPage view;
    private final AdoptionRequestsBoardDAO               dao;
    private final int                                    providerID;
    private final AdoptionRequestsBoardController        boardController;

    public ProviderRequestsSearchFilter(
            AdoptionRequestManagement_ProviderPage view,
            int providerID,
            AdoptionRequestsBoardController boardController) {

        super(view.getSearchbar(), view.getPetTypeFilter(),
              view.getGenderFilter(), view.getPetAgeFilter(),
              view.getClearFilter());

        this.view            = view;
        this.dao             = new AdoptionRequestsBoardDAO();
        this.providerID      = providerID;
        this.boardController = boardController;

        setupPlaceholder();
        init(); 
        setupSearchButton();
    }

  
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

    // ── data ─────────────────────────────────────────────────────────────────

    @Override
    protected ArrayList<PetsData> fetchData() {
        ArrayList<PetsData> pets = dao.getPetsByProvider(providerID);
        return pets != null ? pets : new ArrayList<>();
    }

    // ── filter ───────────────────────────────────────────────────────────────

    @Override
    protected void applyFilters() {
        String query  = cleanQuery();
        String type   = petTypeFilter.getSelectedItem().toString();
        String gender = genderFilter .getSelectedItem().toString();
        String age    = ageFilter    .getSelectedItem().toString();

        ArrayList<PetsData>                       matchedPets     = new ArrayList<>();
        ArrayList<ArrayList<AdoptionRequestData>> matchedRequests = new ArrayList<>();

        for (PetsData pet : fetchData()) {
            if (!type  .equals("Pet Type") && !pet.getPetType()  .equals(type))   continue;
            if (!gender.equals("Gender")   && !pet.getPetGender().equals(gender)) continue;
            if (!age   .equals("Age")      && !pet.getPetAge()   .equals(age))    continue;

            boolean textMatch = query.isBlank()
                || matchesText(pet.getPetName(), query);

            if (textMatch) {
                matchedPets.add(pet);
                matchedRequests.add(dao.getRequestsByPet(pet.getPetID()));
            }
        }

        renderResults(matchedPets, matchedRequests);
    }

    private void renderResults(ArrayList<PetsData> pets,
                               ArrayList<ArrayList<AdoptionRequestData>> allRequests) {
        boardController.populateScrollPanel(pets, allRequests);
    }

    @Override
    protected void renderResults(ArrayList<?> data) { /* unused */ }
}
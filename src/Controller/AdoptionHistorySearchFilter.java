package Controller;

import DAO.ProviderAdoptionHistoryDAO;
import model.AdoptionRequestData;
import model.SessionData;
import view.AdoptionHistoryCard;
import view.ProviderAdoptionHistory;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdoptionHistorySearchFilter extends SearchFilterController {

    private static final String PLACEHOLDER = "pet name, house-trained, kathmandu etc ";
    private static final Color  PLACEHOLDER_COLOR = new Color(204, 204, 204);
    private static final Color  TEXT_COLOR = Color.BLACK;

    private final ProviderAdoptionHistory    view;
    private final ProviderAdoptionHistoryDAO dao;

    public AdoptionHistorySearchFilter(ProviderAdoptionHistory view) {
        super(view.getSearchbar(), view.getPetTypeFilter(),
              view.getGenderFilter(), view.getPetAgeFilter(),
              view.getClearFilter());
        this.view = view;
        this.dao = new ProviderAdoptionHistoryDAO();

        setupPlaceholder(); 
        init();     
        setupSearchButton();
    }

    private void setupSearchButton() {
        view.addSearchButtonListener(e -> applyFilters());
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

    @Override
    protected void reset() {
        JTextField searchbar = view.getSearchbar();
        searchbar.setText(PLACEHOLDER);
        searchbar.setForeground(PLACEHOLDER_COLOR);

        petTypeFilter.setSelectedIndex(0);
        genderFilter.setSelectedIndex(0);
        ageFilter.setSelectedIndex(0);

        applyFilters();
    }

    @Override
    protected ArrayList<AdoptionRequestData> fetchData() {
        ArrayList<AdoptionRequestData> list =
            dao.getProviderAdoptionHistory(SessionData.userID);
        return list != null ? list : new ArrayList<>();
    }

    @Override
    protected void applyFilters() {
        String query  = cleanQuery(); 
        String type = petTypeFilter.getSelectedItem().toString();
        String gender = genderFilter.getSelectedItem().toString();
        String age = ageFilter.getSelectedItem().toString();

        ArrayList<AdoptionRequestData> results = new ArrayList<>();

        for (AdoptionRequestData r : fetchData()) {
            if (!type  .equals("Pet Type") && !r.getPetType().equals(type))   continue;
            if (!gender.equals("Gender") && !r.getPetGender().equals(gender)) continue;
            if (!age   .equals("Age") && !r.getPetAge().equals(age))    continue;

            boolean textMatch = query.isBlank()
                || matchesText(r.getPetName(), query)
                || matchesText(r.getReqFullName(), query);

            if (textMatch) results.add(r);
        }

        renderResults(results);
    }

    @Override
    protected void renderResults(ArrayList<?> data) {
        JPanel panel = view.getSpnlAdoptionHistory();
        panel.removeAll();

        if (data.isEmpty()) {
            panel.setLayout(new BorderLayout());
            JLabel noData = new JLabel("No History", SwingConstants.CENTER);
            noData.setFont(new Font("Segoe UI", Font.BOLD, 18));
            noData.setForeground(new Color(180, 180, 180));
            panel.add(noData, BorderLayout.CENTER);
            view.getlblPetCount().setText("0");
            panel.revalidate();
            panel.repaint();
            return;
        }

       
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int col = 0;
        int row = 0;

        for (int i = 0; i < data.size(); i++) {
            AdoptionRequestData record = (AdoptionRequestData) data.get(i);
            AdoptionHistoryCard card = buildCard(record);

            gbc.gridx = col;
            gbc.gridy = row;

            if (i == data.size() - 1) {
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
            }

            panel.add(card, gbc);

            col++;
            if (col == 3) {
                col = 0;
                row++;
                gbc.weightx = 0;
                gbc.weighty = 0;
            }
        }

        view.getlblPetCount().setText(String.valueOf(data.size()));
        panel.revalidate();
        panel.repaint();
    }

   
    private AdoptionHistoryCard buildCard(AdoptionRequestData data) {
        AdoptionHistoryCard card = new AdoptionHistoryCard();

        card.setPreferredSize(new Dimension(280, 320));
        card.getlbl_PetName().setText(data.getPetName());
        card.getlbl_PetBreed_fill().setText(data.getPetType());
        card.getlbl_PetGender_fill().setText(data.getPetGender());
        card.getlbl_PetAge_fill().setText(data.getPetAge());
        card.getlbl_AdopterName_fill().setText(data.getReqFullName());
        card.getlbl_AdopterPhoneNum_fill().setText(data.getReqPhoneNo());
        card.getlbl_AdopterAddress_fill().setText(data.getReqAddress());

        JLabel status = card.getlbl_Adopt_Status();
        status.setText("Adopted");
        status.setOpaque(true);
        status.setBackground(new Color(0, 180, 0));
        status.setForeground(Color.WHITE);
        status.setHorizontalAlignment(SwingConstants.CENTER);

        String imagePath = data.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image img = new ImageIcon(imagePath)
                    .getImage()
                    .getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                card.getLblPetPicture().setIcon(new ImageIcon(img));
                card.getLblPetPicture().setText("");
            } catch (Exception e) {
                card.getLblPetPicture().setText("No Image");
            }
        }

        return card;
    }
}
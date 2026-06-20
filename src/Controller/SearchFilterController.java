package Controller;
 
import javax.swing.*;
import java.util.ArrayList;
 
public abstract class SearchFilterController {
 
    protected JTextField      searchbar;
    protected JComboBox<String> petTypeFilter;
    protected JComboBox<String> genderFilter;
    protected JComboBox<String> ageFilter;
    protected JButton         resetButton;
 
    private boolean ready = false;
 
    public SearchFilterController(JTextField searchbar,
                                  JComboBox<String> petTypeFilter,
                                  JComboBox<String> genderFilter,
                                  JComboBox<String> ageFilter,
                                  JButton resetButton) {
        this.searchbar     = searchbar;
        this.petTypeFilter = petTypeFilter;
        this.genderFilter  = genderFilter;
        this.ageFilter     = ageFilter;
        this.resetButton   = resetButton;
    }
 
    
    protected void init() {
        attachListeners();
        ready = true;
        applyFilters();
    }
 
   
    private void attachListeners() {
        searchbar    .addActionListener(e -> { if (ready) applyFilters(); });
        petTypeFilter.addActionListener(e -> { if (ready) applyFilters(); });
        genderFilter .addActionListener(e -> { if (ready) applyFilters(); });
        ageFilter    .addActionListener(e -> { if (ready) applyFilters(); });
        resetButton  .addActionListener(e -> { if (ready) reset(); });
    }
 
    protected void reset() {
        searchbar    .setText("");
        petTypeFilter.setSelectedIndex(0);
        genderFilter .setSelectedIndex(0);
        ageFilter    .setSelectedIndex(0);
        applyFilters();
    }
 
    // ── helpers ───────────────────────────────────────────────────────────────
    protected boolean matchesText(String field, String query) {
        return field != null && field.toLowerCase().contains(query.toLowerCase().trim());
    }
 
 
    protected String cleanQuery() {
        String q = searchbar.getText().trim();
        
        if (q.equalsIgnoreCase("pet name, house-trained, kathmandu etc")
                || q.equalsIgnoreCase("pet name, house-trained, kathmandu etc ")) {
            return "";
        }
        return q;
    }
 
    protected abstract ArrayList<?> fetchData();
    protected abstract void applyFilters();
    protected abstract void renderResults(ArrayList<?> data);
}
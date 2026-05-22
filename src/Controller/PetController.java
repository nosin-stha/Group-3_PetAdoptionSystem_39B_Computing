package Controller;

import DAO.AdopterHomePageDao;
import DAO.ProviderHomePageDao;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import model.PetsData;
import model.SessionData;

public class PetController {

    private final AdopterHomePageDao adopterDAO = new AdopterHomePageDao();
    private final ProviderHomePageDao providerDAO = new ProviderHomePageDao();

    private final JPanel adopterPanel;
    private final JPanel providerPanel;

    private final JScrollPane adopterScrollPane;
    private final JScrollPane providerScrollPane;

    public PetController(
            JPanel adopterPanel,
            JPanel providerPanel,
            JScrollPane adopterScrollPane,
            JScrollPane providerScrollPane
    ) {
        this.adopterPanel = adopterPanel;
        this.providerPanel = providerPanel;
        this.adopterScrollPane = adopterScrollPane;
        this.providerScrollPane = providerScrollPane;

        setupScrollListeners();
    }

    public int loadAdopterPets() {

        if (adopterPanel == null) return 0;

        adopterPanel.removeAll();

        List<PetsData> pets = adopterDAO.getAvailablePets();

        for (PetsData pet : pets) {
            adopterPanel.add(new PetCard(pet));
        }

        adopterPanel.revalidate();
        adopterPanel.repaint();

        return pets.size();
    }

    public int loadProviderPets() {

        if (providerPanel == null) return 0;

        providerPanel.removeAll();

        int providerID = SessionData.userID;

        List<PetsData> allPets = providerDAO.getPetsByProvider(providerID);
        List<PetsData> availablePets = new ArrayList<>();

        for (PetsData pet : allPets) {
            if ("Available".equalsIgnoreCase(pet.getPetAdoptionStatus())) {
                availablePets.add(pet);
                providerPanel.add(new PetCard(pet));
            }
        }

        providerPanel.revalidate();
        providerPanel.repaint();

        return availablePets.size();
    }

    // =========================
    // SCROLL SETTINGS
    // =========================
    private void setupScrollListeners() {

        if (adopterScrollPane != null) {
            configureScroll(adopterScrollPane);
        }

        if (providerScrollPane != null) {
            configureScroll(providerScrollPane);
        }
    }

    private void configureScroll(JScrollPane scrollPane) {

        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setUnitIncrement(16);

        bar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                // Debug scroll position if needed
                // System.out.println("Scroll Position: " + e.getValue());
            }
        });
    }

 
    public void refreshAdopter() {
        loadAdopterPets();
    }

    public void refreshProvider() {
        loadProviderPets();
    }
}
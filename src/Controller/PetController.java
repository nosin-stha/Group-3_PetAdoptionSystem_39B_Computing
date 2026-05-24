package Controller;

import DAO.AdopterHomePageDao;
import DAO.ProviderHomePageDao;
import java.util.List;
import javax.swing.JPanel;
import model.PetsData;
import model.SessionData;
import view.PetCardPanel;

public class PetController {

    private final AdopterHomePageDao adopterDAO = new AdopterHomePageDao();
    private final ProviderHomePageDao providerDAO = new ProviderHomePageDao();

    private final JPanel adopterPanel;
    private final JPanel providerPanel;
    private final javax.swing.JLabel countLabel;

    public PetController(JPanel adopterPanel, JPanel providerPanel, javax.swing.JLabel countLabel) {
        this.adopterPanel = adopterPanel;
        this.providerPanel = providerPanel;
        this.countLabel = countLabel;
    }

    // load pet cards for adopters
    public void loadAdopterPets() {

        if (adopterPanel == null) return;

        adopterPanel.removeAll();

        List<PetsData> pets = adopterDAO.getAvailablePets();

        if (pets != null) {
            for (PetsData pet : pets) {
                adopterPanel.add(new PetCardPanel(pet));
            }
        }

        adopterPanel.revalidate();
        adopterPanel.repaint();
        
        if (countLabel != null) {
            countLabel.setText(String.valueOf(adopterPanel.getComponentCount()));
        }
    }

    // load pet cards for providers
    public void loadProviderPets() {

        if (providerPanel == null) return;

        providerPanel.removeAll();

        int providerID = SessionData.userID;

        List<PetsData> pets = providerDAO.getPetsByProvider(providerID);

        if (pets != null) {
            for (PetsData pet : pets) {
                providerPanel.add(new PetCardPanel(pet));
            }
        }

        providerPanel.revalidate();
        providerPanel.repaint();
        
        if (countLabel != null) {
            countLabel.setText(String.valueOf(providerPanel.getComponentCount()));
        }
    }

    // auto refresh system
    public void refresh() {

        if ("Adopter".equals(SessionData.role)) {
            loadAdopterPets();

        } else if ("Provider".equals(SessionData.role)) {
            loadProviderPets();
        }
    }
}
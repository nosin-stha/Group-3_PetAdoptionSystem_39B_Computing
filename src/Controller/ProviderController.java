package Controller;

import DAO.ProviderHomePageDao;
import java.util.List;
import model.PetsData;
import model.SessionData;

public class ProviderController {
    

    private ProviderHomePageDao dao;
    

    public ProviderController() {
        System.out.println("Logged in provider ID: " + SessionData.userID);
        dao = new ProviderHomePageDao();
    }

    public List<PetsData> getProviderPets() {

        int providerId = SessionData.userID;  // ✅ get logged-in user ID
        return dao.getPetsByProvider(providerId);
        
    }
    
}

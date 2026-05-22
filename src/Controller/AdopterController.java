package Controller;

import DAO.AdopterHomePageDao;
import java.util.List;
import model.PetsData;

public class AdopterController {

    private AdopterHomePageDao dao;

    public AdopterController() {
        dao = new AdopterHomePageDao();
    }

    public List<PetsData> getAvailablePets() {
        return dao.getAvailablePets();
    }
}
package Controller;

import DAO.AdoptionRequestDAO;
import java.util.List;
import javax.swing.JPanel;
import model.AdoptionRequestData;
import model.SessionData;
import view.AdopterRequestCard;

public class AdoptionRequestController {

    private final AdoptionRequestDAO dao = new AdoptionRequestDAO();
    private final JPanel requestPanel;

    public AdoptionRequestController(JPanel requestPanel) {
        this.requestPanel = requestPanel;
    }

    public void loadAdopterRequests() {

        requestPanel.removeAll();

        int adopterID = SessionData.userID;

        List<AdoptionRequestData> requests = dao.getRequestsByAdopter(adopterID);
        
        System.out.println("Total Requests: " + requests.size());
        
        for (AdoptionRequestData req : requests) {
            requestPanel.add(new AdopterRequestCard(req));

            requestPanel.add(javax.swing.Box.createVerticalStrut(10));
        }
        

        requestPanel.revalidate();
        requestPanel.repaint();
    }
    
    public int getTotalRequests() {

    int adopterID = model.SessionData.userID;

    return dao.getTotalRequestsByAdopter(adopterID);
}
}
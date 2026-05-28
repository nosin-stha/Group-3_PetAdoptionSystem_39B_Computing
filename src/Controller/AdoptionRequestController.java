package Controller;

import DAO.AdoptionRequestDAO;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import model.AdoptionRequestData;
import model.SessionData;
import view.AdopterRequestCard;
import view.AdoptionRequestTrackingPage;

public class AdoptionRequestController {

    private final AdoptionRequestDAO dao = new AdoptionRequestDAO();
    private JPanel requestPanel;

    // Constructor for full view setup
    public AdoptionRequestController(AdoptionRequestTrackingPage view) {
        this.requestPanel = view.getRequestContainerPanel();

        // Setup scroll pane
        view.getAdopterAdoptionRequestsScroll()
            .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        view.getAdopterAdoptionRequestsScroll()
            .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Setup panel layout
        requestPanel.setLayout(new javax.swing.BoxLayout(
            requestPanel, javax.swing.BoxLayout.Y_AXIS));
        requestPanel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Load cards and set count
        loadAdopterRequests();
        view.getAdopterTotalRequestsCount().setText(
            String.valueOf(getTotalRequests()));

        requestPanel.revalidate();
        requestPanel.repaint();
    }

    // Keep this ONLY if other places use it, otherwise delete it
    public AdoptionRequestController(JPanel requestPanel) {
        this.requestPanel = requestPanel;
    }

    public void loadAdopterRequests() {
        requestPanel.removeAll();

        List<AdoptionRequestData> requests = 
            dao.getRequestsByAdopter(SessionData.userID);

        for (AdoptionRequestData req : requests) {
            requestPanel.add(new AdopterRequestCard(req));
            requestPanel.add(javax.swing.Box.createVerticalStrut(10));
        }

        requestPanel.revalidate();
        requestPanel.repaint();
    }

    public int getTotalRequests() {
        return dao.getTotalRequestsByAdopter(SessionData.userID);
    }
}
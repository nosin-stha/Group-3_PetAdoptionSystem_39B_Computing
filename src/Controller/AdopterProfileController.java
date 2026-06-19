package Controller;
import DAO.AdopterProfileDAO;        
import model.SessionData;
import view.AdopterProfile;
import view.AdopterProfileUpdate;
import java.sql.ResultSet;

public class AdopterProfileController {
    private AdopterProfile view;
    private AdopterProfileDAO dao;           
    private AdopterProfileUpdate editView = null;  

    public AdopterProfileController(AdopterProfile view) {
        this.view = view;
        this.dao = new AdopterProfileDAO();  
        loadProfile();
        attachListeners();
    }

    private void loadProfile() {
        try {
            ResultSet rs = dao.getAdopterProfile(SessionData.userID);
            if (rs != null && rs.next()) {
                view.getlblUsername_fill().setText(rs.getString("adpUsername"));
                view.getlblEmail_fill().setText(rs.getString("adpEmail"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshProfile() {
        loadProfile();
    }

    private void attachListeners() {
        view.addEditListener(e -> openEditPage());
    }

    private void openEditPage() {
        // ← guard: prevent multiple windows
        if (editView != null && editView.isVisible()) {
            editView.toFront();
            return;
        }

        editView = new AdopterProfileUpdate();

        // ← pre-fill with current values
        editView.setUsername(view.getlblUsername_fill().getText());
        editView.setEmail(view.getlblEmail_fill().getText());

        new AdopterProfileUpdateController(editView);
        editView.setLocationRelativeTo(null);
        editView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        editView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshProfile();
                editView = null;  // ← allow reopening
            }
        });

        editView.setVisible(true);
    }
}
package Controller;

import DAO.AdopterProfileDAO;
import model.SessionData;
import view.AdopterProfile;
import view.AdopterProfileUpdate;
import view.NewPassword;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.sql.ResultSet;

public class AdopterProfileController {
    private AdopterProfile view;
    private AdopterProfileDAO dao;
    private AdopterProfileUpdate editView = null;
    private String currentPfpPath = null;

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
                currentPfpPath = rs.getString("adpPfp");
                loadProfileImage(currentPfpPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfileImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            view.setProfileIcon(new ImageIcon(getClass().getResource("/Images/pfp.png")));
            return;
        }
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            view.setProfileIcon(new ImageIcon(scaled));
        } else {
            System.out.println("Adopter profile image not found at: " + imgFile.getAbsolutePath());
            view.setProfileIcon(new ImageIcon(getClass().getResource("/Images/pfp.png")));
        }
    }

    public void refreshProfile() {
        loadProfile();
    }

    private void attachListeners() {
        view.addEditListener(e -> openEditPage());
        view.addChangePasswordListener(e -> openChangePasswordPage());
    }

    private void openChangePasswordPage() {
        NewPassword pwView = new NewPassword();
        pwView.setLocationRelativeTo(null);
        pwView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        new ChangePasswordController(pwView, ChangePasswordController.ADOPTER, SessionData.userID);
        pwView.setVisible(true);
    }

    private void openEditPage() {
        if (editView != null && editView.isVisible()) {
            editView.toFront();
            return;
        }
        editView = new AdopterProfileUpdate();
        editView.setUsername(view.getlblUsername_fill().getText());
        editView.setEmail(view.getlblEmail_fill().getText());
        new AdopterProfileUpdateController(editView, currentPfpPath);
        editView.setLocationRelativeTo(null);
        editView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        editView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshProfile();
                editView = null;
            }
        });
        editView.setVisible(true);
    }
}
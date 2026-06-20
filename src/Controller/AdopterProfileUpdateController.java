package Controller;

import DAO.AdopterProfileDAO;
import model.SessionData;
import view.AdopterProfileUpdate;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Image;
import java.io.File;

public class AdopterProfileUpdateController {
    private final AdopterProfileUpdate view;
    private final AdopterProfileDAO dao;
    private final String originalPfpPath;
    private String selectedPfpPath = null;

    public AdopterProfileUpdateController(AdopterProfileUpdate view, String currentPfpPath){
        this.view = view;
        this.dao = new AdopterProfileDAO();
        this.originalPfpPath = currentPfpPath;
        loadProfileImage(currentPfpPath);
        attachListeners();
    }

    private void attachListeners(){
        for (java.awt.event.ActionListener al : view.getBtnSave().getActionListeners()) {
            view.getBtnSave().removeActionListener(al);
        }
        view.addSaveListener(e -> handleSave());

        view.addPhotoClickListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                choosePhoto();
            }
        });
    }

    private void choosePhoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));
        int result = chooser.showOpenDialog(view.getDialogParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            selectedPfpPath = path;
            loadProfileImage(path);
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

    private void handleSave(){
        String username = view.getUsername().trim();
        String email = view.getEmail().trim();

        if(username.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(view, "All fields are required.");
            return;
        }

        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if(!email.matches(emailRegex)){
            JOptionPane.showMessageDialog(view,
                "Invalid email format.\nExample: example@gmail.com");
            return;
        }

        String pfpToSave = selectedPfpPath != null ? selectedPfpPath : originalPfpPath;

        boolean success = dao.updateAdopterProfile(
                SessionData.userID,
                username,
                email,
                pfpToSave
        );

        if(success){
            JOptionPane.showMessageDialog(view, "Profile updated successfully.");
            view.dispose();
            SessionData.username = username;
        } else {
            JOptionPane.showMessageDialog(view, "Error updating profile.");
        }
    }
}
package Controller;
import DAO.ProviderProfileDAO;
import model.SessionData;
import view.ProviderProfileUpdate;
import javax.swing.JOptionPane;

public class ProviderProfileUpdateController {
    private ProviderProfileUpdate view;
    private ProviderProfileDAO dao;
    private String selectedImagePath = null; 

    public ProviderProfileUpdateController(ProviderProfileUpdate view, String currentImagePath){
        this.view = view;
        this.dao = new ProviderProfileDAO();
        this.selectedImagePath = currentImagePath; 
        loadCurrentImage();                        
        attachListeners();
    }

   
    private void loadCurrentImage(){
        if(selectedImagePath == null || selectedImagePath.isEmpty()) return;
        try {
            java.io.File imgFile = new java.io.File(selectedImagePath);
            if(imgFile.exists()){
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(selectedImagePath);
                java.awt.Image img = icon.getImage()
                        .getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
                view.getProfileImgLabel().setIcon(new javax.swing.ImageIcon(img));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void attachListeners(){
        view.addSaveListener(e -> handleSave());
        view.addChangeImageListener(e -> chooseImage());
    }

    private void chooseImage(){
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        int result = fileChooser.showOpenDialog(view);
        if(result == javax.swing.JFileChooser.APPROVE_OPTION){
            java.io.File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); 
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(selectedImagePath);
            java.awt.Image img = icon.getImage()
                    .getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
            view.getProfileImgLabel().setIcon(new javax.swing.ImageIcon(img));
        }
    }

    private void handleSave(){
        String username = view.getUsername().trim();
        String shelterName = view.getShelterName().trim();
        String licenseID = view.getLicenseID().trim();
        String phone = view.getPhoneNumber().trim();
        String email = view.getEmail().trim();
        String address = view.getAddress().trim();
        String mission = view.getMissionStatement().trim();
        String policy = view.getAdoptionPolicy().trim();
        String startTime = view.getStartTime();
        String endTime = view.getEndTime();
        String startDay = view.getStartDay();
        String endDay = view.getEndDay();

        if(username.isEmpty() || shelterName.isEmpty() || licenseID.isEmpty() ||
           phone.isEmpty() || email.isEmpty() || address.isEmpty() ||
           mission.isEmpty() || policy.isEmpty()){
            JOptionPane.showMessageDialog(view, "All fields are required.");
            return;
        }
        if(!phone.matches("\\d{10}")){
            JOptionPane.showMessageDialog(view, "Phone number must be exactly 10 digits.");
            return;
        }
        if(!email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")){
            JOptionPane.showMessageDialog(view, "Enter a valid email address.");
            return;
        }
        if(startDay.equalsIgnoreCase(endDay)){
            JOptionPane.showMessageDialog(view, "Start day and end day cannot be the same.");
            return;
        }
        if(startTime.equalsIgnoreCase(endTime)){
            JOptionPane.showMessageDialog(view, "Start time and end time cannot be the same.");
            return;
        }

        boolean success = dao.updateProviderProfile(
                SessionData.userID, username, shelterName, licenseID,
                phone, email, address, startTime, endTime,
                startDay, endDay, mission, policy,
                selectedImagePath  
        );

        if(success){
            JOptionPane.showMessageDialog(view, "Profile updated successfully.");
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Error updating profile.");
        }
    }
}
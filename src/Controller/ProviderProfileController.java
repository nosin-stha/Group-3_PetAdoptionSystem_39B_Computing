package Controller;
import DAO.ProviderDetailsDAO;
import model.ProviderData;
import model.SessionData;
import view.ProviderProfile;
import view.ProviderProfileUpdate;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProviderProfileController {
    private ProviderProfile view;
    private ProviderDetailsDAO dao;
    private ProviderProfileUpdate editView = null;

    public ProviderProfileController(ProviderProfile view){
        this.view = view;
        this.dao = new ProviderDetailsDAO();
        loadProfile();
        attachListeners();
    }

    private void loadProfile(){
        ProviderData provider = dao.getProviderById(SessionData.userID);
        if(provider != null){
            view.getlblShelterName_fill().setText(provider.getShelterName());
            view.getlblPhoneNumber_fill().setText(provider.getPhoneNumber());
            view.getlblEmail_fill().setText(provider.getEmail());
            view.getlblLocation_fill().setText(provider.getAddress());
            view.getlblMissionStatement_fill().setText(provider.getMissionStatement());
            view.getlblAdoptionPolicy_fill().setText(provider.getAdoptionPolicy());

          
            view.setlblStartHour_fill(provider.getStartWorkHour());
            view.setlblEndHour_fill(provider.getEndWorkHour());
            view.setlblStartDay_fill(provider.getStartWorkDay());
            view.setlblEndDay_fill(provider.getEndWorkDay());

            loadProfileImage(provider.getPfp());
        }
    }

    private void loadProfileImage(String imagePath){
        try{
            if(imagePath == null || imagePath.isEmpty()) return;
            File imgFile = new File(imagePath);
            if(imgFile.exists()){
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaled = icon.getImage()
                        .getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                view.getlblProfile().setIcon(new ImageIcon(scaled));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void refreshProfile(){
        loadProfile();
    }

    private void attachListeners(){
        view.addEditListener(e -> openEditPage());
    }

    private void openEditPage(){
    if(editView != null && editView.isVisible()){
        editView.toFront();
        return;
    }

    editView = new ProviderProfileUpdate();

    ProviderData provider = dao.getProviderById(SessionData.userID);
    if(provider != null){
        editView.setUsername(provider.getUsername());
        editView.setShelterName(provider.getShelterName());
        editView.setLicenseID(provider.getLicenseID());
        editView.setPhoneNumber(provider.getPhoneNumber());
        editView.setEmail(provider.getEmail());
        editView.setAddress(provider.getAddress());
        editView.setMissionStatement(provider.getMissionStatement());
        editView.setAdoptionPolicy(provider.getAdoptionPolicy());
        editView.setStartTime(provider.getStartWorkHour());
        editView.setEndTime(provider.getEndWorkHour());
        editView.setStartDay(provider.getStartWorkDay());
        editView.setEndDay(provider.getEndWorkDay());

        new ProviderProfileUpdateController(editView, provider.getPfp()); 
    } else {
        new ProviderProfileUpdateController(editView, null);
    }

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
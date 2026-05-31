package Controller;

import DAO.AdoptionReqDAO;
import DAO.PetDAO;
import model.PetsData;
import model.SessionData;
import view.AdoptionRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdoptionReqController {

    private AdoptionRequest view;
    private int petID;

    
        public AdoptionReqController(AdoptionRequest view, int petID) {
        this.view  = view;
        this.petID = petID;

        loadPetInfo();
        attachListeners();
    }

   private void loadPetInfo() {
    PetDAO dao    = new PetDAO();
    PetsData pet  = dao.getPetById(petID);

    if (pet == null) return;

    view.getPetname_petinfopanal().setText(pet.getPetName());
    view.getPetTyoe_petinfopanal_fill().setText(pet.getPetType());
    view.getPetAge_petinfopanal_fill().setText(pet.getPetAge());
    view.getPetGender_petinfopanal_fill().setText(pet.getPetGender());

    view.getLblPetImg().setText(""); // clear placeholder text

    String imagePath = pet.getImagePath();

if (imagePath != null && !imagePath.isEmpty()) {
    // Try loading as a project resource first
    java.net.URL imgURL = getClass().getResource(imagePath);
    
    if (imgURL != null) {
        ImageIcon icon = new ImageIcon(imgURL);
        Image scaled   = icon.getImage()
                             .getScaledInstance(89, 91, Image.SCALE_SMOOTH);
        view.getLblPetImg().setIcon(new ImageIcon(scaled));
    } else {
        // Try as absolute file path
        java.io.File imgFile = new java.io.File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled   = icon.getImage()
                                 .getScaledInstance(89, 91, Image.SCALE_SMOOTH);
            view.getLblPetImg().setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("Image not found: " + imagePath);
            view.getLblPetImg().setText("No Image");
        }
    }
}}
   
   private void attachListeners() {
    view.addSubmitListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleSubmit();
        }
    });
}

    private void handleSubmit() {
        String fullName = view.getFullName().trim();
        String email    = view.getEmail().trim();
        String address  = view.getAddress().trim();
        String whatsapp = view.getWhatsappNumber().trim();
        String reason   = view.getReason().trim();
        System.out.println("Phone: [" + whatsapp + "]");
        System.out.println("Length: " + whatsapp.length());

        if (fullName.isEmpty() || email.isEmpty() ||
            address.isEmpty() || whatsapp.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill all required fields.");
            return;
        }

        if (!email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(view,
                "Enter a valid email.");
            return;
        }
        
        if (!whatsapp.matches("^(98|97)[0-9]{8}$")) {
            JOptionPane.showMessageDialog(view,
                "Phone number must be 10 digits and start with 98 or 97.");
        return;
        }


        if (reason.length() < 20) {
            JOptionPane.showMessageDialog(view,
                "Please describe your reason in at least 20 characters.");
            return;
        }

        int adopterID  = SessionData.userID;

        AdoptionReqDAO dao = new AdoptionReqDAO();
        boolean success    = dao.insertRequest(
                adopterID, petID,
                fullName, email, whatsapp, address, reason
        );

        if (success) {
            JOptionPane.showMessageDialog(view, "Adoption Request Submitted Successfully!");
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view, "Error submitting request.");
        }
    }
}
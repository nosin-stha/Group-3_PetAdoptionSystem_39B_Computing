package Controller;

import DAO.AdoptionReqDAO;
import DAO.PetDAO;
import model.PetsData;
import model.SessionData;
import view.AdoptionRequest;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

public class AdoptionReqController {

    private final AdoptionRequest view;
    private final int petID;

    public AdoptionReqController(AdoptionRequest view, int petID) {
        this.view = view;
        this.petID = petID;

        loadPetInfo();
        attachListeners();
        setupCharacterCounter();
    }

    private void loadPetInfo() {

        PetDAO dao = new PetDAO();
        PetsData pet = dao.getPetById(petID);

        if (pet == null) {
            return;
        }

        view.getPetname_petinfopanal().setText(pet.getPetName());
        view.getPetTyoe_petinfopanal_fill().setText(pet.getPetType());
        view.getPetAge_petinfopanal_fill().setText(pet.getPetAge());
        view.getPetGender_petinfopanal_fill().setText(pet.getPetGender());

        loadPetImage(pet.getImagePath());
    }

    private void loadPetImage(String imagePath) {

        JLabel imageLabel = view.getLblPetImg();
        imageLabel.setText("");
        imageLabel.setIcon(null);

        if (imagePath == null || imagePath.isEmpty()) {
            imageLabel.setText("No Image");
            return;
        }

        URL imageURL = getClass().getResource(imagePath);

        if (imageURL != null) {
            setScaledImage(imageLabel, new ImageIcon(imageURL));
            return;
        }

        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            setScaledImage(imageLabel, new ImageIcon(imagePath));
        } else {
            System.out.println("Image not found: " + imagePath);
            imageLabel.setText("No Image");
        }
    }

    private void setScaledImage(JLabel label, ImageIcon icon) {

        Image scaledImage = icon.getImage()
                .getScaledInstance(89, 91, Image.SCALE_SMOOTH);

        label.setIcon(new ImageIcon(scaledImage));
    }

    private void attachListeners() {

        view.addSubmitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
    }

    private void setupCharacterCounter() {
        JTextArea txtReason = view.getTxtReasonForAdoption();
        JLabel lblCount = new JLabel("0/100");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCount.setForeground(Color.GRAY);

        JPanel panel = (JPanel) txtReason.getParent().getParent().getParent();

        panel.add(lblCount);
        lblCount.setBounds(540, 365, 60, 16);

        panel.revalidate();
        panel.repaint();

        txtReason.getDocument().addDocumentListener(new DocumentListener() {

            private void updateCounter() {
                int count = txtReason.getText().length();
                lblCount.setText(count + "/100");
                
                if (count > 100) {
                    lblCount.setForeground(Color.RED);
                } else {
                    lblCount.setForeground(Color.GRAY);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCounter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCounter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCounter();
            }
        });
    }

    
    
    
    private void handleSubmit() {
    
        AdoptionReqDAO dao = new AdoptionReqDAO(); 

        String fullName = view.getFullName().trim();
        String email = view.getEmail().trim();
        String address = view.getAddress().trim();
        String whatsapp = view.getWhatsappNumber().trim();
        String reason = view.getReason().trim();

        if (fullName.isEmpty() || email.isEmpty() || address.isEmpty() || whatsapp.isEmpty() || reason.isEmpty()) {
            JOptionPane.showMessageDialog(view,"Please fill all required fields.");
            return;
        }

        if (!email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(view,"Enter a valid email.");
            return;
        }

        if (!whatsapp.matches("^(98|97)[0-9]{8}$")) {
            JOptionPane.showMessageDialog(view,"Phone number must be 10 digits and start with 98 or 97.");
            return;
        }

        if (reason.length() < 20) {
            JOptionPane.showMessageDialog(view,"Please describe your reason in at least 20 characters.");
            return;
        }

        if (reason.length() > 100) {
            JOptionPane.showMessageDialog(view,"Reason for adoption must not exceed 100 characters.");
            return;
        }

        boolean success = dao.insertRequest(SessionData.userID, petID, fullName, email, whatsapp, address, reason);

        if (success) {
            JOptionPane.showMessageDialog(view,"Adoption Request Submitted Successfully!");
            view.dispose();
        } else {
            JOptionPane.showMessageDialog(view,"Error submitting request.");
        }
    }
}
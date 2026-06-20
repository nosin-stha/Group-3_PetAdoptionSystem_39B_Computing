/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */

import DAO.AdoptionReqDAO;
import DAO.PetDAO;
import DAO.ProviderDetailsDAO;
import model.PetsData;
import model.ProviderData;
import view.AdopterRequestCard;
import view.AdopterViewPetDetails;
import view.PetCardPanel;
import view.ProviderViewPetDetails;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.AdoptionRequest;

public class PetDetailsController implements ActionListener {

    private final PetsData pet;
    private final String role;
    private final JFrame parentFrame;
    private final boolean hideAdopt;

    private final PetDAO petDAO = new PetDAO();
    private final ProviderDetailsDAO providerDAO = new ProviderDetailsDAO();

    public PetDetailsController(PetCardPanel card, PetsData pet, String role, JFrame parentFrame) {
        this.pet = pet;
        this.role = role;
        this.parentFrame = parentFrame;
        this.hideAdopt = false;
        card.addViewMoreListener(this);
    }
    
    public PetDetailsController(PetsData pet, String role, JFrame parentFrame) {
        this.pet = pet;
        this.role = role;
        this.parentFrame = parentFrame;
        this.hideAdopt = false;
    }

    public PetDetailsController(AdopterRequestCard card, PetsData pet, String role, JFrame parentFrame) {
        this.pet = pet;
        this.role = role;
        this.parentFrame = parentFrame;
        this.hideAdopt = true;
        card.addViewMoreListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PetsData fullPet = petDAO.getPetById(pet.getPetID());
        if (fullPet == null) {
            System.out.println("PetDetailsController: no pet found for ID " + pet.getPetID());
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if ("adopter".equalsIgnoreCase(role)) {
                    openAdopterView(fullPet);
                } else {
                    openProviderView(fullPet);
                }
            }
        });
    }

    private void openAdopterView(PetsData fullPet) {
        AdopterViewPetDetails view = new AdopterViewPetDetails();
        populatePetFields(view, fullPet);

        ProviderData provider = providerDAO.getProviderByPetId(fullPet.getPetID());
        if (provider != null) {
            view.getShelterNameLabel().setText(provider.getShelterName());
            view.getShelterPhoneLabel().setText(provider.getPhoneNumber());
            view.getShelterEmailLabel().setText(provider.getEmail());
            loadShelterImage(view.getShelterLogoLabel(), provider.getPfp());
        }
        
        view.getViewShelterButton().addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProviderData shelterProvider = providerDAO.getProviderByPetId(fullPet.getPetID());
            if (shelterProvider == null) {
                javax.swing.JOptionPane.showMessageDialog(view,
                    "Shelter info not found.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            view.setVisible(false);
            view.AdopterView_Shelter_Detail shelterDetail =
                new view.AdopterView_Shelter_Detail(shelterProvider, view);
            shelterDetail.setLocationRelativeTo(null);
            shelterDetail.setVisible(true);
        }
    });

        if (hideAdopt || model.SessionData.role.equals("Admin")) {
            view.hideAdoptButton();
        }
        
        view.addAdoptListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            AdoptionReqDAO dao = new AdoptionReqDAO();
            if (dao.hasAlreadyApplied(model.SessionData.userID, fullPet.getPetID())) {
                javax.swing.JOptionPane.showMessageDialog(view,
                    "You have already applied for adoption of this pet.",
                    "Already Applied",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            AdoptionRequest adoptionForm = new AdoptionRequest(fullPet.getPetID());
            new AdoptionReqController(adoptionForm, fullPet.getPetID());
            adoptionForm.setSize(1000, 630);
            adoptionForm.setResizable(false);
            adoptionForm.setLocationRelativeTo(view);
            adoptionForm.setAlwaysOnTop(true);
            adoptionForm.setVisible(true);
        }
    });

        view.getExitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                    parentFrame.toFront();
                }
            }
        });

        if (parentFrame != null) parentFrame.setVisible(false);
        view.pack();
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void openProviderView(PetsData fullPet) {
        ProviderViewPetDetails view = new ProviderViewPetDetails();
        populatePetFields(view, fullPet);

        view.getExitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                    parentFrame.toFront();
                }
            }
        });

        if (parentFrame != null) parentFrame.setVisible(false);
        view.setLocationRelativeTo(null);
        view.setVisible(true);
    }

    private void populatePetFields(AdopterViewPetDetails view, PetsData pet) {
        view.getPetNameLabel().setText(pet.getPetName());
        view.getBreedLabel().setText(pet.getPetType());
        view.getGenderLabel().setText(pet.getPetGender());
        view.getAgeLabel().setText(pet.getPetAge());
        view.getHouseTrainedLabel().setText(pet.getHouseTrained());
        view.getSpayedLabel().setText(pet.getSpayed());
        view.getVaccinatedLabel().setText(pet.getVaccinated());
        view.getSpecialNeedsLabel().setText(pet.getSpecialNeeds());
        loadImage(view.getPetImageLabel(), pet.getImagePath());
    }

    private void populatePetFields(ProviderViewPetDetails view, PetsData pet) {
        view.getPetNameLabel().setText(pet.getPetName());
        view.getBreedLabel().setText(pet.getPetType());
        view.getGenderLabel().setText(pet.getPetGender());
        view.getAgeLabel().setText(pet.getPetAge());
        view.getHouseTrainedLabel().setText(pet.getHouseTrained());
        view.getSpayedLabel().setText(pet.getSpayed());
        view.getVaccinatedLabel().setText(pet.getVaccinated());
        view.getSpecialNeedsLabel().setText(pet.getSpecialNeeds());
        loadImage(view.getPetImageLabel(), pet.getImagePath());
    }

    private void loadImage(javax.swing.JLabel imageLabel, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return;
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(244, 215, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");
        } catch (Exception ex) {
            System.out.println("Image load error: " + ex.getMessage());
        }
    }

    private void loadShelterImage(javax.swing.JLabel imageLabel, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            imageLabel.setText("No Image");
            return;
        }
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            imageLabel.setText("");
        } catch (Exception ex) {
            System.out.println("Shelter image load error: " + ex.getMessage());
            imageLabel.setText("No Image");
        }
    }
}
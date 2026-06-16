/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */

import DAO.PetFavDAO;
import model.PetsData;
import model.SessionData;
import view.AdopterPetFavourite;
import view.AdopterPetFavouriteCard;
import view.PetCardPanel;
import view.SheltViewPetCard;

import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class PetFavController {

    private final PetFavDAO favDAO = new PetFavDAO();

    private AdopterPetFavourite favView;
    private JPanel favScrollPanel;
    private JLabel favCountLabel;


    // Constructor for loading the favourites page
    public PetFavController(AdopterPetFavourite favView) {
        this.favView = favView;
        this.favScrollPanel = favView.getPetfavScrollPanel();
        this.favCountLabel = favView.getPetFavCount();

        favScrollPanel.setLayout(new java.awt.GridBagLayout());

        loadFavourites();
    }


    // Called from PetCardPanel fav button
    public static void handleFavToggle(PetCardPanel card, PetsData pet) {
        PetFavDAO dao = new PetFavDAO();
        int adopterID = SessionData.userID;
        int petID = pet.getPetID();

        if (dao.isFavourite(adopterID, petID)) {
            JOptionPane.showMessageDialog(null,
                "'" +pet.getPetName()+"'" + " is already in your favourites!",
                "Already Favourited",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean success = dao.addFavourite(adopterID, petID);
        if (success) {
            JOptionPane.showMessageDialog(null,
                "'" +pet.getPetName()+"'" + " has been saved to your favourites!",
                "Favourited",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Favourite action failed. Please try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    // Called from SheltViewPetCard fav button
    public static void handleFavToggle(SheltViewPetCard card, PetsData pet) {
        PetFavDAO dao = new PetFavDAO();
        int adopterID = SessionData.userID;
        int petID = pet.getPetID();

        if (dao.isFavourite(adopterID, petID)) {
            JOptionPane.showMessageDialog(null,
                "'" +pet.getPetName()+"'" + " is already in your favourites!",
                "Already Favourited",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean success = dao.addFavourite(adopterID, petID);
        if (success) {
            JOptionPane.showMessageDialog(null,
                "'" +pet.getPetName()+"'"  + " has been saved to your favourites!",
                "Favourited",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Favourite action failed. Please try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    // Load all favourite pet cards into the scroll panel
    public void loadFavourites() {
        favScrollPanel.removeAll();

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        ArrayList<PetsData> pets = favDAO.getFavouritePets(SessionData.userID);

        int row = 0;
        for (PetsData pet : pets) {
            AdopterPetFavouriteCard card = new AdopterPetFavouriteCard();
            populateCard(card, pet);
            gbc.gridy = row;
            favScrollPanel.add(card, gbc);
            row++;
        }

        // Filler to push cards to top
        java.awt.GridBagConstraints filler = new java.awt.GridBagConstraints();
        filler.gridx = 0;
        filler.gridy = row;
        filler.weighty = 1.0;
        filler.fill = java.awt.GridBagConstraints.VERTICAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        favScrollPanel.add(spacer, filler);

        favScrollPanel.revalidate();
        favScrollPanel.repaint();

        if (favCountLabel != null) {
            favCountLabel.setText(String.valueOf(pets.size()));
        }
    }


    private void populateCard(AdopterPetFavouriteCard card, PetsData pet) {
        card.getLblRequestCardPetName().setText(pet.getPetName());
        card.getLblRequestCardPetBreed().setText(pet.getPetType());
        card.getLblRequestCardPetGender().setText(pet.getPetGender());
        card.getLblRequestCardPetAge().setText(pet.getPetAge());

        // Load image
        String imgPath = pet.getImagePath();
        if (imgPath != null && !imgPath.trim().isEmpty() && new File(imgPath).exists()) {
            Image scaled = new ImageIcon(imgPath).getImage()
                               .getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            card.getPetImgRequestCard().setIcon(new ImageIcon(scaled));
            card.getPetImgRequestCard().setText("");
        }

        // Remove button
        card.getPetFav_RemoveBtn().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to remove " + pet.getPetName() + " from favourites?",
                "Remove Favourite",
                JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean success = favDAO.removeFavourite(SessionData.userID, pet.getPetID());
            if (success) {
                JOptionPane.showMessageDialog(null,
                "'"+pet.getPetName()+"'" + " removed from favourites.",
                "Removed",
                JOptionPane.INFORMATION_MESSAGE);
                loadFavourites();
            } else {
                JOptionPane.showMessageDialog(null,
                "Remove action failed. Please try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            }
        });

        // View More button
        card.getPetFav_ViewMoreBtn().addActionListener(e -> {
            new PetDetailsController(pet, "adopter", favView).actionPerformed(e);
        });
    }
}
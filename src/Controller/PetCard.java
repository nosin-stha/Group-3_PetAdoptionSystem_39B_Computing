/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.*;
import java.awt.*;
import model.PetsData;

public class PetCard extends JPanel {

    public PetCard(PetsData pet) {

        setPreferredSize(new Dimension(220, 320));
        setBackground(new Color(248, 230, 182));
        setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        setLayout(null);

        // ================= IMAGE =================
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(20, 15, 180, 110);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        loadImage(imageLabel, pet.getImagePath());
        add(imageLabel);

        // ================= PET NAME =================
        JLabel nameLabel = new JLabel(pet.getPetName());
        nameLabel.setBounds(20, 140, 180, 25);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(nameLabel);

        // ================= INFO =================
        JLabel breedLabel = new JLabel("Breed: " + pet.getPetBreed());
        breedLabel.setBounds(20, 170, 180, 20);
        breedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(breedLabel);

        JLabel genderLabel = new JLabel("Gender: " + pet.getPetGender());
        genderLabel.setBounds(20, 195, 180, 20);
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(genderLabel);

        JLabel ageLabel = new JLabel("Age: " + pet.getPetAge());
        ageLabel.setBounds(20, 220, 180, 20);
        ageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(ageLabel);

        // ================= VIEW MORE BUTTON =================
        JButton viewMoreBtn = new JButton("View More");
        viewMoreBtn.setBounds(60, 260, 100, 30);
        viewMoreBtn.setBackground(new Color(255, 153, 51));
        viewMoreBtn.setFocusPainted(false);
        add(viewMoreBtn);

        viewMoreBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "View More clicked for: " + pet.getPetName());
        });
    }

    private void loadImage(JLabel label, String imagePath) {

        if (imagePath != null && !imagePath.isEmpty()) {

            java.net.URL imgURL = getClass().getResource("/" + imagePath);

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage()
                        .getScaledInstance(180, 110, Image.SCALE_SMOOTH);

                label.setIcon(new ImageIcon(img));
            } else {
                label.setText("No Image");
            }

        } else {
            label.setText("No Image");
        }
    }
}

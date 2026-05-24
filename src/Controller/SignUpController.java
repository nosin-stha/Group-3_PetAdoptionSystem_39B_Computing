/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */

import DAO.UsersDAO;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import model.SessionData;
import view.Login;
import view.OTPWindow;
import view.ProviderExtraWindow;
import view.SignupWindow;

public class SignUpController {

    private SignupWindow signupView;
    private ProviderExtraWindow providerView;

    private String mainImagePath;
    private String providerImagePath;

    private final UsersDAO dao = new UsersDAO();

    // Sign Up Main Window - Constructor
    public SignUpController(SignupWindow signupView) {
        this.signupView = signupView;

        signupView.addSignupListener(new SignupListener());
        signupView.addUploadImageListener(new UploadMainImageListener());
        signupView.addBackToLoginListener(new BackToLoginListener());
    }

    // Provider Extra Window - Constructor
    public SignUpController(ProviderExtraWindow providerView) {
        this.providerView = providerView;

        providerView.addSaveProviderListener(new SaveProviderListener());
        providerView.addUploadProviderImageListener(new UploadProviderImageListener());

        if (SessionData.imagePath != null) {
            ImageIcon icon = new ImageIcon(SessionData.imagePath);
            Image image = icon.getImage().getScaledInstance(
                    providerView.getProviderSignUpPfp().getWidth(),
                    providerView.getProviderSignUpPfp().getHeight(),
                    Image.SCALE_SMOOTH
            );
            providerView.getProviderSignUpPfp().setIcon(new ImageIcon(image));
        }
    }

   // open windows
    public void openWindow(JFrame frame) {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // close windows
    public void closeWindow(JFrame frame) {
        frame.dispose();
    }

    // listener to sign up button of main signup window
    class SignupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String username = signupView.getTxtUsername().getText().trim();
            String password = new String(signupView.getTxtPassword().getPassword()).trim();
            String confirmPassword = new String(signupView.getTxtConfirmPassword().getPassword()).trim();
            String email = signupView.getTxtEmail().getText().trim();
            String role = signupView.getCmbRole().getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(signupView, "Fill all fields!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(signupView, "Password mismatch!");
                return;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(signupView, "Invalid email format!");
                return;
            }

            if (dao.isUsernameExist(username)) {
                JOptionPane.showMessageDialog(signupView, "Username already exists!");
                return;
            }

            if (dao.isEmailExist(email)) {
                JOptionPane.showMessageDialog(signupView, "Email already exists!");
                return;
            }

            // store data in session model
            SessionData.username = username;
            SessionData.password = password;
            SessionData.email = email;
            SessionData.role = role;
            SessionData.imagePath = mainImagePath;

            // open OTP window after valid credentials
            OTPWindow otp = new OTPWindow();
            otp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            otp.setLocationRelativeTo(signupView);

            otp.setAlwaysOnTop(true);
            
            signupView.setEnabled(false);


            otp.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    signupView.setEnabled(true);
                    signupView.toFront();
                }
            });

            openWindow(otp);

           new OTPController(otp).open();
        }
    }

    // listener to upload image in main sign up window
    class UploadMainImageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(signupView);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();
                mainImagePath = file.getAbsolutePath();

                SessionData.imagePath = mainImagePath;

                ImageIcon icon = new ImageIcon(mainImagePath);
                Image image = icon.getImage().getScaledInstance(
                        signupView.getPfpMain().getWidth(),
                        signupView.getPfpMain().getHeight(),
                        Image.SCALE_SMOOTH
                );

                signupView.getPfpMain().setIcon(new ImageIcon(image));
            }
        }
    }

    // listener for back to login button
    class BackToLoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            closeWindow(signupView);

            Login login = new Login();
            new LoginController(login);  

            openWindow(login);
        }
    }

    // listener for save button click pointer
    class SaveProviderListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            model.ProviderData provider = new model.ProviderData();

            provider.setShelterName(providerView.getTxtShelterName().getText().trim());
            provider.setLicenseID(providerView.getTxtLicenseID().getText().trim());
            provider.setPhoneNumber(providerView.getTxtPhoneNumber().getText().trim());
            provider.setAddress(providerView.getTxtAddress().getText().trim());
            provider.setMissionStatement(providerView.getTxtMissionStatement().getText().trim());
            provider.setAdoptionPolicy(providerView.getTxtAdoptionPolicy().getText().trim());

            provider.setUsername(SessionData.username);
            provider.setPassword(SessionData.password);
            provider.setEmail(SessionData.email);
            provider.setPfp(SessionData.imagePath);

            if (provider.getShelterName().isEmpty() ||
                provider.getLicenseID().isEmpty() ||
                provider.getPhoneNumber().isEmpty()) {

                JOptionPane.showMessageDialog(providerView, "Fill required fields!");
                return;
            }

            if (!provider.getPhoneNumber().matches("\\d{10}")) {
                JOptionPane.showMessageDialog(providerView, "Invalid phone number!");
                return;
            }

            boolean inserted = dao.insertProvider(provider);

            if (inserted) {
                JOptionPane.showMessageDialog(providerView, "Registration Successful!");

                closeWindow(providerView);

                Login login = new Login();
                new LoginController(login);

                openWindow(login);

            } else {
                JOptionPane.showMessageDialog(providerView, "Registration Failed!");
            }
        }
    }

   // Listener to upload image when provider extra sign up window opens
    class UploadProviderImageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(providerView);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();
                providerImagePath = file.getAbsolutePath();

                SessionData.imagePath = providerImagePath;

                ImageIcon icon = new ImageIcon(providerImagePath);
                Image image = icon.getImage().getScaledInstance(
                        providerView.getProviderSignUpPfp().getWidth(),
                        providerView.getProviderSignUpPfp().getHeight(),
                        Image.SCALE_SMOOTH
                );

                providerView.getProviderSignUpPfp().setIcon(new ImageIcon(image));
            }
        }
    }
}

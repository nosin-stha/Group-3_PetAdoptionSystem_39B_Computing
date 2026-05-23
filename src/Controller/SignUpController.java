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
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import javax.swing.JOptionPane;
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

    
    
    
    // SignUp Window Controller
    public SignUpController(SignupWindow signupView) {
        this.signupView = signupView;

        signupView.addSignupListener(new SignupListener());
        signupView.addUploadImageListener(new UploadMainImageListener());
        signupView.addBackToLoginListener(new BackToLoginListener());
    }

    
    
    // Provider Extra Sign Up Window Controller

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

    
    
    // Window Opening
    public void open() {
        if (signupView != null) {
            signupView.setVisible(true);
            signupView.setLocationRelativeTo(null);
        } else if (providerView != null) {
            providerView.setVisible(true);
            providerView.setLocationRelativeTo(null);
        }
    }

    
    
    // SIGNUP LISTENER
    
    class SignupListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                System.out.println("SIGNUP CLICKED");

                String username = signupView.getTxtUsername().getText().trim();
                String password = new String(signupView.getTxtPassword().getPassword()).trim();
                String confirmPassword = new String(signupView.getTxtConfirmPassword().getPassword()).trim();
                String email = signupView.getTxtEmail().getText().trim();
                String role = signupView.getCmbRole().getSelectedItem().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
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
        
            
                // check username exists
                if (dao.isUsernameExist(username)) {
                    JOptionPane.showMessageDialog(signupView,"Username already exists!");
                    return;
                }
        
                // check email exists
                if (dao.isEmailExist(email)) {
                    JOptionPane.showMessageDialog(signupView, "Email already exists!");
                    return;
                }
            
                // set session data for after login
                SessionData.username = username;
                SessionData.password = password;
                SessionData.email = email;
                SessionData.role = role;
                SessionData.imagePath = mainImagePath;
                
                
                // open OTP window
                OTPWindow otp = new OTPWindow();

                // center OTP relative to signup window
                otp.setLocationRelativeTo(signupView);
                
                otp.setAlwaysOnTop(true);
                
                // IMPORTANT: ensure closing OTP does NOT exit app
                otp.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                
                // optional: bring to front
                otp.toFront();
                otp.requestFocus();

                // start controller
                new OTPController(otp).open();

            } catch (Exception ex) {
                    JOptionPane.showMessageDialog(signupView, "Error: " + ex.getMessage());
                }
            }
        }
    
    // Uploaded Image Listener at Main SignUp
    class UploadMainImageListener implements ActionListener {
        

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            
            int result = chooser.showOpenDialog(signupView);
            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();

                // SAVE IMAGE PATH
                mainImagePath = file.getAbsolutePath();
                SessionData.imagePath = mainImagePath;

                // DISPLAY IMAGE
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
       
    // BACK TO LOGIN LISTENER
    class BackToLoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // close signup window
            signupView.dispose();

            // open login window
            Login login = new Login();

            login.setVisible(true);
            login.setLocationRelativeTo(null);
    }
}

    
    // SAVE PROVIDER LISTENER


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
            provider.setStartWorkDay(providerView.getCmbStartDay().getSelectedItem().toString());
            provider.setEndWorkDay(providerView.getCmbEndDay().getSelectedItem().toString());
            provider.setStartWorkHour(providerView.getCmbStartHour().getSelectedItem().toString());
            provider.setEndWorkHour(providerView.getCmbEndHour().getSelectedItem().toString());
            
            
            provider.setUsername(SessionData.username);
            provider.setPassword(SessionData.password);
            provider.setEmail(SessionData.email);
            provider.setPfp(SessionData.imagePath);


            // VALIDATION

            if (provider.getShelterName().isEmpty()
                    || provider.getLicenseID().isEmpty()
                    || provider.getPhoneNumber().isEmpty()
                    || provider.getAddress().isEmpty()) {

                JOptionPane.showMessageDialog(
                        providerView,
                        "Please fill all required fields!"
                );

                return;
            }
            
            if (!provider.getPhoneNumber().matches("\\d{10}")) {

                JOptionPane.showMessageDialog(
                        providerView,
                        "Invalid Phone Number! Must be exactly 10 digits."
                );
                return;
            }
            

            boolean inserted = dao.insertProvider(provider);

            if (inserted) {
                JOptionPane.showMessageDialog(providerView,"You have sucessfully registered as a Pet Provider!");
                providerView.dispose();
                Login login = new Login();
           
                login.setVisible(true);
                login.setLocationRelativeTo(null);
                
            } else {
                JOptionPane.showMessageDialog(providerView,"Provider Registration Failed!");
                  
            }
        }
    }
    
    
    // Uploaded Image Listener at Provider Extra SignUp
    class UploadProviderImageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(providerView);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            // SAVE IMAGE PATH
            providerImagePath = file.getAbsolutePath();
            SessionData.imagePath = providerImagePath;

            // DISPLAY IMAGE
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

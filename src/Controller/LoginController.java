package Controller;

import DAO.LoginDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.SessionData;
import view.AdopterHomePage;
import view.Login;
import view.ProviderHomePage;
import view.SignupWindow;

public class LoginController {

    private final Login loginView;
    private final LoginDAO dao;

    public LoginController(Login loginView) {

        this.loginView = loginView;
        this.dao = new LoginDAO();

        this.loginView.getBtnLogin().addActionListener(new LoginListener());
        this.loginView.getBtnCreateAccount().addActionListener(new CreateAccountListener());
    }

    // Open Window
    private void openWindow(JFrame frame) {

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    // Close Window
    private void closeWindow(JFrame frame) {

        frame.setVisible(false);
        frame.dispose();
    }

    // Login Button Listener
    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String username = loginView.getTxtLoginUsername().getText().trim();

            String password = String.valueOf(loginView.getTxtLoginPassword().getPassword());

            String role = loginView.getSelectedRole();

            // VALIDATION
            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {

                JOptionPane.showMessageDialog(loginView,"Please fill all fields.");

                return;
            }

            // LOGIN
            boolean success = dao.loginUser(username, password, role);

            if (!success) {
                JOptionPane.showMessageDialog(loginView, "Invalid login credentials.");
                return;
            }

            // Login as ADOPTER
            if (SessionData.role.equals("Adopter")) {
                JOptionPane.showMessageDialog( loginView, "Adopter Login Successful");

                AdopterHomePage home = new AdopterHomePage();

                // safe pet loading (no crash if panel changes later)
                new PetController(home.getPetContainerPanel(), null, null).loadAdopterPets();

                openWindow(home);
                closeWindow(loginView);
            }

            // Login as PROVIDER
            else if (SessionData.role.equals("Provider")) {
                JOptionPane.showMessageDialog(loginView, "Provider Login Successful");

                ProviderHomePage home = new ProviderHomePage();

                new PetController(null, home.getProviderPetContainerPanel(), null).loadProviderPets();

                openWindow(home);
                closeWindow(loginView);
            }

            // Login as ADMIN
            else if (SessionData.role.equals("Admin")) {
                JOptionPane.showMessageDialog(loginView, "Admin Login Successful\nDashboard not created yet.");
            }
        }
    }
    
    
    // Create Account Button Listener
    class CreateAccountListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            
            SignupWindow signup = new SignupWindow();
            new SignUpController(signup);
            
            signup.setLocationRelativeTo(null);
            signup.setVisible(true);

            loginView.dispose();
        }
    }
}
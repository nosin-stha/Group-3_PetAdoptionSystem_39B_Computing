package Controller;

import DAO.LoginDAO;
import DAO.RequestToAdminDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.SessionData;
import view.AdminAllAccountStatus;
import view.AdopterHomePage;
import view.Login;
import view.ProviderHomePage;
import view.SignupWindow;
import java.sql.Connection;
import database.MySqlConnector;
import view.OTPWindow;
import view.RequestToAdminForm;

public class LoginController {

    private final Login loginView;
    private final LoginDAO dao;

    public LoginController(Login loginView) {
        this.loginView = loginView;
        this.dao = new LoginDAO();
        this.loginView.getBtnLogin().addActionListener(new LoginListener());
        this.loginView.getBtnCreateAccount().addActionListener(new CreateAccountListener());
        this.loginView.addRequestToAdminListener(new RequestToAdminListener());
        
        this.loginView.addForgotPasswordListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            OTPWindow otpWindow = new OTPWindow();
            new ForgotPasswordController(otpWindow);
            otpWindow.setLocationRelativeTo(null);
            otpWindow.setVisible(true);
        }
    });
    }

    private void openWindow(JFrame frame) {
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void closeWindow(JFrame frame) {
        frame.setVisible(false);
        frame.dispose();
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getTxtLoginUsername().getText().trim();
            String password = String.valueOf(loginView.getTxtLoginPassword().getPassword());
            String role     = loginView.getSelectedRole();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(loginView, "Please fill all fields.");
                return;
            }

            String result = dao.loginUser(username, password, role);

            switch (result) {
                case "disabled" -> {
                    JOptionPane.showMessageDialog(
                        loginView,
                        "Your account has been disabled.\nPlease contact support.",
                        "Account Disabled",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                case "failed" -> {
                    JOptionPane.showMessageDialog(
                        loginView,
                        "Invalid login credentials.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
                case "success" -> {
                    if (SessionData.role.equals("Adopter")) {
                        JOptionPane.showMessageDialog(loginView, "Adopter Login Successful");
                        AdopterHomePage home = new AdopterHomePage();
                        openWindow(home);
                        closeWindow(loginView);

                    } else if (SessionData.role.equals("Provider")) {
                        JOptionPane.showMessageDialog(loginView, "Provider Login Successful");
                        ProviderHomePage home = new ProviderHomePage();
                        new Provider_NavigationController(home, SessionData.userID);
                        openWindow(home);
                        closeWindow(loginView);

                    } else if (SessionData.role.equals("Admin")) {
                        JOptionPane.showMessageDialog(loginView, "Admin Login Successful");
                        AdminAllAccountStatus adminPage = new AdminAllAccountStatus();
                        Connection conn = new MySqlConnector().openConnection();
                        new AdminAllAccountsController(adminPage, conn);
                        new Admin_NavigationController(adminPage);
                        openWindow(adminPage);
                        closeWindow(loginView);
                    }
                }
            }
        }
    }

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
    
    class RequestToAdminListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RequestToAdminForm form = new RequestToAdminForm();
            form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
            
            RequestToAdminDAO dao = new RequestToAdminDAO();
            new RequestToAdminController(form, dao);
            
            form.setLocationRelativeTo(null); 
            form.setVisible(true);
        }
    }
}
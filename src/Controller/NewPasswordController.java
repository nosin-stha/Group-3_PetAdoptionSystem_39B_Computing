package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dell
 */

import DAO.ForgotPasswordDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import view.Login;
import view.NewPassword;

public class NewPasswordController {

    private final NewPassword view;
    private final String email;
    private final boolean isForgotPassword;
    private final ForgotPasswordDAO forgotDAO = new ForgotPasswordDAO();

    public NewPasswordController(NewPassword view, String email, boolean isForgotPassword) {
        this.view = view;
        this.email = email;
        this.isForgotPassword = isForgotPassword;

        
        if (isForgotPassword) {
            view.hideOldPasswordField();
        }

       
        view.getShowPasswordCheckBox().addActionListener(e -> {
            boolean show = view.getShowPasswordCheckBox().isSelected();
            view.setShowPassword(show);
        });

        view.addConfirmListener(new ConfirmListener());
    }

    class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String newPassword     = new String(view.getNewPassword());
            String confirmPassword = new String(view.getConfirmPassword());

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(view, "Passwords do not match.");
                return;
            }


            if (!isForgotPassword) {
                String oldPassword = new String(view.getOldPassword());
                if (oldPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Please enter your old password.");
                    return;
                }
            }

            boolean updated = forgotDAO.updatePassword(email, newPassword);

            if (updated) {
                JOptionPane.showMessageDialog(view, "Password updated successfully!");
                view.dispose();

                Login login = new Login();
                new LoginController(login);
                login.setLocationRelativeTo(null);
                login.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(view, "Failed to update password. Please try again.");
            }
        }
    }
}
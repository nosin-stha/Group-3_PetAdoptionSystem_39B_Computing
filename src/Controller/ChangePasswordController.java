/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import DAO.ChangePasswordDAO;
import java.util.Arrays;
import javax.swing.JOptionPane;
import view.NewPassword;
/**
 *
 * @author Dell
 */

public class ChangePasswordController {

    public static final String ADOPTER = ChangePasswordDAO.ADOPTER;
    public static final String PROVIDER = ChangePasswordDAO.PROVIDER;

    private final ChangePasswordDAO dao = new ChangePasswordDAO();
    private final NewPassword view;
    private final String userType;
    private final int userID;

    public ChangePasswordController(NewPassword view, String userType, int userID) {
        this.view = view;
        this.userType = userType;
        this.userID = userID;

      
        view.getShowPasswordCheckBox().addActionListener(e ->
            view.setShowPassword(view.getShowPasswordCheckBox().isSelected())
        );

       
        view.addConfirmListener(e -> handleChangePassword());
    }

    private void handleChangePassword() {
        char[] oldPassChars = view.getOldPassword();
        char[] newPassChars = view.getNewPassword();
        char[] confirmPassChars = view.getConfirmPassword();

        try {
            String oldPassword = new String(oldPassChars);
            String newPassword = new String(newPassChars);
            String confirmPassword = new String(confirmPassChars);

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all fields.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(view, "New password and confirm password do not match.");
                return;
            }
     
            if (newPassword.equals(oldPassword)) {
                JOptionPane.showMessageDialog(view, "New password must be different from the old password.");
                return;
            }

            if (!dao.verifyOldPassword(userType, userID, oldPassword)) {
                JOptionPane.showMessageDialog(view, "Old password is incorrect.");
                return;
            }

            boolean updated = dao.updatePassword(userType, userID, newPassword);
            if (updated) {
                JOptionPane.showMessageDialog(view, "Password changed successfully.");
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to change password. Please try again.");
            }
        } finally {
         
            Arrays.fill(oldPassChars, '0');
            Arrays.fill(newPassChars, '0');
            Arrays.fill(confirmPassChars, '0');
        }
    }
}

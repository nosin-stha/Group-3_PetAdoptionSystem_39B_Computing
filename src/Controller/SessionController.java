package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import model.SessionData;
import view.Login;

public class SessionController {

    // Logout Logic
    public void logout(javax.swing.JFrame currentFrame) {

        // clear session
        SessionData.userID = 0;
        SessionData.username = null;
        SessionData.role = null;
        SessionData.email = null;
        SessionData.password = null;
        SessionData.imagePath = null;

        // close current window
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        // open login again WITH controller
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            //new LoginController(login);
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        });
    }

    // Logout Button - Listener
    public ActionListener getLogoutListener(javax.swing.JFrame frame) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout(frame);
            }
        };
    }
}
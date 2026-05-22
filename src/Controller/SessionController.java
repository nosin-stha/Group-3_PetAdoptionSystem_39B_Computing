package Controller;

import model.SessionData;
import view.Login;

public class SessionController {
public void logout(javax.swing.JFrame currentFrame) {
    // Clear user session data
    model.SessionData.userID = 0;
    model.SessionData.role = null;

    // Instantly close the home page
    if (currentFrame != null) {
        currentFrame.dispose();
    }

    // Instantly open the login page centered and fresh
    javax.swing.SwingUtilities.invokeLater(() -> {
        Login login = new Login();
        login.setVisible(true);
    });
}






public void logout() {
    SessionData.userID = 0;
    SessionData.role = null;
}
}

   

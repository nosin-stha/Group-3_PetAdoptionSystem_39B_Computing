package Controller;

import view.AdopterHomePage;
import view.AdoptionRequestTrackingPage;
import view.AdopterViewPetDetails;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class NavigationController {

    private JFrame currentFrame;

    public NavigationController(JFrame currentFrame) {
        this.currentFrame = currentFrame;
        attachListeners();
    }

    private void attachListeners() {
        if (currentFrame instanceof AdopterHomePage home) {
            home.addHomeListener(new HomeListener());
            home.addMyRequestsListener(new MyRequestsListener());
            home.addLogoutListener(new LogoutListener());

        } else if (currentFrame instanceof AdoptionRequestTrackingPage requests) {
            requests.addHomeListener(new HomeListener());
            requests.addMyRequestsListener(new MyRequestsListener());
            requests.addLogoutListener(new LogoutListener());

        } else if (currentFrame instanceof AdopterViewPetDetails petDetails) {
            petDetails.addHomeListener(new HomeListener());
            petDetails.addMyRequestsListener(new MyRequestsListener());
            petDetails.addLogoutListener(new LogoutListener());
        }
    }

    public class HomeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof AdopterHomePage) return;
            new AdopterHomePage().setVisible(true);
            currentFrame.dispose();
        }
    }

    public class MyRequestsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof AdoptionRequestTrackingPage) return;
            new AdoptionRequestTrackingPage().setVisible(true);
            currentFrame.dispose();
        }
    }

    public class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                currentFrame,
                "Are you sure you want to logout?",
                "Logout",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                new SessionController().logout(currentFrame);
            }
        }
    }
}
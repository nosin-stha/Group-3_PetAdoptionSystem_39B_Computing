/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Dell
 */


import view.AdoptionRequestManagement_ProviderPage;
import view.ProviderAdoptionHistory;
import view.ProviderHomePage;
import view.ProviderProfile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Provider_NavigationController {

    private JFrame currentFrame;
    private int providerID;

    public Provider_NavigationController(JFrame currentFrame, int providerID) {
        this.currentFrame = currentFrame;
        this.providerID = providerID;
        attachListeners();
    }

    private void attachListeners() {
        if (currentFrame instanceof AdoptionRequestManagement_ProviderPage providerPage) {
            providerPage.addHomeListener(new HomeListener());
            providerPage.addAdoptionRequestsListener(new AdoptionRequestsListener());
            providerPage.addAdoptionHistoryListener(new AdoptionHistoryListener());
            providerPage.addLogoutListener(new LogoutListener());
            providerPage.addProfileListener(new ProfileListener());

        } else if (currentFrame instanceof ProviderHomePage home) {
            home.addHomeListener(new HomeListener());
            home.addAdoptionRequestsListener(new AdoptionRequestsListener());
            home.addAdoptionHistoryListener(new AdoptionHistoryListener());
            home.addLogoutListener(new LogoutListener());
            home.addProfileListener(new ProfileListener());

        } else if (currentFrame instanceof ProviderAdoptionHistory history) {
            history.addHomeListener(new HomeListener());
            history.addAdoptionRequestsListener(new AdoptionRequestsListener());
            history.addAdoptionHistoryListener(new AdoptionHistoryListener());
            history.addLogoutListener(new LogoutListener());
            history.addProfileListener(new ProfileListener());
            
        } else if (currentFrame instanceof ProviderProfile proProfile) {
            proProfile.addHomeListener(new HomeListener());
            proProfile.addAdoptionRequestsListener(new AdoptionRequestsListener());
            proProfile.addAdoptionHistoryListener(new AdoptionHistoryListener());
            proProfile.addLogoutListener(new LogoutListener());
            proProfile.addProfileListener(new ProfileListener());
        }  
    }

    public class HomeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof ProviderHomePage) return;
            ProviderHomePage home = new ProviderHomePage();
            new Provider_NavigationController(home, providerID);
            home.setLocationRelativeTo(null);
            home.setVisible(true);
            currentFrame.dispose();
        }
    }

    public class AdoptionRequestsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof AdoptionRequestManagement_ProviderPage) return;
            AdoptionRequestManagement_ProviderPage requestsPage =
                    new AdoptionRequestManagement_ProviderPage();
            new AdoptionRequestsBoardController(requestsPage, providerID);
            new Provider_NavigationController(requestsPage, providerID);
            requestsPage.setLocationRelativeTo(null);
            requestsPage.setVisible(true);
            currentFrame.dispose();
        }
    }

    public class AdoptionHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof ProviderAdoptionHistory) return;
            ProviderAdoptionHistory historyPage = new ProviderAdoptionHistory();
            new Provider_NavigationController(historyPage, providerID);
            historyPage.setLocationRelativeTo(null);
            historyPage.setVisible(true);
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
    
    public class ProfileListener implements ActionListener {
       @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof ProviderProfile) return;
            ProviderProfile proProfile = new ProviderProfile();
            new ProviderProfileController(proProfile);
            new Provider_NavigationController(proProfile, providerID);
            proProfile.setLocationRelativeTo(null);
            proProfile.setVisible(true);
            currentFrame.dispose();
        }
    }
}
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
import view.ProviderHomePage;
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
            providerPage.addLogoutListener(new LogoutListener());
        } else if (currentFrame instanceof ProviderHomePage home) {
            home.addHomeListener(new HomeListener());
            home.addAdoptionRequestsListener(new AdoptionRequestsListener());
            home.addLogoutListener(new LogoutListener());
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

            AdoptionRequestManagement_ProviderPage requestsPage = new AdoptionRequestManagement_ProviderPage();
            new AdoptionRequestsBoardController(requestsPage, providerID); // load data into requestsPage
            new Provider_NavigationController(requestsPage, providerID);   // wire nav into requestsPage
            requestsPage.setLocationRelativeTo(null);
            requestsPage.setVisible(true);   // show the SAME requestsPage, not a new one
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

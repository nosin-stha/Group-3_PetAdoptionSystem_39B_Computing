package Controller;

import Controller.SessionController;
import view.AdopterHomePage;
import view.AdoptionRequestTrackingPage;
import view.AdopterViewPetDetails;
import view.ShelterListingDisplay;
import view.AdopterProfile;
import view.AdopterPetFavourite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import view.AdopterView_Shelter_Detail;

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
            home.addShelterListener(new ShelterListener());
            home.addFavouriteListener(new FavouriteListener());
            home.addProfileListener(new ProfileListener());

        } else if (currentFrame instanceof AdoptionRequestTrackingPage requests) {
            requests.addHomeListener(new HomeListener());
            requests.addMyRequestsListener(new MyRequestsListener());
            requests.addLogoutListener(new LogoutListener());
            requests.addShelterListener(new ShelterListener());
            requests.addFavouriteListener(new FavouriteListener());
            requests.addProfileListener(new ProfileListener());

        } else if (currentFrame instanceof AdopterViewPetDetails petDetails) {
            petDetails.addHomeListener(new HomeListener());
            petDetails.addMyRequestsListener(new MyRequestsListener());
            petDetails.addLogoutListener(new LogoutListener());
            petDetails.addShelterListener(new ShelterListener());
            petDetails.addFavouriteListener(new FavouriteListener());
            petDetails.addProfileListener(new ProfileListener());

        } else if (currentFrame instanceof ShelterListingDisplay shelter) {
            shelter.addHomeListener(new HomeListener());
            shelter.addMyRequestsListener(new MyRequestsListener());
            shelter.addLogoutListener(new LogoutListener());
            shelter.addShelterListener(new ShelterListener());
            shelter.addFavouriteListener(new FavouriteListener());
            shelter.addProfileListener(new ProfileListener());
            
        } else if (currentFrame instanceof AdopterView_Shelter_Detail shelterview) {
            shelterview.addHomeListener(new HomeListener());
            shelterview.addMyRequestsListener(new MyRequestsListener());
            shelterview.addLogoutListener(new LogoutListener());
            shelterview.addShelterListener(new ShelterListener());
            shelterview.addFavouriteListener(new FavouriteListener());
            shelterview.addProfileListener(new ProfileListener());
            
        } else if (currentFrame instanceof AdopterPetFavourite petFavourite) {
            petFavourite.addHomeListener(new HomeListener());
            petFavourite.addMyRequestsListener(new MyRequestsListener());
            petFavourite.addLogoutListener(new LogoutListener());
            petFavourite.addShelterListener(new ShelterListener());
            petFavourite.addFavouriteListener(new FavouriteListener());
            petFavourite.addProfileListener(new ProfileListener());
            
        } else if (currentFrame instanceof AdopterProfile adpProfile) {
            adpProfile.addHomeListener(new HomeListener());
            adpProfile.addMyRequestsListener(new MyRequestsListener());
            adpProfile.addLogoutListener(new LogoutListener());
            adpProfile.addShelterListener(new ShelterListener());
            adpProfile.addFavouriteListener(new FavouriteListener());
            adpProfile.addProfileListener(new ProfileListener());
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

    public class ShelterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof ShelterListingDisplay) return;
            ShelterListingDisplay shelterView = new ShelterListingDisplay();
            new ShelterController(shelterView);
            shelterView.setLocationRelativeTo(null);
            shelterView.setVisible(true);
            currentFrame.dispose();
        }
    }
    
    
    public class FavouriteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof AdopterPetFavourite) return;
            AdopterPetFavourite petFavourites = new AdopterPetFavourite();
            new PetFavController(petFavourites);
            petFavourites.setLocationRelativeTo(null);
            petFavourites.setVisible(true);
            currentFrame.dispose();
        }
    }
    
    
    public class ProfileListener implements ActionListener {
       @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFrame instanceof AdopterProfile) return;
            AdopterProfile adpProfile = new AdopterProfile();
            new AdopterProfileController(adpProfile);
            new NavigationController(adpProfile);
            adpProfile.setLocationRelativeTo(null);
            adpProfile.setVisible(true);
            currentFrame.dispose();
        }
    }
}
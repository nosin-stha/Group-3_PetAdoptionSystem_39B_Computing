/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group3petadoptionsystem;

//import view.AdopterHomePage;



import Controller.LoginController;
import Controller.ReportPetProviderController;
import model.SessionData;


import view.Login;
import view.ReportPetProvider;


//import model.SessionData;
//import view.AdoptionRequest;
//import view.AdoptionRequestTrackingPage;


//import Controller.SignUpController;
//import view.SignupWindow;

//import model.SessionData;
//import view.ProviderHomePage;

//import Controller.SignUpController;
//import view.SignupWindow;

/**
 *
 * @author Dell
 */
public class Group3PetAdoptionSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //SignupWindow view = new SignupWindow();
        //SignUpController controller = new SignUpController(view);

        //Controller.openWindow(view);
        
        //new AdopterHomePage().setVisible(true);
        
        //SessionData.userID = 2;
        //SessionData.role = "Provider";
        //new ProviderHomePage().setVisible(true);
        
        Login login = new Login();

        new LoginController(login); 

        login.setVisible(true);
        login.setLocationRelativeTo(null);
        
        
        
        //SessionData.userID = 1;

        //java.awt.EventQueue.invokeLater(() -> {
          //  new AdoptionRequestTrackingPage().setVisible(true);
        //});
        
        
        
        //SessionData.userID = 1; 
        //int providerID = 1;      
        //ReportPetProvider view = new ReportPetProvider();

        //new ReportPetProviderController(view, providerID);

        //view.setLocationRelativeTo(null);
        //view.setVisible(true);
        
        //SessionData.userID = 1;  
        //SessionData.username = "yourAdopterUsername";
        //int testPetID = 3;  // Use actual petID from DB
        //AdoptionRequest view = new AdoptionRequest(testPetID);
        //view.setLocationRelativeTo(null);
        //view.setVisible(true);
    }
}

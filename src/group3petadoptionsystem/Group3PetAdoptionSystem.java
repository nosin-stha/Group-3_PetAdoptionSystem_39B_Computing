/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group3petadoptionsystem;

//import view.AdopterHomePage;



//import Controller.LoginController;
//import Controller.SignUpController;
//import view.Login;
//import view.SignupWindow;

import model.SessionData;
import view.ProviderHomePage;

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
        
        SessionData.userID = 2;
        SessionData.role = "Provider";
        new ProviderHomePage().setVisible(true);
        
        //Login login = new Login();

        //new LoginController(login); // attach controller

        //login.setVisible(true);
        //login.setLocationRelativeTo(null);
    }
}

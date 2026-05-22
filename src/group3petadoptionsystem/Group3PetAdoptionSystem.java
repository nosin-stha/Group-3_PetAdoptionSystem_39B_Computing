/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group3petadoptionsystem;


<<<<<<< HEAD
import view.AdopterHomePage;
import view.Login;
import view.ProviderHomePage;


=======
import Controller.SignUpController;
import view.SignupWindow;
>>>>>>> 126af96bea7c3c6c7422958fcc11fa48bbd802f1

/**
 *
 * @author Dell
 */
public class Group3PetAdoptionSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
<<<<<<< HEAD
        // TODO code application logic here
        new ProviderHomePage().setVisible(true);
=======

        SignupWindow view = new SignupWindow();
        SignUpController controller = new SignUpController(view);

        controller.open();
>>>>>>> 126af96bea7c3c6c7422958fcc11fa48bbd802f1
    }
    
}


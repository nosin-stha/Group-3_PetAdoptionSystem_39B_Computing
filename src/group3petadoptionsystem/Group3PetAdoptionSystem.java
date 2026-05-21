/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group3petadoptionsystem;


import Controller.SignUpController;
import view.SignupWindow;

/**
 *
 * @author Dell
 */
public class Group3PetAdoptionSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SignupWindow view = new SignupWindow();
        SignUpController controller = new SignUpController(view);

        controller.open();
    }
}

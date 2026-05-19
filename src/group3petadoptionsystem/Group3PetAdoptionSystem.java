/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group3petadoptionsystem;


import database.Db;
import database.MySqlConnector;
import java.sql.Connection;

/**
 *
 * @author Dell
 */
public class Group3PetAdoptionSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Db database = new MySqlConnector();
        Connection result = database.openConnection();
        if(result == null){
            System.out.println("Not conenction");
        }else{
            System.out.println("connected");
        }  
    }
}

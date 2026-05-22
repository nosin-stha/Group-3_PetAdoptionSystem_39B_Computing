/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dell
 */
public class AdopterData {
    private int adopter_id;
    private String username;
    private String password;
    private String email;
    private String Pfp;
    
    public int getAdopterID() {
        return adopter_id;
    }

    public void setAdopterID(int adopter_id) {
        this.adopter_id = adopter_id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPfp() {
        return Pfp;
    }

    public void setPfp(String Pfp) {
        this.Pfp = Pfp;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dell
 */
public class ProviderData {
    private int provider_id;
    private String shelterName;
    private String licenseID;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;

    private String startWorkHour;
    private String endWorkHour;

    private String startWorkDay;
    private String endWorkDay;

    private String missionStatement;
    private String adoptionPolicy;
    
    private String Pfp;
    
    
    public int getProviderID() {
        return provider_id;
    }

    public void setProviderID(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartWorkHour() {
        return startWorkHour;
    }

    public void setStartWorkHour(String startWorkHour) {
        this.startWorkHour = startWorkHour;
    }

    public String getEndWorkHour() {
        return endWorkHour;
    }

    public void setEndWorkHour(String endWorkHour) {
        this.endWorkHour = endWorkHour;
    }

    public String getStartWorkDay() {
        return startWorkDay;
    }

    public void setStartWorkDay(String startWorkDay) {
        this.startWorkDay = startWorkDay;
    }

    public String getEndWorkDay() {
        return endWorkDay;
    }

    public void setEndWorkDay(String endWorkDay) {
        this.endWorkDay = endWorkDay;
    }

    public String getMissionStatement() {
        return missionStatement;
    }

    public void setMissionStatement(String missionStatement) {
        this.missionStatement = missionStatement;
    }

    public String getAdoptionPolicy() {
        return adoptionPolicy;
    }

    public void setAdoptionPolicy(String adoptionPolicy) {
        this.adoptionPolicy = adoptionPolicy;
    }
    
    public String getPfp() {
        return Pfp;
    }

    public void setPfp(String Pfp) {
        this.Pfp = Pfp;
    }
}

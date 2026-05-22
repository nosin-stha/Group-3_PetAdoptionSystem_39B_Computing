/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author OMEN
 */
public class PetsData {

    private int petID;
    private int providerID;
    private String petName;
    private String petBreed;
    private String petGender;
    private String petAge;
    private String houseTrained;
    private String spayed;
    private String vaccinated;
    private String specialNeeds;
    private String petAdoptionStatus;
    private String imagePath;   // ✅ Only ONE declaration

    public PetsData(int petID, int providerID, String petName,
                    String petBreed, String petGender, String petAge,
                    String houseTrained, String spayed,
                    String vaccinated, String specialNeeds,
                    String petAdoptionStatus,
                    String imagePath) {

        this.petID = petID;
        this.providerID = providerID;
        this.petName = petName;
        this.petBreed = petBreed;
        this.petGender = petGender;
        this.petAge = petAge;
        this.houseTrained = houseTrained;
        this.spayed = spayed;
        this.vaccinated = vaccinated;
        this.specialNeeds = specialNeeds;
        this.petAdoptionStatus = petAdoptionStatus;
        this.imagePath = imagePath;
    }

    public int getPetID() {
        return petID;
    }

    public int getProviderID() {
        return providerID;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public String getPetGender() {
        return petGender;
    }

    public String getPetAge() {
        return petAge;
    }

    public String getHouseTrained() {
        return houseTrained;
    }

    public String getSpayed() {
        return spayed;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public String getPetAdoptionStatus() {
        return petAdoptionStatus;
    }

    public String getImagePath() {
        return imagePath;
    }
}
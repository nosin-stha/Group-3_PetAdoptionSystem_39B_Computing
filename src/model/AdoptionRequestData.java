package model;

public class AdoptionRequestData {

    private int adoptionID;
    private int adopterID;
    private int petID;

    private String reqFullName;
    private String reqEmail;
    private String reqPhoneNo;
    private String reqAddress;
    private String reqReason;

    private String adoptionStatus;

    // PET DETAILS
    private String petName;
    private String petBreed;
    private String petGender;
    private String petAge;
    private String imagePath;

    public AdoptionRequestData(
            int adoptionID,
            int adopterID,
            int petID,
            String reqFullName,
            String reqEmail,
            String reqPhoneNo,
            String reqAddress,
            String reqReason,
            String adoptionStatus,
            String petName,
            String petBreed,
            String petGender,
            String petAge,
            String imagePath
    ) {

        this.adoptionID = adoptionID;
        this.adopterID = adopterID;
        this.petID = petID;

        this.reqFullName = reqFullName;
        this.reqEmail = reqEmail;
        this.reqPhoneNo = reqPhoneNo;
        this.reqAddress = reqAddress;
        this.reqReason = reqReason;

        this.adoptionStatus = adoptionStatus;

        this.petName = petName;
        this.petBreed = petBreed;
        this.petGender = petGender;
        this.petAge = petAge;
        this.imagePath = imagePath;
    }

    public int getAdoptionID() {
        return adoptionID;
    }

    public int getAdopterID() {
        return adopterID;
    }

    public int getPetID() {
        return petID;
    }

    public String getReqFullName() {
        return reqFullName;
    }

    public String getReqEmail() {
        return reqEmail;
    }

    public String getReqPhoneNo() {
        return reqPhoneNo;
    }

    public String getReqAddress() {
        return reqAddress;
    }
    
    public String getReqReason() {
        return reqReason;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
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

    public String getImagePath() {
        return imagePath;
    }
}
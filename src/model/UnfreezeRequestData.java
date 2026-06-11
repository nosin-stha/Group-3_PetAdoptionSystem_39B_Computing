package model;

public class UnfreezeRequestData {

    private int recoverID;
    private int providerID;
    private String providerName;
    private String email;
    private String requestDetail;
    private String status;

    public UnfreezeRequestData(int recoverID,
                               int providerID,
                               String providerName,
                               String email,
                               String requestDetail,
                               String status) {

        this.recoverID = recoverID;
        this.providerID = providerID;
        this.providerName = providerName;
        this.email = email;
        this.requestDetail = requestDetail;
        this.status = status;
    }

    public UnfreezeRequestData() {
    }

    // Getters

    public int getRecoverID() {
        return recoverID;
    }

    public int getProviderID() {
        return providerID;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getEmail() {
        return email;
    }

    public String getRequestDetail() {
        return requestDetail;
    }

    public String getStatus() {
        return status;
    }

    // Setters

    public void setRecoverID(int recoverID) {
        this.recoverID = recoverID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRequestDetail(String requestDetail) {
        this.requestDetail = requestDetail;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
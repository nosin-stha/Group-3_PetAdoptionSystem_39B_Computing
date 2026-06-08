/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dell
 */

public class RecoverRequestData {

    private int recoverID;
    private int providerID;
    private String recoverReqReason;
    private String recoverReqStatus;

    public RecoverRequestData(int recoverID, int providerID,
                               String recoverReqReason, String recoverReqStatus) {
        this.recoverID = recoverID;
        this.providerID = providerID;
        this.recoverReqReason = recoverReqReason;
        this.recoverReqStatus = recoverReqStatus;
    }

    // Getters
    public int getRecoverID() {
        return recoverID;
    }
    public int getProviderID() {
        return providerID;
    }
    public String getRecoverReqReason() {
        return recoverReqReason;
    }
    public String getRecoverReqStatus() {
        return recoverReqStatus;
    }

    // Setters
    public void setRecoverID(int recoverID) {
        this.recoverID = recoverID;
    }
    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }
    public void setRecoverReqReason(String recoverReqReason) {
        this.recoverReqReason = recoverReqReason;
    }
    public void setRecoverReqStatus(String recoverReqStatus) {
        this.recoverReqStatus = recoverReqStatus;
    }

    public RecoverRequestData() {
    }
}

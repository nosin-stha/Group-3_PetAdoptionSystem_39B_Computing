/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Dell
 */

public class AccountReportData {

    private int reportID;
    private int adopterID;
    private int providerID;
    private String reportReason;
    private String reportStatus;

    public AccountReportData(int reportID, int adopterID, int providerID,
                              String reportReason, String reportStatus) {
        this.reportID = reportID;
        this.adopterID = adopterID;
        this.providerID = providerID;
        this.reportReason = reportReason;
        this.reportStatus = reportStatus;
    }

    // Getters
    public int getReportID() {
        return reportID;
    }
    public int getAdopterID() {
        return adopterID;
    }
    public int getProviderID() {
        return providerID;
    }
    public String getReportReason() {
        return reportReason;
    }
    public String getReportStatus() {
        return reportStatus;
    }

    // Setters
    public void setReportID(int reportID) {
        this.reportID = reportID;
    }
    public void setAdopterID(int adopterID) {
        this.adopterID = adopterID;
    }
    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }
    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public AccountReportData() {
    }
}

package model;

public class UnfreezeRequestData {

    private int providerID;
    private String providerName;
    private String email;
    private int reports;
    private String requestDetail;
    private String status;

    public int getProviderID() { return providerID; }
    public void setProviderID(int providerID) { this.providerID = providerID; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getReports() { return reports; }
    public void setReports(int reports) { this.reports = reports; }

    public String getRequestDetail() { return requestDetail; }
    public void setRequestDetail(String requestDetail) { this.requestDetail = requestDetail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
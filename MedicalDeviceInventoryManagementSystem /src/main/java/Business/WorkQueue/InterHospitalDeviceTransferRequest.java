/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.LogisticsEnterprise;
import Business.UserAccount.UserAccount;
import Business.models.device.Device;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class InterHospitalDeviceTransferRequest extends WorkRequest {
    
    private Device device;
    private HospitalEnterprise requestingHospital;
    private HospitalEnterprise owningHospital;
    private Date requiredFrom;
    private Date requiredUntil;
    private String justification; 
    
    private UserAccount phsApprover;
    private String phsApprovalStatus;

    private UserAccount ownerDeviceManagerApprover;
    private String ownerApprovalStatus;

    private UserAccount requestingAdminApprover;
    private String requesterAdminApprovalStatus;

    private LogisticsEnterprise logisticsPartner;
    private String logisticsStatus;
    
    public InterHospitalDeviceTransferRequest() {
        super();
        this.phsApprovalStatus = "Pending PHS Approval";
        this.ownerApprovalStatus = "Pending Owner Approval";
        this.requesterAdminApprovalStatus = "Pending Requester Approval";
        this.logisticsStatus = "Not Started";

        super.setStatusString("Sent to PHS");
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public HospitalEnterprise getRequestingHospital() {
        return requestingHospital;
    }

    public void setRequestingHospital(HospitalEnterprise requestingHospital) {
        this.requestingHospital = requestingHospital;
    }

    public HospitalEnterprise getOwningHospital() {
        return owningHospital;
    }

    public void setOwningHospital(HospitalEnterprise owningHospital) {
        this.owningHospital = owningHospital;
    }

    public Date getRequiredFrom() {
        return requiredFrom;
    }

    public void setRequiredFrom(Date requiredFrom) {
        this.requiredFrom = requiredFrom;
    }

    public Date getRequiredUntil() {
        return requiredUntil;
    }

    public void setRequiredUntil(Date requiredUntil) {
        this.requiredUntil = requiredUntil;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public UserAccount getPhsApprover() {
        return phsApprover;
    }

    public void setPhsApprover(UserAccount phsApprover) {
        this.phsApprover = phsApprover;
    }

    public String getPhsApprovalStatus() {
        return phsApprovalStatus;
    }

    public void setPhsApprovalStatus(String phsApprovalStatus) {
        this.phsApprovalStatus = phsApprovalStatus;
    }

    public UserAccount getOwnerDeviceManagerApprover() {
        return ownerDeviceManagerApprover;
    }

    public void setOwnerDeviceManagerApprover(UserAccount ownerDeviceManagerApprover) {
        this.ownerDeviceManagerApprover = ownerDeviceManagerApprover;
    }

    public String getOwnerApprovalStatus() {
        return ownerApprovalStatus;
    }

    public void setOwnerApprovalStatus(String ownerApprovalStatus) {
        this.ownerApprovalStatus = ownerApprovalStatus;
    }

    public UserAccount getRequestingAdminApprover() {
        return requestingAdminApprover;
    }

    public void setRequestingAdminApprover(UserAccount requestingAdminApprover) {
        this.requestingAdminApprover = requestingAdminApprover;
    }

    public String getRequesterAdminApprovalStatus() {
        return requesterAdminApprovalStatus;
    }

    public void setRequesterAdminApprovalStatus(String requesterAdminApprovalStatus) {
        this.requesterAdminApprovalStatus = requesterAdminApprovalStatus;
    }

    public LogisticsEnterprise getLogisticsPartner() {
        return logisticsPartner;
    }

    public void setLogisticsPartner(LogisticsEnterprise logisticsPartner) {
        this.logisticsPartner = logisticsPartner;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
        updateOverallStatusBasedOnLogistics();
    }

    public void updateOverallStatusBasedOnLogistics() {
        if ("Delivered".equalsIgnoreCase(this.logisticsStatus)) {
            super.setStatusString("Transfer Complete - Delivered");
        } else if ("Returned".equalsIgnoreCase(this.logisticsStatus)){
             super.setStatusString("Transfer Finished - Device Returned");
        } else if ("In Transit".equalsIgnoreCase(this.logisticsStatus)){
             super.setStatusString("Transfer In Progress - In Transit");
        }
    }

    @Override
    public String toString() {
        String deviceName = (device != null) ? device.getName() : "Unknown Device";
        String reqHosp = (requestingHospital != null) ? requestingHospital.getName() : "Unknown";
        return "Transfer Request: " + deviceName + " to " + reqHosp;
    }
}

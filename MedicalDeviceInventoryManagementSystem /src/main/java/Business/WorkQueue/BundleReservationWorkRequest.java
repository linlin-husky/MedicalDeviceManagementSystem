package Business.WorkQueue;


import Business.EcoSystem;
import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Organization.LogisticsOrganization;
import Business.Organization.Organization;
import Business.models.bundle.ResourceBundle;
import Business.models.device.Device;
import java.util.ArrayList;

public class BundleReservationWorkRequest extends WorkRequest {
     private Network netWork;
    private Enterprise enterprise;
    private Business.EcoSystem business;
    private String patientName;
    private String patientID;
    private String surgeryType;
    private String requiredDate;
    private String requiredRoomType;
    private String notes;
    private String bundleIdFromTemplate;

    private ResourceBundle resourceBundle;

    private int deviceApprovalsNeeded = 0;
    private int deviceApprovalsReceived = 0;
    private boolean roomApprovalNeeded = false;
    private boolean roomApproved = false;
    private boolean isRejected = false;
    private int teamApprovalsNeeded = 0;
    private int teamApprovalsReceived = 0;
    
    private int transferRequestsNeeded = 0;
    private int transferRequestsReceivedOrCompleted = 0;
    private ArrayList<InterHospitalDeviceTransferRequest> transferRequests;

    public void setBusiness(EcoSystem business) {
        this.business = business;
    }
    
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSurgeryType() {
        return surgeryType;
    }

    public void setSurgeryType(String surgeryType) {
        this.surgeryType = surgeryType;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }
    
    public String getRequiredRoomType() {
        return requiredRoomType;
    }

    public void setRequiredRoomType(String requiredRoomType) {
        this.requiredRoomType = requiredRoomType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBundleIdFromTemplate() {
         if (bundleIdFromTemplate != null && !bundleIdFromTemplate.isEmpty()) {
            return bundleIdFromTemplate;
        }
        if (resourceBundle != null) {
            return resourceBundle.getBundleId();
        }
        return "REQ_" + (getSender() != null ? getSender().getUsername() : "unknown") + "_" + getRequestDate().getTime();
    }

    public void setBundleIdFromTemplate(String bundleId) {
        this.bundleIdFromTemplate = bundleId;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        if (resourceBundle != null) {
            this.bundleIdFromTemplate = resourceBundle.getBundleId();

            System.out.println("BundleReservationWorkRequest: Saving ResourceBundle with " + 
               (resourceBundle.getRequiredDevices() != null ? 
                resourceBundle.getRequiredDevices().size() : "null") + " devices");

            if (resourceBundle.getRequiredDevices() != null) {
                for (Device device : resourceBundle.getRequiredDevices()) {
                    System.out.println("  - Device in bundle: " + 
                       (device != null ? device.getName() : "null"));
                }
            }
        }
    }

    public int getDeviceApprovalsNeeded() {
        return deviceApprovalsNeeded;
    }

    public void setDeviceApprovalsNeeded(int deviceApprovalsNeeded) {
        this.deviceApprovalsNeeded = deviceApprovalsNeeded;
    }

    public int getDeviceApprovalsReceived() {
        return deviceApprovalsReceived;
    }
    
    public int getTransferRequestsNeeded() {
        return transferRequestsNeeded;
    }

    public void setTransferRequestsNeeded(int transferRequestsNeeded) {
        this.transferRequestsNeeded = Math.max(0, transferRequestsNeeded);
    }

    public int getTransferRequestsReceivedOrCompleted() {
        return transferRequestsReceivedOrCompleted;
    }

    public void incrementDeviceApprovalsReceived() {
        this.deviceApprovalsReceived++;
        checkAndFinalizeStatus();
    }

    public boolean isRoomApprovalNeeded() {
        return roomApprovalNeeded;
    }

    public void setRoomApprovalNeeded(boolean roomApprovalNeeded) {
        this.roomApprovalNeeded = roomApprovalNeeded;
    }

    public boolean isRoomApproved() {
        return roomApproved;
    }

    public void setRoomApproved(boolean roomApproved) {
        if (this.roomApprovalNeeded && roomApproved) {
            this.roomApproved = true;
            checkAndFinalizeStatus();
        } else if (!roomApproved) {
            this.roomApproved = false;
        }
    }
    
    public int getTeamApprovalsNeeded() {
        return teamApprovalsNeeded;
    }

    public void setTeamApprovalsNeeded(int teamApprovalsNeeded) {
        this.teamApprovalsNeeded = Math.max(0, teamApprovalsNeeded);
    }

    public int getTeamApprovalsReceived() {
        return teamApprovalsReceived;
    }

    public void incrementTeamApprovalsReceived() {
        this.teamApprovalsReceived++;
        checkAndFinalizeStatus();
    }
    
    public void incrementTransferRequestsCompleted() {
        this.transferRequestsReceivedOrCompleted++;
        checkAndFinalizeStatus();
    }

    public ArrayList<InterHospitalDeviceTransferRequest> getTransferRequests() {
        if (transferRequests == null) {
            transferRequests = new ArrayList<>();
        }
        return transferRequests;
    }

    public void addTransferRequest(InterHospitalDeviceTransferRequest request) {
         getTransferRequests().add(request);
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void markAsRejected() {
        if (!this.isRejected) {
            this.isRejected = true;
            this.setStatusString("Rejected");
            System.out.println("Bundle Request " + getBundleIdFromTemplate() + " marked as Rejected.");
        }
    }

    public void checkAndFinalizeStatus() {
        if (isRejected) {
            if (!"Rejected".equals(getStatus())) setStatusString("Rejected");
            return;
        }

        boolean allDevicesApproved = (deviceApprovalsReceived >= deviceApprovalsNeeded);
        boolean roomRequirementMet = (!roomApprovalNeeded || roomApproved);

        // MODIFIED: Consider it approved if at least one team has approved (not all)
        boolean allTeamsApproved = (teamApprovalsReceived > 0);

        // MODIFIED: Consider transfers complete if any needed transfers are complete
        boolean allTransfersCompleted = (transferRequestsNeeded == 0 || transferRequestsReceivedOrCompleted > 0);

        System.out.println("Debug - Room approval details: needed=" + roomApprovalNeeded + 
                            ", approved=" + roomApproved);
        System.out.println("Debug - Team approvals: received=" + teamApprovalsReceived + 
                            ", needed=" + teamApprovalsNeeded);
        System.out.println("Debug - All approvals met? Devices:" + allDevicesApproved + 
                            ", Room:" + roomRequirementMet + 
                            ", Teams:" + allTeamsApproved + 
                            ", Transfers:" + allTransfersCompleted);

        if (allDevicesApproved && roomRequirementMet && allTeamsApproved) {
            String currentStatus = getStatusString();
            if (currentStatus == null ||
                !(currentStatus.startsWith("Fully Approved") || 
                  currentStatus.startsWith("Approved - Pending Logistics") || 
                  currentStatus.equals("Rejected")))
            {
                System.out.println("All approvals completed for Bundle " + 
                                  getBundleIdFromTemplate() + ". Setting status to Fully Approved.");
                setStatusString("Fully Approved - Ready to Schedule");

                // Make sure to call this method to trigger logistics
                checkAndTriggerLogistics();
            }
        }
    }

   private void checkAndTriggerLogistics() {
    // Create a new logistics request
    DeviceTransferWorkRequest logisticsRequest = new DeviceTransferWorkRequest();
    
    // Set the required properties
    logisticsRequest.setSender(this.getSender());
  
    logisticsRequest.setMessage("Transfer request for device");
    logisticsRequest.setStatusString("Pending Logistics");
    logisticsRequest.setStatus(WorkRequest.Status.Pending);
    
    // Set origin and destination
//    logisticsRequest.setOrigin(this.getOrigin());  // Assuming this class has getOrigin method
//    logisticsRequest.setDestination(this.getDestination());  // Assuming this class has getDestination method
//    
    // Set the DeviceManagementOrganization
//    logisticsRequest.setDmo(this.getDmo());  // Assuming this class has getDmo method
    
    // Find and validate the logistics organization
    Organization logisticsOrg = findLogisticsOrganization();
    if (logisticsOrg != null) {
        // Add the request to the logistics organization's work queue
        logisticsOrg.getWorkQueue().getWorkRequestList().add(logisticsRequest);
        
        // Update the status of the current request
        setStatus(WorkRequest.Status.Sent);
        setStatusString("Logistics Request Sent");
        
        System.out.println("Logistics request created and sent for device transfer");
        logisticsOrg.addWorRequest(logisticsRequest);
    } else {
        // Handle the case when logistics organization is not found
        System.err.println("Could not find Logistics organization to send request");
        setStatusString("Failed to find logistics organization");
        
        
    }
    
    
    
    // Update the database or notify observers if needed
    // saveToDatabase(); // Implement if needed
}


private Organization findLogisticsOrganization() {
    // This implementation depends on your system structure
    for (Network network : business.getNetworkList()) {
        for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
            // Check if this is a logistics enterprise or contains logistics organizations
            for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                if (organization instanceof LogisticsOrganization) {
                    return organization;
                }
            }
        }
    }
    return null;
}

    @Override
    public String toString() {
        return (patientName != null ? patientName : "No Patient") + " - " + (surgeryType != null ? surgeryType : "No Surgery Type");
    }
}
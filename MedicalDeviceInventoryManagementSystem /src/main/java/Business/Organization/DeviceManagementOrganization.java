/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Role.DeviceManagerRole;
import Business.Role.Role;
import Business.WorkQueue.DeviceAcquisationWorkRequest;
import Business.WorkQueue.DeviceReceiptWorkRequest;
import Business.WorkQueue.DeviceReservationWorkRequest;
import Business.WorkQueue.WorkRequest;
import Business.models.device.Device;
import Business.models.device.DeviceDirectory;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author rajath
 */
public class DeviceManagementOrganization extends Organization{
    private DeviceDirectory deviceDirectory;
    
    public DeviceManagementOrganization() {
        super(Organization.Type.DeviceManagement.getValue());
        this.deviceDirectory = new DeviceDirectory();
    }
    
      public ArrayList<Device> getDevices(DeviceReservationWorkRequest request) {
        System.out.println("primaryDevicesLength" + deviceDirectory.getDeviceList().size());
        ArrayList<Device> devices = new ArrayList<Device>();
   
        for (Device device : deviceDirectory.getDeviceList()) {
             System.out.println(device.getName());
              System.out.println(device.getDeviceID());
            if (device.getName().equals(request.getDeviceName())) {
                devices.add(device);
            }
        }
        System.out.println("Total matches: " + devices.size());
        return devices;
    }
      
      public void approveDeviceReservation(Device device, DeviceReservationWorkRequest request) {
        request.approve();
        request.setDeviceID(device.getDeviceID());
    }

    public ArrayList<SupplierOrganization> getSupplierOrgs(DeviceReservationWorkRequest request, Network network) {
        ArrayList<SupplierOrganization> supplierOrgs = new ArrayList<>();
        System.out.println("===== Checking for existing SupplierOrganizations =====");

        for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
           System.out.println("Enterprise: " + enterprise.getName() + " (" + enterprise.getEnterpriseType() + ")");
            for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                
                if (organization instanceof SupplierOrganization) {
                     SupplierOrganization supplierOrg = (SupplierOrganization) organization;
                System.out.println("  -> SupplierOrganization found: " + supplierOrg.getName());
                    supplierOrgs.add((SupplierOrganization) organization);
                    } else {
                System.out.println("  - Skipping non-supplier organization: " + organization.getName() + " (" + organization.getClass().getSimpleName() + ")");
            

                }
            }
        } 
       System.out.println("Total SupplierOrganizations found: " + supplierOrgs.size());
        return supplierOrgs;
    }
    
    public DeviceAcquisationWorkRequest orderDevice(DeviceReservationWorkRequest reservationRequest,
            SupplierOrganization supplierOrg) {
        DeviceAcquisationWorkRequest acquisitionRequest = new DeviceAcquisationWorkRequest();
        acquisitionRequest.setDevice(reservationRequest.getDeviceName());
//        System.out.println("找devicename"+ reservationRequest.getDeviceName());
//        System.out.println("找deviceprice"+ supplierOrg.getDevicePrice(reservationRequest.getDeviceName()));
        acquisitionRequest.setDevicePrice(supplierOrg.getDevicePrice(reservationRequest.getDeviceName()));
        acquisitionRequest.setSender(reservationRequest.getReceiver());
        acquisitionRequest.setSenderOrg(this);

        this.addWorRequest(acquisitionRequest);
        supplierOrg.addWorRequest(acquisitionRequest);

        reservationRequest.setDeviceAcquisationID(acquisitionRequest.getId());
        return acquisitionRequest;
    }

    public void receiveDevice(DeviceReceiptWorkRequest receiptRequest) {
        receiptRequest.approve();
        // Find relevant work requests
        DeviceAcquisationWorkRequest acquisitionRequest = null;
        for (WorkRequest request : getWorkQueue().getWorkRequestList()) {
            if (request instanceof DeviceAcquisationWorkRequest) {
                DeviceAcquisationWorkRequest acqRequest = (DeviceAcquisationWorkRequest) request;
                if (acqRequest.getDeviceTransferWorkRequest().getId() == receiptRequest.getDeviceTransferId()) {
                    acquisitionRequest = acqRequest;
                    break;
                }
            }
        }
        DeviceReservationWorkRequest reservationRequest = null;
        for (WorkRequest request : getWorkQueue().getWorkRequestList()) {
            if (request instanceof DeviceReservationWorkRequest) {
                DeviceReservationWorkRequest resRequest = (DeviceReservationWorkRequest) request;
                if (resRequest.getDeviceAcquisationID() == acquisitionRequest.getId()) {
                    reservationRequest = resRequest;
                    break;
                }
            }
        }

        // Create a device and add to deviceDirectory
        Device device = new Device();
        device.setAvailable(true);
        device.setName(reservationRequest.getDeviceName());
        device.setPrice(acquisitionRequest.getDevicePrice());
//        device.setDeviceID(reservationRequest.getDeviceID());
//        device.setDeviceID(002);
        deviceDirectory.addDevice(device);
        // Update reservationRequest
        reservationRequest.setDeviceID(device.getDeviceID());
        reservationRequest.approve();
        receiptRequest.setStatus(WorkRequest.Status.Completed);
    }

   

    public DeviceDirectory getDeviceDirectory() {
        return deviceDirectory;
    }
    
    public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
         this.deviceDirectory = deviceDirectory;
     }
 
    

    public ArrayList<DeviceAcquisationWorkRequest> getAcquisitionRequests() {
        return getWorkQueue().getWorkRequestList().stream()
                .filter(request -> request instanceof DeviceAcquisationWorkRequest)
                .map(request -> (DeviceAcquisationWorkRequest) request)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<DeviceReservationWorkRequest> getReservationRequests() {
        return getWorkQueue().getWorkRequestList().stream()
                .filter(request -> request instanceof DeviceReservationWorkRequest)
                .map(request -> (DeviceReservationWorkRequest) request)
                .collect(Collectors.toCollection(ArrayList::new));
    }

     
    
    
    
      
      
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new DeviceManagerRole());
        return roles;
    }
}

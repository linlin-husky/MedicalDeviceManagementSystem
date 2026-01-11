/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;



import Business.Enterprise.Enterprise;
import Business.Network.Network;
import Business.Role.Role;
import Business.Role.SupplierRole;
import Business.UserAccount.UserAccount;

import Business.WorkQueue.DeviceAcquisationWorkRequest;
import Business.WorkQueue.DeviceTransferWorkRequest;
import Business.WorkQueue.WorkRequest;
import Business.models.device.Device;
import Business.models.device.DeviceDirectory;
import Business.models.supply.SupplierDirectory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author linlinfan
 */
public class SupplierOrganization extends Organization{
       private DeviceDirectory deviceDirectory;
           private boolean isCertified;
       
       
      public SupplierOrganization() {
        super(Organization.Type.Supplier.getValue());
       this.deviceDirectory = new DeviceDirectory();
    }
      
      
    
    public int getDeviceCount(String deviceName) {
        int count = 0;
        for (Device device : deviceDirectory.getDeviceList()) {
            if (device.getName().equals(deviceName)) {
                count++;
            }
        }
        return count;
    }

    public double getDevicePrice(String deviceName) {
        Double minPrice = null;

        for (Device device : deviceDirectory.getDeviceList()) {
            System.out.println("Supplier Org Get device name" + device.getName());
            if (device.getName().equals(deviceName)) {
                System.out.println("Supplier Org Get device price" + device.getPrice());
                if (minPrice == null || device.getPrice() < minPrice) {
                    minPrice = device.getPrice();
                }
            }
        }

        return minPrice != null ? minPrice : 0.0;
    }
    
     public DeviceDirectory getDeviceDirectory() {
        return deviceDirectory;
    }

    public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
        this.deviceDirectory = deviceDirectory;
    }

    public ArrayList<Device> getDevices(DeviceAcquisationWorkRequest request) {
        ArrayList<Device> matchingDevices = new ArrayList<>();
        for (Device device : deviceDirectory.getDeviceList()) {
            if (device.getName().equals(request.getDeviceName())) {
                matchingDevices.add(device);
            }
        }
        return matchingDevices;
    }

    public ArrayList<LogisticsOrganization> getLogisticsOrgs(
            DeviceAcquisationWorkRequest acquisitionRequest,
            Device device,
            Network network) {
        ArrayList<LogisticsOrganization> logisticsOrgs = new ArrayList<>();
        for (Enterprise enterprise : network.getEnterpriseDirectory().getEnterpriseList()) {
            for (Organization organization : enterprise.getOrganizationDirectory().getOrganizationList()) {
                if (organization instanceof LogisticsOrganization) {
                    logisticsOrgs.add((LogisticsOrganization) organization);
                }
            }
        }
        return logisticsOrgs;
    }

    public DeviceTransferWorkRequest orderDeviceTransfer(
            DeviceAcquisationWorkRequest acquisitionRequest,
            Device device,
            LogisticsOrganization logisticsOrg) {
        DeviceTransferWorkRequest transferRequest = new DeviceTransferWorkRequest();
        transferRequest.setSender(acquisitionRequest.getReceiver());
        acquisitionRequest.setDeviceTransferWorkRequest(transferRequest);
        logisticsOrg.addWorRequest(transferRequest);
        transferRequest.setDmo(acquisitionRequest.getSenderOrg());
        deviceDirectory.removeDevice(device);
        System.out.println("acquisitionRequest.getSenderOrg()" + acquisitionRequest.getSenderOrg());
        return transferRequest;
    }

   

    public void removeDeviceByName(String deviceName) {

    }

    public List<DeviceTransferWorkRequest> getDeviceTransferRequests(SupplierOrganization organization,
            UserAccount userAccount) {
        List<DeviceTransferWorkRequest> transferRequests = new ArrayList<>();

        for (WorkRequest request : organization.getWorkQueue().getWorkRequestList()) {
            if (request instanceof DeviceAcquisationWorkRequest) {
                DeviceAcquisationWorkRequest daRequest = (DeviceAcquisationWorkRequest) request;

                if (daRequest.getStatus() == WorkRequest.Status.Completed &&
                        userAccount.equals(daRequest.getReceiver())) {

                    DeviceTransferWorkRequest transferRequest = daRequest.getDeviceTransferWorkRequest();
                    if (transferRequest != null) {
                        transferRequests.add(transferRequest);
                    }
                }
            }
        }
        return transferRequests;
    }

    public void proceseDeviceAcquisition(DeviceAcquisationWorkRequest deviceAcquisition) {

        if (deviceAcquisition.getStatus() == WorkRequest.Status.Completed) {
            String deviceName = deviceAcquisition.getDeviceName();
            Device device = deviceDirectory.searchDeviceByName(deviceName);

            if (device != null) {
                deviceDirectory.removeDevice(device);
                System.out.println("Device '" + deviceName + "' removed from directory.");
            } else {
                System.out.println("Device '" + deviceName + "' not found in directory.");
            }
        } else {
            System.out.println("Device acquisition request is not approved.");
        }
    }

    public boolean isCertified() {
        return isCertified;
    }

    public void setCertified(boolean isCertified) {
        this.isCertified = isCertified;
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new SupplierRole());
        return roles;
    }

    public List<Device> findDevicesByManufacturerName(String name) {
        return deviceDirectory.getDeviceList().stream()
                .filter(device -> device.getManufacturer() != null && device.getManufacturer().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.getName();
    }


    
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.Organization.DeviceManagementOrganization;
import Business.Organization.SupplierOrganization;
import Business.UserAccount.UserAccount;

/**
 *
 * @author linlinfan
 */
public class DeviceAcquisationWorkRequest extends WorkRequest {

    private String deviceName;
    private double devicePrice;
    private String deliveryDate;
    private DeviceTransferWorkRequest deviceTransferWorkRequest;
    DeviceManagementOrganization senderOrg;

    public DeviceManagementOrganization getSenderOrg() {
        return senderOrg;
    }

    public void setSenderOrg(DeviceManagementOrganization senderOrg) {
        this.senderOrg = senderOrg;
    }

    public DeviceTransferWorkRequest getDeviceTransferWorkRequest() {
        return deviceTransferWorkRequest;
    }

    // private int deviceTransferId;
    public void setDeviceTransferWorkRequest(DeviceTransferWorkRequest deviceTransferWorkRequest) {
        this.deviceTransferWorkRequest = deviceTransferWorkRequest;
    }

    public double getDevicePrice() {
        return devicePrice;
    }

    public void setDevicePrice(Double devicePrice) {
        this.devicePrice = devicePrice;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    // int or null (for SupplierOrg to fill in, if approved)

    public String getDeviceName() {
        return deviceName;
    }

    public void setDevice(String deviceName) {
        this.deviceName = deviceName;
    }
    
    @Override
    public String toString() {
        return this.getDeviceName();
    }

}

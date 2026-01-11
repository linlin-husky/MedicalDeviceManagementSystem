/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.LogisticsManagerRole;
import Business.Role.Role;
import Business.WorkQueue.DeviceReceiptWorkRequest;
import Business.WorkQueue.DeviceTransferWorkRequest;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class LogisticsOrganization extends Organization {

    public LogisticsOrganization() {
        super(Organization.Type.Logistics.getValue());
    }

    public DeviceReceiptWorkRequest transferDevice(DeviceTransferWorkRequest transferRequest) {
        transferRequest.approve();
        DeviceReceiptWorkRequest receiptRequest = new DeviceReceiptWorkRequest();
        receiptRequest.setSender(transferRequest.getReceiver());
        receiptRequest.setDeviceTransferId(transferRequest.getId());
        transferRequest.getDmo().addWorRequest(receiptRequest);
        transferRequest.setDestination("MGH");
        return receiptRequest;
    }

    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new LogisticsManagerRole());
        return roles;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
}

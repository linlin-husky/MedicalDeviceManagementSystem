/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.Organization.DeviceManagementOrganization;

/**
 *
 * @author linlinfan
 */
public class DeviceReceiptWorkRequest extends WorkRequest {

    private int deviceTransferId;
    DeviceManagementOrganization dmo;
    
    public DeviceManagementOrganization getDmo() {
        return dmo;
    }

    public void setDmo(DeviceManagementOrganization dmo) {
        this.dmo = dmo;
    }

    public int getDeviceTransferId() {
        return deviceTransferId;
    }

    public void setDeviceTransferId(int deviceTransferId) {
        this.deviceTransferId = deviceTransferId;
    }
    
    @Override
    public String toString(){
        return String.valueOf(this.getId());
    }

}

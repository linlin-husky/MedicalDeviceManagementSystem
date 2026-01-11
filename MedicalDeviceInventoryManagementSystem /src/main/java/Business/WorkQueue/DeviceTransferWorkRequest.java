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
public class DeviceTransferWorkRequest extends WorkRequest {

    String deliveryDate;
    String Destination;
    String Origin;
    DeviceManagementOrganization dmo;

    public DeviceManagementOrganization getDmo() {
        return dmo;
    }

    public void setDmo(DeviceManagementOrganization dmo) {
        this.dmo = dmo;
    }
    
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String Origin) {
        this.Origin = Origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String Destination) {
        this.Destination = Destination;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }

}

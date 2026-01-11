/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Enterprise;

import Business.Organization.DeviceManagementOrganization;
import Business.Organization.Organization;
import Business.Role.Role;
import Business.models.room.RoomDirectory;
import Business.models.device.DeviceDirectory;
import java.util.ArrayList;

/**
 *
 * @author MyPC1
 */
public class HospitalEnterprise extends Enterprise {

    private RoomDirectory roomDirectory;
    private DeviceDirectory deviceDirectory;

    public HospitalEnterprise(String name){
        super(name,EnterpriseType.Hospital);
        roomDirectory = new RoomDirectory();
        deviceDirectory = new DeviceDirectory();
    }
    
    public RoomDirectory getRoomDirectory() {
        return roomDirectory;
    }

    public void setRoomDirectory(RoomDirectory roomDirectory) {
        this.roomDirectory = roomDirectory;
    }

//    public DeviceDirectory getDeviceDirectory() {
//        return deviceDirectory;
//    }
    
     public DeviceDirectory getDeviceDirectory() {
         DeviceManagementOrganization dmo = 
                 this.getDeviceManagementOrg();
         return dmo.getDeviceDirectory();
    }

//    public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
//        this.deviceDirectory = deviceDirectory;
//    }
    
     public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
         this.deviceDirectory = deviceDirectory;
         DeviceManagementOrganization dmo = this.getDeviceManagementOrg();
         dmo.setDeviceDirectory(deviceDirectory);
     }
 
     private DeviceManagementOrganization getDeviceManagementOrg() {
         DeviceManagementOrganization found = null;
         int count = 0;
         for (Organization org : getOrganizationDirectory().getOrganizationList()) {
             if (org instanceof DeviceManagementOrganization) {
                 found = (DeviceManagementOrganization) org;
                 count++;
             }
         }
         if (count == 0) {
             throw new RuntimeException("DeviceManagementOrganization not found in organization directory");
         }
         if (count > 1) {
             throw new RuntimeException("Multiple instances of DeviceManagementOrganization found - expected only one");
         }
         return found;
     }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        return null;
    }
}
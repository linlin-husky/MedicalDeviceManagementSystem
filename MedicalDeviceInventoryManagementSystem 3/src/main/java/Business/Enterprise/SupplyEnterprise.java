/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Enterprise;

import Business.Role.Role;
import Business.models.device.Device;
import Business.models.device.DeviceDirectory;
import java.util.ArrayList;

/**
 *
 * @author linlinfan
 */
public class SupplyEnterprise extends Enterprise {
    
    private Device device;
    private DeviceDirectory deviceDirectory;
    
    
      
    public SupplyEnterprise(String name){
        super(name,EnterpriseType.Supply);
        deviceDirectory = new DeviceDirectory();
        device = new Device();
    }
    

    public DeviceDirectory getDeviceDirectory() {
        return deviceDirectory;
    }

    public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
        this.deviceDirectory = deviceDirectory;
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        return null;
    }
    
}

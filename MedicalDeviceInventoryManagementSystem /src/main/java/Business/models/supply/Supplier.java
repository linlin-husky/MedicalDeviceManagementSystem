/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.models.supply;

import Business.models.device.DeviceDirectory;

/**
 *
 * @author archil
 */
public class Supplier {

    private String supplierName;
    private DeviceDirectory deviceDirectory;

   

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplyName) {
        this.supplierName = supplyName;
    }

    public DeviceDirectory getdeviceDirectory() {
        return deviceDirectory;
    }

    public void setDeviceDirectory(DeviceDirectory deviceDirectory) {
        this.deviceDirectory = deviceDirectory;
    }

    @Override
    public String toString() {
        return supplierName; 
    }
}

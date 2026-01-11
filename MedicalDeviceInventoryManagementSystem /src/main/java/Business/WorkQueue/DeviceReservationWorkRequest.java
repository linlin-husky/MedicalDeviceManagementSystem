/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.UserAccount.UserAccount;
import Business.models.device.Device;
import Business.models.room.Room;

/**
 *
 * @author rajath
 */
public class DeviceReservationWorkRequest extends WorkRequest{

    String scheduleDate;
    String Duration;
    Device device;
    String deviceName;
    boolean approval = false;
    UserAccount approvedBy;
    int deviceAcquisationID;
    String deviceID;
    
    
    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String Duration) {
        this.Duration = Duration;
    }
    
    public int getDeviceAcquisationID() {
        return deviceAcquisationID;
    }

    public void setDeviceAcquisationID(int deviceAcquisationID) {
        this.deviceAcquisationID = deviceAcquisationID;
    }
    
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
    
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
     
    
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public UserAccount getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UserAccount approvedBy) {
        this.approvedBy = approvedBy;
    }
    
}

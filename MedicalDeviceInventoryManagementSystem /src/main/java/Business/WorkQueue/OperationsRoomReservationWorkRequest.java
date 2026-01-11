/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.UserAccount.UserAccount;
import Business.models.room.Room;

/**
 *
 * @author rajath
 */
public class OperationsRoomReservationWorkRequest extends WorkRequest{
    
    String scheduleDate;
    String Duration;
    Room room;
    boolean approval = false;
    UserAccount approvedBy;
    
    
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
    
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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

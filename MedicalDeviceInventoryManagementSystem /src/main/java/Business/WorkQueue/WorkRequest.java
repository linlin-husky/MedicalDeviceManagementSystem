/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.WorkQueue;

import Business.UserAccount.UserAccount;
import java.util.Date;

/**
 *
 * @author raunak
 */
public abstract class WorkRequest {

    
    private static int count = 1;
    private int id;
    private String message;
    private UserAccount sender;
    private UserAccount receiver;
     private Status statusEnum; // Keeping the enum

    private String statusString;
////    private String status;
//    private Status status;
    private Date requestDate;
    private Date resolveDate;
    
     public enum Status {
        // Waiting,
        Sent,
        Pending,
        // Approved,
        Denied,
        Completed
    }
    
    public WorkRequest(){
//        requestDate = new Date();
        this.id = count++;
        requestDate = new Date();
        statusEnum = Status.Sent;
    }
    
     public WorkRequest(String message, UserAccount sender, UserAccount receiver) {
        this();
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }
    
     public void approve() {
        this.statusEnum = Status.Completed;
        this.resolveDate = new Date();
    }

    public void deny() {
        this.statusEnum = Status.Denied;
        this.resolveDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } 
     
     
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserAccount getSender() {
        return sender;
    }

    public void setSender(UserAccount sender) {
        this.sender = sender;
    }

    public UserAccount getReceiver() {
        return receiver;
    }

    public void setReceiver(UserAccount receiver) {
        this.receiver = receiver;
    }
    
    public Status getStatus() {
        return statusEnum;
    }

    public void setStatus(Status status) {
        this.statusEnum = status;
    }
    
    
    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getResolveDate() {
        return resolveDate;
    }

    public void setResolveDate(Date resolveDate) {
        this.resolveDate = resolveDate;
    }
    
    @Override
    public String toString() {
        return this.message;
    }
}

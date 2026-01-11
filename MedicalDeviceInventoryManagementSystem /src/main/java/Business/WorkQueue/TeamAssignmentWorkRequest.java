/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.WorkQueue;

import Business.UserAccount.UserAccount;
import Business.models.team.MedicalTeam;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class TeamAssignmentWorkRequest extends WorkRequest {
    
    private String requiredSpecialization;
    private Date requiredDateTime;
    private String parentBundleRequestId;
    private MedicalTeam assignedTeam;

    public String getRequiredSpecialization() {
        return requiredSpecialization;
    }

    public void setRequiredSpecialization(String requiredSpecialization) {
        this.requiredSpecialization = requiredSpecialization;
    }

    public Date getRequiredDateTime() {
        return requiredDateTime;
    }

    public void setRequiredDateTime(Date requiredDateTime) {
        this.requiredDateTime = requiredDateTime;
    }

    public String getParentBundleRequestId() {
        return parentBundleRequestId;
    }

    public void setParentBundleRequestId(String parentBundleRequestId) {
        this.parentBundleRequestId = parentBundleRequestId;
    }

    public MedicalTeam getAssignedTeam() {
        return assignedTeam;
    }

    public void setAssignedTeam(MedicalTeam assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    @Override
    public String toString() {
        return "Team Request for " + requiredSpecialization;
    }
    
}

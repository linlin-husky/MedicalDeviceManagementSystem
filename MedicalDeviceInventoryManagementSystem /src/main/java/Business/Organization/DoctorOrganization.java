/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Role.DoctorRole;
import Business.Role.Role;
import Business.models.team.MedicalTeamDirectory;
import java.util.ArrayList;

/**
 *
 * @author Di
 */
public class DoctorOrganization extends Organization{
    
    private MedicalTeamDirectory medicalTeamDirectory;

    public DoctorOrganization() {
        super(Organization.Type.Doctor.getValue());
        medicalTeamDirectory = new MedicalTeamDirectory();
    }
    
    public MedicalTeamDirectory getMedicalTeamDirectory() {
        return medicalTeamDirectory;
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new DoctorRole());
        roles.add(new Business.Role.MedicalTeamManagerRole());
        return roles;
    }
     
}
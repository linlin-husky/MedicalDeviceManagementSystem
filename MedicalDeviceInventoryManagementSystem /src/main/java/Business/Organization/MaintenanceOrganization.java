/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.DoctorRole;
import Business.Role.MaintainerRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author rajath
 */
public class MaintenanceOrganization extends Organization{
    
    public MaintenanceOrganization() {
        super(Organization.Type.Maintenance.getValue());
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new MaintainerRole());
        return roles;
    }
    
}

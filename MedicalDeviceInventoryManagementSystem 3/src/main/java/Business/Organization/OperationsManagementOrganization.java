/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.OperationsManagerRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author rajath
 */
public class OperationsManagementOrganization extends Organization{
    
    public OperationsManagementOrganization() {
        super(Organization.Type.OperationsManagement.getValue());
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList();
        roles.add(new OperationsManagerRole());
        return roles;
    }
}

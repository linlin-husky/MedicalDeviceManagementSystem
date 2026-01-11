/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.Organization;

import Business.Role.PHSAdminRole;
import Business.Role.Role;
import java.util.ArrayList;

/**
 *
 * @author Di
 */
public class PHSAdminOrganization extends Organization {
    public PHSAdminOrganization() {
        super(Organization.Type.PHSAdmin.getValue());
    }
    
    @Override
    public ArrayList<Role> getSupportedRole() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new PHSAdminRole());
        return roles;
    }
}

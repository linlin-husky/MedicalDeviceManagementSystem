/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Organization;

import Business.Organization.Organization.Type;
import java.util.ArrayList;

/**
 *
 * @author raunak
 */
public class OrganizationDirectory {
    
    private ArrayList<Organization> organizationList;

    public OrganizationDirectory() {
        organizationList = new ArrayList();
    }

    public ArrayList<Organization> getOrganizationList() {
        return organizationList;
    }
    
    public Organization createOrganization(Type type){
        Organization organization = null;
        if (type.getValue().equals(Type.Doctor.getValue())){
            organization = new DoctorOrganization();
        }
        else if (type.getValue().equals(Type.DeviceManagement.getValue())){
            organization = new DeviceManagementOrganization();
        }
        else if (type.getValue().equals(Type.OperationsManagement.getValue())){
            organization = new OperationsManagementOrganization();
        }
        else if (type.getValue().equals(Type.PHSAdmin.getValue())) {
            organization = new PHSAdminOrganization();
       
          } else if (type.getValue().equals(Type.Supplier.getValue())) {
            organization = new SupplierOrganization();
        } 
        else if (type.getValue().equals(Type.Maintenance.getValue())){
            organization = new MaintenanceOrganization();
        }
        else if (type.getValue().equals(Type.Logistics.getValue())) {
            organization = new LogisticsOrganization();
        }
        
        if (organization != null) {
            organizationList.add(organization);
        }
        return organization;
    }
}
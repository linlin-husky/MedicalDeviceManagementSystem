/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business.Enterprise;

import Business.Organization.OrganizationDirectory;
import java.util.ArrayList;

/**
 *
 * @author MyPC1
 */
public class EnterpriseDirectory {
    private ArrayList<Enterprise> enterpriseList;
   
    
     public EnterpriseDirectory(){
        enterpriseList=new ArrayList<Enterprise>();
    }

    public ArrayList<Enterprise> getEnterpriseList() {
        return enterpriseList;
    }

    public void setEnterpriseList(ArrayList<Enterprise> enterpriseList) {
        this.enterpriseList = enterpriseList;
    }
    
   
    
    //Create enterprise
    public Enterprise createAndAddEnterprise(String name, Enterprise.EnterpriseType type){
        System.out.println("createAndAddEnterprise called with name: " + name + ", type: " + type);
        Enterprise enterprise=null;
        if( null != type)switch (type) {
            case Hospital -> {
                enterprise=new HospitalEnterprise(name);
                enterpriseList.add(enterprise);
            }
            case Supply -> {
                enterprise = new SupplyEnterprise(name);
                System.out.println("Added Supply: " + name);
                enterpriseList.add(enterprise);
            }
            case Logistics -> {
                enterprise = new LogisticsEnterprise(name);    
                enterpriseList.add(enterprise);
            }
            case PHS -> {
                enterprise = new PHSEnterprise(name);  // Add this line for debugging
                System.out.println("Created PHS Enterprise: " + enterprise);
                enterpriseList.add(enterprise);
            }
            default -> {
            }
        }

        System.out.println("createAndAddEnterprise returning: " + enterprise);
        return enterprise;

    }
}

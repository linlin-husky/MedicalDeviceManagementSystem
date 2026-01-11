
import Business.models.supply.SupplierDirectory;
import model.MasterOrderList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author linlinfan
 */
public class Business {
    
    MasterOrderList masterOrderList;
    SupplierDirectory supplierDirectory; 
    
    
    public Business() {
        
        masterOrderList = new MasterOrderList();
        supplierDirectory = new SupplierDirectory();
    }
    
    public MasterOrderList getMasterOrderList() {
        return masterOrderList;
    }
    
    public void setMasterOrderList(MasterOrderList masterOrderList) {
        this.masterOrderList = masterOrderList;
    }  
    
    public SupplierDirectory getSupplierDirectory() {
        return supplierDirectory;
    }

    public void setSupplierDirectory(SupplierDirectory supplierDirectory) {
        this.supplierDirectory = supplierDirectory;
    }
    
    
    
}

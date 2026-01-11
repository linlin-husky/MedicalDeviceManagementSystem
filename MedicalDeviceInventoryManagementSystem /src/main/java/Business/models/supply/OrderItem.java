/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import Business.models.device.Device;

/**
 *
 * @author linlinfan
 */
public class OrderItem {

  
    Device device;
    double salesPrice;
    int quantity;
    
      public OrderItem(Device device, double salesPrice, int quantity) {
        this.device = device;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
    }
      
      
    public Device getDevice() {
        return device;
    }

    public void setProduct(Device product) {
        this.device = product;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return this.device.toString();
    }
  
    
}

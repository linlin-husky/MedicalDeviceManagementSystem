/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business.models.supply;

import Business.models.device.Device;
import java.util.ArrayList;
import model.OrderItem;

/**
 *
 * @author linlinfan
 */
public class Order {
    
      ArrayList<OrderItem> orderItemList;
      
      
       
    public Order() {
        
        this.orderItemList = new ArrayList<OrderItem>();
    }
    

    public ArrayList<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
    
    public void addNewOrderItem(Device device, double price, int quantity) {
        OrderItem orderItem = new OrderItem(device, price, quantity);
        orderItemList.add(orderItem);
    }


    public void deleteItem(OrderItem item) {
        this.orderItemList.remove(item);
    }


    public OrderItem findDevice(Device device) {

        for (OrderItem oi: this.getOrderItemList()) {
            if (oi.getDevice().equals(device)) {
                return oi;
            }
        }
        return null;
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AdministratorEntity;
import entity.CategoryEntity;
import entity.DeliveryCompanyEntity;

/**
 *
 * @author Yuxin
 */
public class CreateDeliveryReq {
    private String username;
    private String password;
    private DeliveryCompanyEntity newDelivery;
    
    public CreateDeliveryReq() {
        
    }
    
    public CreateDeliveryReq(String username, String password, DeliveryCompanyEntity newDelivery) {
        this.username = username;
        this.password = password;
        this.newDelivery = newDelivery;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeliveryCompanyEntity getNewDelivery() {
        return newDelivery;
    }

    public void setNewDelivery(DeliveryCompanyEntity newDelivery) {
        this.newDelivery = newDelivery;
    }

    

    
    
    
}

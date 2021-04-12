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
public class CreateDeliveryCompanyReq {
    private String username;
    private String password;
    private DeliveryCompanyEntity newDeliveryCompany;
    
    public CreateDeliveryCompanyReq() {
        
    }
    
    public CreateDeliveryCompanyReq(String username, String password, DeliveryCompanyEntity newDeliveryCompany) {
        this.username = username;
        this.password = password;
        this.newDeliveryCompany = newDeliveryCompany;
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

    public DeliveryCompanyEntity getNewDeliveryCompany() {
        return newDeliveryCompany;
    }

    public void setNewDeliveryCompany(DeliveryCompanyEntity newDeliveryCompany) {
        this.newDeliveryCompany = newDeliveryCompany;
    }

    

    
    
    
}

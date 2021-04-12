/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.AdministratorEntity;
import entity.CategoryEntity;

/**
 *
 * @author Yuxin
 */
public class CreateAdminReq {
    private String username;
    private String password;
    private AdministratorEntity newAdmin;
    
    public CreateAdminReq() {
        
    }
    
    public CreateAdminReq(String username, String password, AdministratorEntity newAdmin) {
        this.username = username;
        this.password = password;
        this.newAdmin = newAdmin;
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

    public AdministratorEntity getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(AdministratorEntity newAdmin) {
        this.newAdmin = newAdmin;
    }

    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.CategoryEntity;

/**
 *
 * @author Yuxin
 */
public class CreateCategoryReq {
    private String username;
    private String password;
    private CategoryEntity newCategoryEntity;
    
    public CreateCategoryReq() {
        
    }
    
    public CreateCategoryReq(String username, String password, CategoryEntity categoryEntity) {
        this.username = username;
        this.password = password;
        this.newCategoryEntity = categoryEntity;
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

    public CategoryEntity getNewCategoryEntity() {
        return newCategoryEntity;
    }

    public void setNewCategoryEntity(CategoryEntity newCategoryEntity) {
        this.newCategoryEntity = newCategoryEntity;
    }
    
    
}

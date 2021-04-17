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
public class CreateCategoryWithParentReq {
    private String username;
    private String password;
    private CategoryEntity newCategoryEntity;
    private Long parentCategoryId;
    
    public CreateCategoryWithParentReq() {
        
    }
    
    public CreateCategoryWithParentReq(String username, String password, CategoryEntity categoryEntity, Long parentCategoryId) {
        this.username = username;
        this.password = password;
        this.newCategoryEntity = categoryEntity;
        this.parentCategoryId = parentCategoryId;
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

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;
import entity.TagEntity;

/**
 *
 * @author Yuxin
 */
public class CreateTagReq {
    private String username;
    private String password;
    private TagEntity newTagEntity;
    
    public CreateTagReq() {
        
    }
    
    public CreateTagReq(String username, String password, TagEntity tagEntity) {
        this.username = username;
        this.password = password;
        this.newTagEntity = tagEntity;
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

    public TagEntity getNewTagEntity() {
        return newTagEntity;
    }

    public void setNewTagEntity(TagEntity newTagEntity) {
        this.newTagEntity = newTagEntity;
    }

    
}

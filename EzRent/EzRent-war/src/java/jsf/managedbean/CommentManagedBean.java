/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.CommentEntity;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Li Xin
 */
@Named(value = "commentManagedBean")
@ViewScoped
public class CommentManagedBean implements Serializable {

    private CommentEntity commentEntity;

    /**
     * Creates a new instance of CommentManagedBean
     */
    public CommentManagedBean() {
        this.commentEntity = new CommentEntity();
    }

    public CommentEntity getCommentEntity() {
        return commentEntity;
    }

    public void setCommentEntity(CommentEntity commentEntity) {
        this.commentEntity = commentEntity;
    }

}

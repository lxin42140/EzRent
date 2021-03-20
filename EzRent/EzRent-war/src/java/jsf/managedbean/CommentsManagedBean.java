/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CommentEntitySessionBeanLocal;
import entity.CommentEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Li Xin
 */
@Named(value = "commentsManagedBean")
@ViewScoped
public class CommentsManagedBean implements Serializable {

    @Inject
    private CommentManagedBean commentManagedBean;
    @EJB
    private CommentEntitySessionBeanLocal commentEntitySessionBeanLocal;

    /*View all comments for a listing*/
    private List<CommentEntity> commentsForListing;

    /*Create new comment for a listing*/
    private CommentEntity newComment;

    /**
     * Creates a new instance of CommentsManagedBean
     */
    public CommentsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

    }

    public List<CommentEntity> getCommentsForListing() {
        return commentsForListing;
    }

    public void setCommentsForListing(List<CommentEntity> commentsForListing) {
        this.commentsForListing = commentsForListing;
    }

    public CommentManagedBean getCommentManagedBean() {
        return commentManagedBean;
    }

    public void setCommentManagedBean(CommentManagedBean commentManagedBean) {
        this.commentManagedBean = commentManagedBean;
    }

    public CommentEntity getNewComment() {
        return newComment;
    }

    public void setNewComment(CommentEntity newComment) {
        this.newComment = newComment;
    }

}

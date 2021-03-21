/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CommentEntitySessionBeanLocal;
import entity.CommentEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import util.exception.CommentNotFoundException;
import util.exception.CreateNewCommentException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCommentException;
import util.exception.ListingNotFoundException;

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

    public CommentsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

    }

    public void createNewComment(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            ListingEntity listingToComment = (ListingEntity) event.getComponent().getAttributes().get("listingToComment");
            CommentEntity parentComment = (CommentEntity) event.getComponent().getAttributes().get("parentComment");

            if (parentComment != null) {
                this.newComment = commentEntitySessionBeanLocal.createNewComment(listingToComment.getListingId(), customer.getUserId(), parentComment.getCommentId(), newComment);
            } else {
                this.newComment = commentEntitySessionBeanLocal.createNewCommentWithNoParentComment(listingToComment.getListingId(), customer.getUserId(), newComment);
            }
            // add new comment to list
            this.commentsForListing.add(newComment);
            // reset comment
            this.newComment = new CommentEntity();
        } catch (IOException | CommentNotFoundException | CreateNewCommentException | CustomerNotFoundException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while adding comment: " + ex.getMessage(), null));
        }
    }

    public void deleteComment(ActionEvent event) {
        try {
            CommentEntity commentToDelete = (CommentEntity) event.getComponent().getAttributes().get("commentToDelete");
            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

            commentEntitySessionBeanLocal.deleteCommentById(commentToDelete.getCommentId(), customer.getUserId());

            this.commentsForListing.remove(commentToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment deleted!", null));
        } catch (CommentNotFoundException | DeleteCommentException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting comment: " + ex.getMessage(), null));
        }
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

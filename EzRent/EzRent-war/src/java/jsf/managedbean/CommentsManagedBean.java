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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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

    @EJB
    private CommentEntitySessionBeanLocal commentEntitySessionBeanLocal;

    /*View all comments for a listing*/
    private List<CommentEntity> commentsForListing;

    /*Create new comment for a listing*/
    private CommentEntity newComment;
    private ListingEntity listingToComment;
    private String commentMessage;

    //New reply
    private String replyMessage;
    private Boolean showReplyInput;
    private Long commentIdToReply;

    private CustomerEntity customer;

    public CommentsManagedBean() {
        this.newComment = new CommentEntity();
//        this.parentComment = new CommentEntity();
        this.commentMessage = "";
        this.replyMessage = "";
        this.showReplyInput = false;
        this.commentIdToReply = -1l;
    }

    @PostConstruct
    public void postConstruct() {
        this.customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
    }

    public void showReply(ActionEvent event) {
        this.replyMessage = "";
        if (this.commentIdToReply == -1l) {
            this.showReplyInput = !this.showReplyInput;
            this.commentIdToReply = (Long) event.getComponent().getAttributes().get("commentIdToReply");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please cancel the opened reply dialogue!", null));
        }
    }

    public void cancelReply() {
        this.replyMessage = "";
        this.commentIdToReply = -1l;
        this.showReplyInput = false;
    }

    public void createNewComment(ActionEvent event) {
        try {
            if (this.customer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            CommentEntity parentComment = (CommentEntity) event.getComponent().getAttributes().get("parentComment");

            // set timestamp
            this.newComment.setTimeStamp(new Date());

            if (parentComment != null) {
                this.newComment.setMessage(this.replyMessage);
                this.newComment = commentEntitySessionBeanLocal.createNewComment(listingToComment.getListingId(), customer.getUserId(), parentComment.getCommentId(), newComment);
                //remove parent comment from list and add it back to retrieve the replies
                this.commentsForListing.remove(parentComment);
                parentComment = commentEntitySessionBeanLocal.retrieveCommentByCommentId(parentComment.getCommentId());
                this.commentsForListing.add(parentComment);
            } else {
                this.newComment.setMessage(this.commentMessage);
                this.newComment = commentEntitySessionBeanLocal.createNewCommentWithNoParentComment(listingToComment.getListingId(), customer.getUserId(), newComment);
                this.commentsForListing.add(this.newComment);
            }

            // reset comment
            this.newComment = new CommentEntity();
            this.commentMessage = "";
            this.replyMessage = "";
            this.showReplyInput = false;
            this.commentIdToReply = -1l;
        } catch (CommentNotFoundException | CreateNewCommentException | CustomerNotFoundException | ListingNotFoundException | IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteComment(ActionEvent event) {
        try {
            CommentEntity commentToDelete = (CommentEntity) event.getComponent().getAttributes().get("commentToDelete");
            commentEntitySessionBeanLocal.deleteCommentById(commentToDelete.getCommentId(), customer.getUserId());
            this.commentsForListing.remove(commentToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment deleted!", null));
        } catch (CommentNotFoundException | DeleteCommentException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting comment: " + ex.getMessage(), null));
        }
    }

    public void deleteReply(ActionEvent event) {
        try {
            CommentEntity replyToDelete = (CommentEntity) event.getComponent().getAttributes().get("replyToDelete");
            CommentEntity parentComment = (CommentEntity) event.getComponent().getAttributes().get("parentComment");

            commentEntitySessionBeanLocal.deleteCommentById(replyToDelete.getCommentId(), customer.getUserId());

            this.commentsForListing.remove(parentComment);
            parentComment = commentEntitySessionBeanLocal.retrieveCommentByCommentId(parentComment.getCommentId());
            this.commentsForListing.add(parentComment);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reply deleted!", null));
        } catch (CommentNotFoundException | CustomerNotFoundException | DeleteCommentException | NullPointerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting reply: " + ex.getMessage(), null));
        }
    }

    public List<CommentEntity> getCommentsForListing() {
        return commentsForListing;
    }

    public void setCommentsForListing(List<CommentEntity> commentsForListing) {
        this.commentsForListing = commentsForListing;
    }

    public CommentEntity getNewComment() {
        return newComment;
    }

    public void setNewComment(CommentEntity newComment) {
        this.newComment = newComment;
    }

    public void setListingToComment(ListingEntity listingToComment) {
        this.listingToComment = listingToComment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Boolean getShowReplyInput() {
        return showReplyInput;
    }

    public Long getCommentIdToReply() {
        return commentIdToReply;
    }

}

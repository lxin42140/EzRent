/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yuxin
 */
@Entity
public class CommentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    @NotNull
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date timeStamp;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "parentCommentId")
    private CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<CommentEntity> replies;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    //not required for replies
    @JoinColumn(nullable = true, name = "listingId")
    private ListingEntity listing;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "customerId")
    private CustomerEntity sender;

    //do not delete comments that have replies
    private boolean isDeleted;

    public CommentEntity() {
        this.replies = new ArrayList<>();
        this.isDeleted = false;
    }

    public CommentEntity(String message, Date timeStamp, CustomerEntity sender) {
        this();
        this.message = message;
        this.timeStamp = timeStamp;
        this.sender = sender;
    }
    
    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public ListingEntity getListing() {
        return listing;
    }

    public void setListing(ListingEntity listing) {
        this.listing = listing;
    }

    public Long getCommentId() {
        return commentId;
    }

    public CommentEntity getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentEntity parentComment) {
        this.parentComment = parentComment;
    }

    public List<CommentEntity> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentEntity> replies) {
        this.replies = replies;
    }

    public CustomerEntity getSender() {
        return sender;
    }

    public void setSender(CustomerEntity sender) {
        this.sender = sender;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentId != null ? commentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the commentId fields are not set
        if (!(object instanceof CommentEntity)) {
            return false;
        }
        CommentEntity other = (CommentEntity) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CommentEntity[ id=" + commentId + " ]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}

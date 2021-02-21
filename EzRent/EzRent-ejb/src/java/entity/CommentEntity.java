/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    
    private CommentEntity parentComment;
    
    @OneToMany(mappedBy = "comment")
    private List<CommentEntity> replies;

    public CommentEntity() {
    }

    public CommentEntity(String message) {
        this();
        
        this.message = message;
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
    
}

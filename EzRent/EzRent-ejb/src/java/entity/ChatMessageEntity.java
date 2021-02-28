/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yuxin
 */
@Entity
public class ChatMessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date sentDate;

    @Column(nullable = false)
    @NotNull
    private String message;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "conversationId")
    private ConversationEntity conversation;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false, name = "messageSenderId")
    private CustomerEntity messageSender;

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(Date sentDate, String message) {
        this.sentDate = sentDate;
        this.message = message;
    }

    public CustomerEntity getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(CustomerEntity messageSender) {
        this.messageSender = messageSender;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public Long getChatMessageId() {
        return chatMessageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chatMessageId != null ? chatMessageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the chatMessageId fields are not set
        if (!(object instanceof ChatMessageEntity)) {
            return false;
        }
        ChatMessageEntity other = (ChatMessageEntity) object;
        if ((this.chatMessageId == null && other.chatMessageId != null) || (this.chatMessageId != null && !this.chatMessageId.equals(other.chatMessageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ChatMessageEntity[ id=" + chatMessageId + " ]";
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

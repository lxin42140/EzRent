/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author Yuxin
 */
@Entity
public class ConversationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationId;

    @ManyToMany
    @Size(min = 2, max = 2)
    @NotEmpty
    private List<CustomerEntity> chatMembers;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER)
    private List<ChatMessageEntity> chatMessages;

    public ConversationEntity() {
        this.chatMembers = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
    }

    public Long getConversationId() {
        return conversationId;
    }

    public List<ChatMessageEntity> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessageEntity> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<CustomerEntity> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(List<CustomerEntity> chatMembers) {
        this.chatMembers = chatMembers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conversationId != null ? conversationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the conversationId fields are not set
        if (!(object instanceof ConversationEntity)) {
            return false;
        }
        ConversationEntity other = (ConversationEntity) object;
        if ((this.conversationId == null && other.conversationId != null) || (this.conversationId != null && !this.conversationId.equals(other.conversationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ConversationEntity[ id=" + conversationId + " ]";
    }

}

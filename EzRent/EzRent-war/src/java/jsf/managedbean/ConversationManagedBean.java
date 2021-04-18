/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ChatMessageEntitySessionBeanLocal;
import ejb.session.stateless.ConversationEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.ChatMessageEntity;
import entity.ConversationEntity;
import entity.CustomerEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.ConversationNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Ziyue
 */
@Named(value = "conversationManagedBean")
@ViewScoped
public class ConversationManagedBean implements Serializable{

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "ChatMessageEntitySessionBeanLocal")
    private ChatMessageEntitySessionBeanLocal chatMessageEntitySessionBeanLocal;

    @EJB(name = "ConversationEntitySessionBeanLocal")
    private ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal;

    private List<ConversationEntity> conversationEntities;
    private CustomerEntity currentCustomer;
    
    /**
     * Creates a new instance of ConversationManagedBean
     */
    public ConversationManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        setCurrentCustomer((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer"));
        try {
            this.setCurrentCustomer(customerEntitySessionBeanLocal.retrieveCustomerById(getCurrentCustomer().getUserId()));
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Current customer: " + getCurrentCustomer().getUserId());

        this.setConversationEntities(currentCustomer.getConversations());
    }
    
    public void redirectToChatByUser(Long conversationId) throws IOException, ConversationNotFoundException, CustomerNotFoundException {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer") == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
        }

        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("receiverUsername", this.getRecevier(conversationId).getUserName());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/chat/chatPage.xhtml");
        } catch (IOException ex) {
        }
    }
    
    public CustomerEntity getRecevier(Long conversationId) throws ConversationNotFoundException, CustomerNotFoundException {
        return conversationEntitySessionBeanLocal.getReceiverCustomer(conversationId, this.currentCustomer.getUserId());
    }

    public ChatMessageEntity getLatestChatMessage(Long conversationId) throws ConversationNotFoundException {
        ConversationEntity conversation = conversationEntitySessionBeanLocal.retrieveConversationByConversationId(conversationId);
        ChatMessageEntity latestMessage = conversation.getChatMessages().get(conversation.getChatMessages().size() - 1);
        return latestMessage;
    }
    
    public String getLatestChatMessageToString(Long conversationId) throws ConversationNotFoundException {
        ChatMessageEntity message = this.getLatestChatMessage(conversationId);
        return "Latest Message Sent By: " + message.getMessageSender().getUserName() + "  Message: " + message.getMessage();
    }
    
    public List<ConversationEntity> getConversationEntities() {
        return conversationEntities;
    }

    public void setConversationEntities(List<ConversationEntity> conversationEntities) {
        this.conversationEntities = conversationEntities;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    
    
    
}

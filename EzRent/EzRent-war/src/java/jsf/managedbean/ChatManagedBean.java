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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import util.exception.ConversationNotFoundException;
import util.exception.CreateNewChatMessageException;
import util.exception.CreateNewConversationException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Ziyue
 */
@Named(value = "chatManagedBean")
@SessionScoped
public class ChatManagedBean implements Serializable {

    @EJB(name = "ChatMessageEntitySessionBeanLocaal")
    private ChatMessageEntitySessionBeanLocal chatMessageEntitySessionBeanLocal;

    @EJB(name = "ConversationEntitySessionBeanLocal")
    private ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    
    
    @Inject @Push(channel = "channel2")
    private PushContext pushContext;
    
    private CustomerEntity receiver;
    private CustomerEntity sender;
    //private List<String> messages;
    private ConversationEntity conversation;
    private String channelName;    
    private String message;
    private List<CustomerEntity> users;     

    

    
    
    public ChatManagedBean() {
        channelName = "channel2";
//        messages = new ArrayList<>();
//        messages.add("test1");
//        messages.add("test2");
//        messages.add("test1");
//        messages.add("test2");
//        messages.add("test1");
//        messages.add("test2");
//        messages.add("test1");
//        messages.add("test2");
        users = new ArrayList<>();
        
    }
    
    @PostConstruct
    public void postConstruct() {
        //for debugging
//        try {
//            this.setReceiver(customerEntitySessionBeanLocal.retrieveCustomerById(2l));
//            this.setSender(customerEntitySessionBeanLocal.retrieveCustomerById(3l));
//        } catch (CustomerNotFoundException ex) {
//            Logger.getLogger(ChatManagedBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
        System.out.println("" + receiver + "      " + sender);
//        
        String receiverUser = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("receiverUsername");
        if (receiverUser != null) {
            try {
                this.setReceiver(customerEntitySessionBeanLocal.retrieveCustomerByUsername(receiverUser.toLowerCase().trim()));
                this.setSender((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer"));
            } catch (CustomerNotFoundException ex) {
                System.out.println(ex.getMessage() + "Chat Customer not Found");
            }
        } else {
            System.out.println("Receiver Username is null");
        }
        getUsers().add(sender);
        getUsers().add(receiver);
//        try {
//            if (conversationEntitySessionBeanLocal.retrieveAllConversationsBySenderReceiver(sender.getUserId(), receiver.getUserId()) != null) {
//                this.setConversation(conversationEntitySessionBeanLocal.retrieveAllConversationsBySenderReceiver(sender.getUserId(), receiver.getUserId()));        
//            }
//        } catch (ConversationNotFoundException ex) {
//            System.out.println(ex.getMessage());
//        }

        for (ConversationEntity currConversation : sender.getConversations()) {
            if (currConversation.getChatMembers().contains(receiver)) {
                this.conversation = currConversation;
                break;
            }
        }

        System.out.println("" + receiver + "      " + sender + "++");
//      
    }
    
    public void doSendPush(ActionEvent event)
    {
        System.out.println("********** SendPushManagedBean.doSendPush(): sender: " + sender.getUserName() + "; receiver: " + receiver.getUserName() + "; message: " + message);
        
        try {
            doUpdateConversation();        
        } catch (CustomerNotFoundException | CreateNewConversationException | CreateNewChatMessageException | ConversationNotFoundException ex) {
            System.out.println(ex.getMessage() + "");
        }
        
//        try {
//            ConversationEntity currConversation = conversationEntitySessionBeanLocal.retrieveAllConversationsBySenderReceiver(sender.getUserId(), receiver.getUserId());
//            conversationId = currConversation.getConversationId();
//        } catch(ConversationNotFoundException ex) {
//            System.out.println(ex.getMessage());
//            ConversationEntity newConversation = new ConversationEntity();
//            conversationId = conversationEntitySessionBeanLocal.createNewConversation(sender.getUserId(), receiver.getUserId(), newConversation);
//        }
        
        
        try
        {
            //Set<Future<Void>> sets = pushContext.send(message);
            pushContext.send("updateEvent", getUsers());
            //System.out.println(sets.isEmpty());
            //messages.add(message);
            message = "";
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void getLatestConversation(AjaxBehaviorEvent event) throws ConversationNotFoundException {
        Long conversationId = 0l;
        
        for (ConversationEntity currConversation : sender.getConversations()) {
            if (currConversation.getChatMembers().contains(receiver)) {
                conversationId = currConversation.getConversationId();
                System.out.println(conversationId + "here ++++");
                break;
            }
        }
        
        this.conversation = conversationEntitySessionBeanLocal.retrieveConversationByConversationId(conversationId);
    }
    
    private void doUpdateConversation() throws CustomerNotFoundException, CreateNewConversationException, CreateNewChatMessageException, ConversationNotFoundException {
        
        Long conversationId = 0l;
        
        for (ConversationEntity currConversation : sender.getConversations()) {
            if (currConversation.getChatMembers().contains(receiver)) {
                conversationId = currConversation.getConversationId();
                System.out.println(conversationId + "here ++++");
                break;
            }
        }
        
        if (this.conversation == null) {
            ConversationEntity newConversation = new ConversationEntity();
            conversationId = conversationEntitySessionBeanLocal.createNewConversation(sender.getUserId(), receiver.getUserId(), newConversation);        
        }
        
        ChatMessageEntity chatMessage = new ChatMessageEntity(new Date(), message);
        chatMessageEntitySessionBeanLocal.createnewChatMessage(conversationId, sender.getUserId(), chatMessage);
        this.setConversation(conversationEntitySessionBeanLocal.retrieveConversationByConversationId(conversationId));


    }
    
    

    public CustomerEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(CustomerEntity receiver) {
        this.receiver = receiver;
    }

    public CustomerEntity getSender() {
        return sender;
    }

    public void setSender(CustomerEntity sender) {
        this.sender = sender;
    }

    

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomerEntity> getUsers() {
        return users;
    }

    public void setUsers(List<CustomerEntity> users) {
        this.users = users;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    
}

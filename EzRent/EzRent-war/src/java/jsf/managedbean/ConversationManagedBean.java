/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ChatMessageEntitySessionBeanLocal;
import ejb.session.stateless.ConversationEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.ConversationEntity;
import entity.CustomerEntity;
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
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        try {
            this.currentCustomer = customerEntitySessionBeanLocal.retrieveCustomerById(currentCustomer.getUserId());
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Current customer: " + currentCustomer.getUserId());

        conversationEntities = conversationEntitySessionBeanLocal.retrieveAllConversationsByCustomer(currentCustomer.getUserId());

    }
    
}

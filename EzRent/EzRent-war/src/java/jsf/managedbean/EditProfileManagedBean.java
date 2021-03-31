/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CustomerNotFoundException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author Yuxin
 */
@Named(value = "editProfileManagedBean")
@ViewScoped
public class EditProfileManagedBean implements Serializable{

    @EJB(name = "CreditCardEntitySessionBeanLocal")
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    
    private CustomerEntity currentCustomer;
     
    @Inject
    private CreditCardManagedBean creditCardManagedBean;
    
    private Boolean disabledEditing;
    
    public EditProfileManagedBean() {  
    }
    
    @PostConstruct
    public void postConstruct() {
        disabledEditing = true;
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
    }
    
    public void updateCustomerDetails(ActionEvent event) {
        try {
            Long currentCustId = customerEntitySessionBeanLocal.updateCustomerProfileDetails(currentCustomer.getUserId(), currentCustomer);
            editButton();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully (Customer ID: " + currentCustId + ")", null));
        } catch(CustomerNotFoundException | UpdateCustomerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating: " + ex.getMessage(), null));
        }
    }
    
    public void editButton() {
        this.disabledEditing = !this.disabledEditing;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public Boolean getDisabledEditing() {
        return disabledEditing;
    }

    public void setDisabledEditing(Boolean disabledEditing) {
        this.disabledEditing = disabledEditing;
    }

    public CreditCardManagedBean getCreditCardManagedBean() {
        return creditCardManagedBean;
    }

    public void setCreditCardManagedBean(CreditCardManagedBean creditCardManagedBean) {
        this.creditCardManagedBean = creditCardManagedBean;
    }

    
    
}

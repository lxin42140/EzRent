/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author Yuxin
 */
@Named(value = "creditCardManagedBean")
@ViewScoped
public class CreditCardManagedBean implements Serializable {

    @EJB(name = "CreditCardEntitySessionBeanLocal")
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;

    private CustomerEntity currentCustomer;

    private List<CreditCardEntity> customerCCs;

    private CreditCardEntity newCreditCard;

    private CreditCardEntity selectedCreditCardToUpdate;

    public CreditCardManagedBean() {
        newCreditCard = new CreditCardEntity();
    }

    @PostConstruct
    public void postConstruct() {
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        customerCCs = creditCardEntitySessionBeanLocal.retrieveCreditCardsByCustomerId(currentCustomer.getUserId());

    }

    public void addNewCreditCard(ActionEvent event) {
        try {
            Long newCreditCardId = creditCardEntitySessionBeanLocal.createNewCreditCard(currentCustomer.getUserId(), newCreditCard);
            customerCCs.add(newCreditCard);
            newCreditCard = new CreditCardEntity();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New credit card added successfully (Credit Card ID: " + newCreditCardId + ")", null));
        } catch (CreateNewCreditCardException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while adding the new credit card: " + ex.getMessage(), null));

        }
    }

    public void doUpdateCreditCard(ActionEvent event) {
        selectedCreditCardToUpdate = (CreditCardEntity) event.getComponent().getAttributes().get("creditCardEntityToUpdate");
        System.out.println("DO UPDATE CREDIT CARD ----- " + selectedCreditCardToUpdate.getCreditCardId());

    }

    public void updateCreditCard(ActionEvent event) {
        try {
            Long updatedCreditCardId = creditCardEntitySessionBeanLocal.updateCreditCardDetails(selectedCreditCardToUpdate.getCreditCardId(), selectedCreditCardToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit card updated successfully (Credit Card ID: " + updatedCreditCardId + ")", null));
        } catch (CreditCardNotFoundException | UpdateCreditCardException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while editing credit card: " + ex.getMessage(), null));
        }
    }
    
    public void deleteCreditCard(ActionEvent event) {
        CreditCardEntity creditCardToDelete = (CreditCardEntity)event.getComponent().getAttributes().get("creditCardEntityToDelete");
        try{
            creditCardEntitySessionBeanLocal.deleteCreditCard(creditCardToDelete.getCreditCardId());
            customerCCs.remove(creditCardToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit card deleted successfully" , null));
        } catch(CreditCardNotFoundException | DeleteCreditCardException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting credit card: " + ex.getMessage(), null));
        }
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<CreditCardEntity> getCustomerCCs() {
        return customerCCs;
    }

    public void setCustomerCCs(List<CreditCardEntity> customerCCs) {
        this.customerCCs = customerCCs;
    }

    public CreditCardEntity getNewCreditCard() {
        return newCreditCard;
    }

    public void setNewCreditCard(CreditCardEntity newCreditCard) {
        this.newCreditCard = newCreditCard;
    }

    public CreditCardEntity getSelectedCreditCardToUpdate() {
        return selectedCreditCardToUpdate;
    }

    public void setSelectedCreditCardToUpdate(CreditCardEntity selectedCreditCardToUpdate) {
        this.selectedCreditCardToUpdate = selectedCreditCardToUpdate;
    }

}

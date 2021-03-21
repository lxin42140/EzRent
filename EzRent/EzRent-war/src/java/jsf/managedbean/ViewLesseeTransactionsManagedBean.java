/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.OfferEntity;
import entity.TransactionEntity;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.OfferNotFoundException;
import util.exception.PaymentNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateOfferException;
import util.exception.UpdatePaymentFailException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author kiyon
 */
@Named(value = "viewLesseeTransactionsManagedBean")
@ViewScoped
public class ViewLesseeTransactionsManagedBean implements Serializable {

    @EJB
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;
    
    private CustomerEntity customer;

    private List<OfferEntity> pendingOffersMade;
    private List<TransactionEntity> transactions;
    
    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;

    /**
     * Creates a new instance of ViewLesseeTransactionsManagedBean
     */
    public ViewLesseeTransactionsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity") != null) {
            customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customer.getUserId()));
        } else {
            customer = new CustomerEntity();
            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(1l));
        }

//        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
//        setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customer.getUserId()));

        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactions());
        Iterator<TransactionEntity> iterator = getTransactions().iterator();
        while (iterator.hasNext()) {
            TransactionEntity transaction = iterator.next();
            if (!transaction.getOffer().getListing().getLessor().getUserId().equals(customer.getUserId())) {
                getTransactions().remove(transaction);
            }
        }
    }
    
    public void selectOffer(ActionEvent event) {
        setSelectedOffer((OfferEntity) event.getComponent().getAttributes().get("selectedOffer"));
    }
    
    public void deleteOwnOffer(ActionEvent event) {
        try {
            offerEntitySessionBeanLocal.cancelOffer(selectedOffer.getOfferId());
            
            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customer.getUserId()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Offer has been cancelled!", null));
        } catch (OfferNotFoundException | UpdateOfferException | UpdateTransactionStatusException | TransactionNotFoundException | PaymentNotFoundException | UpdatePaymentFailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to cancel offer! " + ex.getMessage(), null));
        }
        
    }

    /**
     * @return the pendingOffersMade
     */
    public List<OfferEntity> getPendingOffersMade() {
        return pendingOffersMade;
    }

    /**
     * @param pendingOffersMade the pendingOffersMade to set
     */
    public void setPendingOffersMade(List<OfferEntity> pendingOffersMade) {
        this.pendingOffersMade = pendingOffersMade;
    }

    /**
     * @return the transactions
     */
    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return the selectedOffer
     */
    public OfferEntity getSelectedOffer() {
        return selectedOffer;
    }

    /**
     * @param selectedOffer the selectedOffer to set
     */
    public void setSelectedOffer(OfferEntity selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    /**
     * @return the selectedTransaction
     */
    public TransactionEntity getSelectedTransaction() {
        return selectedTransaction;
    }

    /**
     * @param selectedTransaction the selectedTransaction to set
     */
    public void setSelectedTransaction(TransactionEntity selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    
}

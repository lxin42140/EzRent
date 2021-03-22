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
import util.exception.CreateNewTransactionException;
import util.exception.OfferNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateOfferException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author kiyon
 */
@Named(value = "viewLessorTransactionsManagedBean")
@ViewScoped
public class ViewLessorTransactionsManagedBean implements Serializable {

    @EJB
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private List<OfferEntity> offersFromCustomers;
    private List<TransactionEntity> transactions;

    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;

    /**
     * Creates a new instance of ViewLessorTransactionsManagedBean
     */
    public ViewLessorTransactionsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

        setOffersFromCustomers(offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(2l));

//        CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
//        setOffersFromCustomers(offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(customer.getUserId()));
        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactions());
        Iterator<TransactionEntity> iterator = getTransactions().iterator();
        while (iterator.hasNext()) {
            TransactionEntity transaction = iterator.next();
//            if (!transaction.getOffer().getListing().getListingOwner().getUserId().equals(customer.getUserId())) {
            if (!transaction.getOffer().getListing().getListingOwner().getUserId().equals(2l)) {
                getTransactions().remove(transaction);
            }
        }
    }

    public void selectOffer(ActionEvent event) {
        setSelectedOffer((OfferEntity) event.getComponent().getAttributes().get("selectedOffer"));
    }

    public void acceptOffer(ActionEvent event) {
        try {
            offerEntitySessionBeanLocal.acceptOffer(selectedOffer.getOfferId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has been marked as completed!", null));
        } catch (UpdateOfferException | OfferNotFoundException | CreateNewTransactionException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void selectTransaction(ActionEvent event) {
        setSelectedTransaction((TransactionEntity) event.getComponent().getAttributes().get("selectedTransaction"));
    }

    public void completeTransaction(ActionEvent event) {
        try {
            transactionEntitySessionBeanLocal.markTransactionCompleted(selectedTransaction.getTransactionId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has been marked as completed!", null));
        } catch (TransactionNotFoundException | UpdateTransactionStatusException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    /**
     * @return the offersFromCustomers
     */
    public List<OfferEntity> getOffersFromCustomers() {
        return offersFromCustomers;
    }

    /**
     * @param offersFromCustomers the offersFromCustomers to set
     */
    public void setOffersFromCustomers(List<OfferEntity> offersFromCustomers) {
        this.offersFromCustomers = offersFromCustomers;
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

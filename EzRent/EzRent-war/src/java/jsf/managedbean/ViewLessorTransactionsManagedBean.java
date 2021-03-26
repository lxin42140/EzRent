/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.PaymentEntitySessionBeanLocal;
import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.DeliveryEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.PaymentEntity;
import entity.TransactionEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.DeliveryStatusEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewPaymentException;
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
    
    @EJB
    private PaymentEntitySessionBeanLocal paymentEntitySessionBeanLocal;

    private List<OfferEntity> offersFromCustomers;
    private List<TransactionEntity> transactions;

    private Long customerId;
    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;
    
    /**
     * Creates a new instance of ViewLessorTransactionsManagedBean
     */
    public ViewLessorTransactionsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

        this.offersFromCustomers = offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(customerId);

        this.customerId = ((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getUserId();
        setOffersFromCustomers(offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(customerId));

//        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(2l));
        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(customerId));
    }

    public void acceptOffer(ActionEvent event) {
        try {
            this.selectedOffer = (OfferEntity) event.getComponent().getAttributes().get("selectedOffer");
            Long transactionId = offerEntitySessionBeanLocal.acceptOffer(selectedOffer.getOfferId());
            
//            this.offersFromCustomers = offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(2l);
            this.offersFromCustomers = offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(customerId);

            //if COD, straight away create payment
            if (selectedOffer.getListing().getModeOfPayment() == ModeOfPaymentEnum.CASH_ON_DELIVERY) {
                PaymentEntity codPayment = new PaymentEntity(null, new BigDecimal(selectedOffer.getListing().getPrice()));
                codPayment.setModeOfPayment(ModeOfPaymentEnum.CASH_ON_DELIVERY);
                
                paymentEntitySessionBeanLocal.createNewCashPayment(codPayment, transactionId);
            }
            
            setOffersFromCustomers(offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(2l));
//            setOffersFromCustomers(offerEntitySessionBeanLocal.retrieveAllPendingOffersByListingOwners(customer.getUserId()));
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You have accepted this offer!", null));
        } catch (UpdateOfferException | OfferNotFoundException | CreateNewTransactionException | TransactionNotFoundException | CreateNewPaymentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    public String retrieveStatus(ActionEvent event, TransactionEntity transaction) {
        ListingEntity listing = transaction.getOffer().getListing();
        PaymentEntity payment = transaction.getPayment();
        
        TransactionStatusEnum transactionStatus = transaction.getTransactionStatus();
        
        if (transactionStatus == TransactionStatusEnum.COMPLETED || transactionStatus == TransactionStatusEnum.RECEIVED) {
            return transactionStatus.toString();
        }
        
        
        if (listing.getDeliveryOption() == DeliveryOptionEnum.MEETUP) { //meetup
            if (listing.getModeOfPayment() == ModeOfPaymentEnum.CASH_ON_DELIVERY) { //cash on delivery
                return "PENDING MEETUP";
            } else { //credit card
                if (payment != null && payment.getPaymentStatus() == PaymentStatusEnum.PAID) {
                    return "PENDING MEETUP";
                } else {
                    return "PENDING PAYMENT FROM CUSTOMER";
                }
            }
        } else { //delivery
            DeliveryEntity delivery = transaction.getDelivery();
                if (listing.getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD) { //cash on delivery
                    if (transaction.getPayment() == null || transaction.getPayment().getPaymentStatus() == PaymentStatusEnum.UNPAID) {
                        return "PENDING PAYMENT FROM CUSTOMER";
                    } else {
                        return delivery.getDeliveryStatus().toString();
                    }
                } else {
                    if (delivery == null) { //havent confirm customer address
                        return "PENDING CONFIRMATION FROM CUSTOMER";
                    } else {
                        return delivery.getDeliveryStatus().toString();
                    }
                }
        }
    }
    
//    public void directToChat(ActionEvent event) {
//        FacesContext.getCurrentInstance().getExternalContext().redirect("chat.xhtml");
//    }

    public void completeTransaction(ActionEvent event) {
        try {
            this.selectedTransaction = (TransactionEntity) event.getComponent().getAttributes().get("selectedTransaction");
            transactionEntitySessionBeanLocal.markTransactionCompleted(selectedTransaction.getTransactionId());
            
//            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(2l));
            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(customerId));
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has been marked as completed! You can view this transaction under profile.", null));
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

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    
}

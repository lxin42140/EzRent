/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.PaymentEntitySessionBeanLocal;
import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.CreditCardEntity;
import entity.DeliveryEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.PaymentEntity;
import entity.TransactionEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
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
import util.exception.CreateNewDeliveryException;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeliveryCompanyNotFoundException;
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
    @EJB
    private PaymentEntitySessionBeanLocal paymentEntitySessionBeanLocal;
    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;

    //private CustomerEntity customer;
    private List<OfferEntity> pendingOffersMade;
    private List<TransactionEntity> transactions;

    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;

    //temp attribute to make payment, needs to be changed.
    private Long creditCardId;
    private String newDeliveryComment;
    private String newStreetName;
    private String newPostalCode;

    /**
     * Creates a new instance of ViewLesseeTransactionsManagedBean
     */
    public ViewLesseeTransactionsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {

        setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(3l));

//        customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomerEntity");
//        setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customer.getUserId()));
        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(3l));

    }

    public void deleteOwnOffer(ActionEvent event) {
        try {
            setSelectedOffer((OfferEntity) event.getComponent().getAttributes().get("selectedOffer"));
            offerEntitySessionBeanLocal.cancelOffer(selectedOffer.getOfferId());

            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(3l));
//            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customer.getUserId()));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Offer has been cancelled!", null));
        } catch (OfferNotFoundException | UpdateOfferException | UpdateTransactionStatusException | TransactionNotFoundException | PaymentNotFoundException | UpdatePaymentFailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to cancel offer! " + ex.getMessage(), null));
        }

    }

//    public void directToChat(ActionEvent event) {
//        FacesContext.getCurrentInstance().getExternalContext().redirect("chat.xhtml");
//    }

    public void setTransaction(ActionEvent event) {
        setSelectedTransaction((TransactionEntity) event.getComponent().getAttributes().get("selectedTransaction"));

        if (selectedTransaction.getOffer().getCustomer().getCreditCards().size() > 0) {
            this.creditCardId = selectedTransaction.getOffer().getCustomer().getCreditCards().get(0).getCreditCardId();
        }
        this.setNewStreetName(selectedTransaction.getOffer().getCustomer().getStreetName());
        this.setNewPostalCode(selectedTransaction.getOffer().getCustomer().getPostalCode());

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
                    return "PENDING PAYMENT";
                }
            }
        } else { //delivery
            DeliveryEntity delivery = transaction.getDelivery();
            if (listing.getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD) { //credit card
                if (transaction.getPayment() == null || transaction.getPayment().getPaymentStatus() == PaymentStatusEnum.UNPAID) {
                    return "PENDING PAYMENT";
                } else {
                    return delivery.getDeliveryStatus().toString();
                }
            } else { //COD
                if (delivery == null) { //havent confirm customer address
                    return "PENDING ADDRESS CONFIRMATION";
                } else {
                    return delivery.getDeliveryStatus().toString();
                }
            }
        }
    }

    public void makePaymentAndDelivery(ActionEvent event) {
        try {
            if (selectedTransaction.getOffer().getListing().getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD) {
                makeCreditCardPayment();
            }

            if (selectedTransaction.getOffer().getListing().getDeliveryOption() == DeliveryOptionEnum.DELIVERY) {
                makeDelivery();
            }
            
            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(3l));
        } catch (CreateNewPaymentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to make payment! " + ex.getMessage(), null));
        } catch (CreateNewDeliveryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to create delivery! " + ex.getMessage(), null));
        }
    }

    public void makeCreditCardPayment() throws CreateNewPaymentException {
        try {
            PaymentEntity ccPayment = new PaymentEntity(new Date(), new BigDecimal(selectedTransaction.getOffer().getListing().getPrice()));

            paymentEntitySessionBeanLocal.createNewCreditCardPayement(ccPayment, creditCardId, selectedTransaction.getTransactionId());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment has been made!", null));
        } catch (CreditCardNotFoundException | UpdateTransactionStatusException | TransactionNotFoundException | CreateNewPaymentException ex) {
            throw new CreateNewPaymentException("CreateNewPaymentException: " + ex.getMessage());
        }
    }

    private void makeDelivery() throws CreateNewDeliveryException {
        try {
            DeliveryEntity newDelivery = new DeliveryEntity();

            //TODO: DeliveryCompany should change this from PENDING to DELIVERING
            newDelivery.setDeliveryStatus(DeliveryStatusEnum.DELIVERING);

            //TODO: Delivery company should be set by the system instead.
            newDelivery.setDeliveryComment(newDeliveryComment);
            newDelivery.setLastUpateDate(new Date());

            deliveryEntitySessionBeanLocal.createNewDelivery(newDelivery, selectedTransaction.getTransactionId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delivery company is now delivering the item!", null));
        } catch (DeliveryCompanyNotFoundException | CreateNewDeliveryException | TransactionNotFoundException ex) {
            throw new CreateNewDeliveryException("CreateNewDeliveryException: " + ex.getMessage());
        }
    }

    public void markTransactionAsReceived(ActionEvent event) {
        try {
            this.selectedTransaction = (TransactionEntity) event.getComponent().getAttributes().get("selectedTransaction");
            transactionEntitySessionBeanLocal.markTransactionReceived(selectedTransaction.getTransactionId());

            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(3l));
//            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(customer.getUserId()));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has been marked as received! You can view this transaction under profile.", null));
        } catch (TransactionNotFoundException | UpdateTransactionStatusException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
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

    /**
     * @return the creditCardId
     */
    public Long getCreditCardId() {
        return creditCardId;
    }

    /**
     * @param creditCardId the creditCardId to set
     */
    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    /**
     * @return the newDeliveryComment
     */
    public String getNewDeliveryComment() {
        return newDeliveryComment;
    }

    /**
     * @param newDeliveryComment the newDeliveryComment to set
     */
    public void setNewDeliveryComment(String newDeliveryComment) {
        this.newDeliveryComment = newDeliveryComment;
    }

    /**
     * @return the newStreetName
     */
    public String getNewStreetName() {
        return newStreetName;
    }

    /**
     * @param newStreetName the newStreetName to set
     */
    public void setNewStreetName(String newStreetName) {
        this.newStreetName = newStreetName;
    }

    /**
     * @return the newPostalCode
     */
    public String getNewPostalCode() {
        return newPostalCode;
    }

    /**
     * @param newPostalCode the newPostalCode to set
     */
    public void setNewPostalCode(String newPostalCode) {
        this.newPostalCode = newPostalCode;
    }

}

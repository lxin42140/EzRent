/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import util.exception.CreateNewOfferException;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.ListingNotFoundException;
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

    private Long customerId;
    private List<OfferEntity> pendingOffersMade;
    private List<TransactionEntity> transactions;

    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;
    
    private Date newOfferRentalStartDate;
    private Date newOfferRentalEndDate;

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

        if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
            this.setCustomerId(((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getUserId());
        }
        setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customerId));
        
        setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(customerId));
    }
    
    public String displayTime(Date date) {
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormat.format(date);
    }
    
    public void makeOffer() throws CreateNewOfferException {
        OfferEntity newOffer = new OfferEntity();
        newOffer.setDateOffered(new Date());
        newOffer.setRentalStartDate(newOfferRentalStartDate);
        newOffer.setRentalEndDate(newOfferRentalEndDate);
        
        ListingEntity listing = (ListingEntity) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("listingToOffer");
        try {
            offerEntitySessionBeanLocal.createNewOffer(newOffer, customerId, listing.getListingId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Offer successfully created!", null));
        } catch (CreateNewOfferException | CustomerNotFoundException | ListingNotFoundException ex) {
            throw new CreateNewOfferException("CreateNewOfferException: " + ex.getMessage());
        }
    }

    public void deleteOwnOffer(ActionEvent event) {
        if (customerId == null) {
            this.setCustomerId(((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getUserId());
        }
        try {
            setSelectedOffer((OfferEntity) event.getComponent().getAttributes().get("selectedOffer"));
            offerEntitySessionBeanLocal.cancelOffer(selectedOffer.getOfferId());

            setPendingOffersMade(offerEntitySessionBeanLocal.retrieveAllPendingOffersByCustomer(customerId));
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
        if (customerId == null) {
            this.setCustomerId(((CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer")).getUserId());
        }
        try {
            if (selectedTransaction != null && selectedTransaction.getOffer().getListing().getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD) {
                makeCreditCardPayment();
            }

            if (selectedTransaction != null && selectedTransaction.getOffer().getListing().getDeliveryOption() == DeliveryOptionEnum.MAIL) {
                makeDelivery();
            }
            
            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(customerId));
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

//            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(3l));
            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByLessorId(customerId));

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

    /**
     * @return the customer
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

    /**
     * @return the newOfferRentalStartDate
     */
    public Date getNewOfferRentalStartDate() {
        return newOfferRentalStartDate;
    }

    /**
     * @param newOfferRentalStartDate the newOfferRentalStartDate to set
     */
    public void setNewOfferRentalStartDate(Date newOfferRentalStartDate) {
        this.newOfferRentalStartDate = newOfferRentalStartDate;
    }

    /**
     * @return the newOfferRentalEndDate
     */
    public Date getNewOfferRentalEndDate() {
        return newOfferRentalEndDate;
    }

    /**
     * @param newOfferRentalEndDate the newOfferRentalEndDate to set
     */
    public void setNewOfferRentalEndDate(Date newOfferRentalEndDate) {
        this.newOfferRentalEndDate = newOfferRentalEndDate;
    }


    
}

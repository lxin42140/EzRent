/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.PaymentEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.DeliveryEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.PaymentEntity;
import entity.ReviewEntity;
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
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewOfferException;
import util.exception.CreateNewPaymentException;
import util.exception.CreateNewReviewException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
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
    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;
    @EJB
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    private Long customerId;
    private List<OfferEntity> pendingOffersMade;
    private List<TransactionEntity> transactions;

    private OfferEntity selectedOffer;
    private TransactionEntity selectedTransaction;
    private List<TransactionEntity> completedTransactions;

    private Date newOfferRentalStartDate;
    private Date newOfferRentalEndDate;

    //temp attribute to make payment, needs to be changed.
    private Long creditCardId;
    private List<CreditCardEntity> creditCards;

    private Integer ratingNumber;
    private String ratingDescription;

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

        setCompletedTransactions(transactionEntitySessionBeanLocal.retrieveAllCompletedTransactionsByCustomerId(customerId));
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
        this.creditCards = creditCardEntitySessionBeanLocal.retrieveCreditCardsByCustomerId(customerId);
        if (this.creditCards.size() > 0) {
            this.creditCardId = selectedTransaction.getOffer().getCustomer().getCreditCards().get(0).getCreditCardId();
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
                    return "PENDING PAYMENT";
                }
            }
        } else { //delivery
            DeliveryEntity delivery = transaction.getDelivery();
            if (listing.getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD) { //credit card
                if (transaction.getPayment() == null || transaction.getPayment().getPaymentStatus() == PaymentStatusEnum.UNPAID) {
                    return "PENDING PAYMENT";
                } else {
                    if (delivery == null) {
                        return "PENDING DELIVERY";
                    } else {
                        return delivery.getDeliveryStatus().toString();
                    }
                }
            } else { //COD
                if (delivery == null) {
                    return "PENDING DELIVERY";
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

            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(customerId));
        } catch (CreateNewPaymentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to make payment! " + ex.getMessage(), null));
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

    public void markTransactionAsReceived(ActionEvent event) {
        try {
            this.selectedTransaction = (TransactionEntity) event.getComponent().getAttributes().get("selectedTransaction");
            transactionEntitySessionBeanLocal.markTransactionReceived(selectedTransaction.getTransactionId());

            setTransactions(transactionEntitySessionBeanLocal.retrieveAllActiveTransactionsByCustomerId(customerId));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction has been marked as received!", null));
        } catch (TransactionNotFoundException | UpdateTransactionStatusException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public boolean checkReviews(TransactionEntity checkedTransaction) {
        for (ReviewEntity review : checkedTransaction.getReviews()) {
            if (review.getCustomer().getUserId().equals(customerId)) {
                return false;
            }
        }
        return true;
    }

    public void submitReview(ActionEvent event) {
        ReviewEntity review = new ReviewEntity(getRatingDescription(), getRatingNumber());
        try {
            reviewEntitySessionBeanLocal.createNewReview(customerId, selectedTransaction.getTransactionId(), review);

            setRatingDescription(null);
            setRatingNumber(null);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Review has been posted successfully!", null));
        } catch (CreateNewReviewException | TransactionNotFoundException | CustomerNotFoundException ex) {
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

    /**
     * @return the creditCards
     */
    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    /**
     * @param creditCards the creditCards to set
     */
    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * @return the ratingNumber
     */
    public Integer getRatingNumber() {
        return ratingNumber;
    }

    /**
     * @param ratingNumber the ratingNumber to set
     */
    public void setRatingNumber(Integer ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    /**
     * @return the ratingDescription
     */
    public String getRatingDescription() {
        return ratingDescription;
    }

    /**
     * @param ratingDescription the ratingDescription to set
     */
    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    /**
     * @return the completedTransactions
     */
    public List<TransactionEntity> getCompletedTransactions() {
        return completedTransactions;
    }

    /**
     * @param completedTransactions the completedTransactions to set
     */
    public void setCompletedTransactions(List<TransactionEntity> completedTransactions) {
        this.completedTransactions = completedTransactions;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OfferEntity;
import entity.TransactionEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.DeliveryStatusEnum;
import util.enumeration.OfferStatusEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewTransactionException;
import util.exception.OfferNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateTransactionStatusException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class TransactionEntitySessionBean implements TransactionEntitySessionBeanLocal {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    //transaction is created first, followed be dependent entities such as payment and delivery
    //this is to remove cyclic dependency
    @Override
    public Long createNewTransaction(Long offerId, TransactionEntity newTransaction) throws CreateNewTransactionException, OfferNotFoundException {
        if (offerId == null || newTransaction == null) {
            throw new CreateNewTransactionException("CreateNewTransactionException: Please provide valid transaction/offer Id!");
        }

        OfferEntity offer = offerEntitySessionBeanLocal.retrieveOfferByOfferId(offerId);
        //bi-asso with transaction
        offer.setTransaction(newTransaction);
        newTransaction.setOffer(offer);

        try {
            validate(newTransaction);
            em.persist(newTransaction);
            em.flush();
            return newTransaction.getTransactionId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewTransactionException("CreateNewTransactionException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewTransactionException("CreateNewTransactionException: Transaction with same ID already exists!");
            } else {
                throw new CreateNewTransactionException("CreateNewTransactionException: " + ex.getMessage());
            }
        }
    }

    @Override
    //retrieving all non-cancelled transactions
    public List<TransactionEntity> retrieveAllActiveTransactions() {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t WHERE NOT t.transactionStatus =:status");
        query.setParameter("status", TransactionStatusEnum.CANCELLED);

        return query.getResultList();
    }

    @Override
    public List<TransactionEntity> retrieveAllActiveTransactionsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t WHERE t.offer.customer.userId =:incustomerId and NOT t.transactionStatus =:firstStatus and NOT t.transactionStatus =:secondStatus");
        query.setParameter("firstStatus", TransactionStatusEnum.CANCELLED);
        query.setParameter("secondStatus", TransactionStatusEnum.COMPLETED);
        query.setParameter("incustomerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<TransactionEntity> retrieveAllActiveTransactionsByLessorId(Long lessorId) {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t WHERE t.offer.listing.listingOwner.userId =:inLessorId and NOT t.transactionStatus =:firstStatus and NOT t.transactionStatus =:secondStatus");
        query.setParameter("firstStatus", TransactionStatusEnum.CANCELLED);
        query.setParameter("secondStatus", TransactionStatusEnum.COMPLETED);
        query.setParameter("inLessorId", lessorId);
        return query.getResultList();
    }

    @Override
    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException {
        if (transactionId == null) {
            throw new TransactionNotFoundException("TransactionNotFoundException: transaction Id is null!");
        }

        TransactionEntity transaction = em.find(TransactionEntity.class, transactionId);

        if (transaction == null) {
            throw new TransactionNotFoundException("TransactionNotFoundException: Transaction with ID " + transactionId + " does not exists!");
        }

        return transaction;
    }

    @Override
    public Long markTransactionPaid(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException {
        TransactionEntity transaction = this.retrieveTransactionByTransactionId(transactionId);

        if (transaction.getPayment() == null || transaction.getPayment().getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new UpdateTransactionStatusException("UpdateTransactionStatusException: Payment has not yet completed!");
        }
        transaction.setTransactionStatus(TransactionStatusEnum.PAID);

        return this.updateTransactionStatusHelper(transaction);
    }

    @Override
    // this status should be able to be updated by user, for listing with meet up
    public Long markTransactionReceived(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException {
        TransactionEntity transaction = this.retrieveTransactionByTransactionId(transactionId);

        // if mode of delivery is delivery
        if (transaction.getOffer().getListing().getDeliveryOption() == DeliveryOptionEnum.MAIL) {
            if (transaction.getDelivery() == null || transaction.getDelivery().getDeliveryStatus() != DeliveryStatusEnum.DELIVERED) {
                throw new UpdateTransactionStatusException("UpdateTransactionStatusException: Item has not yet been delivered!");
            }
        }

        transaction.setTransactionStatus(TransactionStatusEnum.RECEIVED);

        return this.updateTransactionStatusHelper(transaction);
    }

    //mark transaction as completed when rental has ended
    @Override
    public Long markTransactionCompleted(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException {
        TransactionEntity transaction = this.retrieveTransactionByTransactionId(transactionId);

        transaction.setTransactionStatus(TransactionStatusEnum.COMPLETED);

        return this.updateTransactionStatusHelper(transaction);
    }

    //user cancels an offer, and the cancellation will cancel the transaction
    @Override
    public Long markTransactionCancelled(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException {
        TransactionEntity transaction = this.retrieveTransactionByTransactionId(transactionId);
        if (transaction.getOffer().getOfferStatus() != OfferStatusEnum.CANCELLED) {
            throw new UpdateTransactionStatusException("UpdateTransactionStatusException: Offer has to be cancelled first!");
        }

        transaction.setTransactionStatus(TransactionStatusEnum.CANCELLED);

        return this.updateTransactionStatusHelper(transaction);
    }

    private Long updateTransactionStatusHelper(TransactionEntity transaction) throws UpdateTransactionStatusException {
        try {
            em.merge(transaction);
            return transaction.getTransactionId();
        } catch (PersistenceException ex) {
            throw new UpdateTransactionStatusException("UpdateTransactionStatusException: " + ex.getMessage());
        }
    }

//    @Schedule(hour = "12")
//    public void automateTransactionStatus() {
//        try {
//            List<OfferEntity> offers = offerEntitySessionBeanLocal.retrieveAllOffers();
//            Date todayDate = new Date();
//            SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
//            todayDate = dateFormatter.parse(dateFormatter.format(new Date()));
//            for (OfferEntity offer : offers) {
//                Date offerEndDate = dateFormatter.parse(dateFormatter.format(offer.getRentalEndDate()));
//                if (!offerEndDate.after(todayDate) && (offer.getTransaction().getTransactionStatus() == TransactionStatusEnum.RECEIVED)) {
//                    offer.getTransaction().setTransactionStatus(TransactionStatusEnum.COMPLETED);
//                }
//            }
//        } catch (ParseException ex) {
//            System.out.println("Invalid date, please try again.");
//        }
//    }
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(TransactionEntity transactionEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<TransactionEntity>> errors = validator.validate(transactionEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

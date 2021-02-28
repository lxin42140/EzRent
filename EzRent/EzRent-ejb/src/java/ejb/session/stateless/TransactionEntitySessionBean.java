/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.DeliveryEntity;
import entity.OfferEntity;
import entity.PaymentEntity;
import entity.ReviewEntity;
import entity.TransactionEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.DeliveryStatusEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.OfferStatusEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewTransactionException;
import util.exception.TransactionAlreadyCancelledException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class TransactionEntitySessionBean implements TransactionEntitySessionBeanLocal {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    @Resource
    private TimerService timerService;
    

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public TransactionEntity createNewTransaction(Long offerId, PaymentEntity newPayment, Long creditCardId, Long deliveryId, Long reviewId, TransactionEntity newTransaction) throws CreateNewTransactionException {

        if (newTransaction != null || offerId != null || newPayment != null) {
            OfferEntity offer = em.find(OfferEntity.class, offerId);

            if (offer.equals(null)) {
                throw new CreateNewTransactionException("Failed to create new Transaction; Offer not found!");
            } else {
                offer.setTransaction(newTransaction);
                newTransaction.setOffer(offer);
                newPayment.setTransaction(newTransaction);
                newTransaction.setPayment(newPayment);
            }

            if (deliveryId != null) {
                DeliveryEntity delivery = em.find(DeliveryEntity.class, deliveryId);
                delivery.setTransaction(newTransaction);
                newTransaction.setDelivery(delivery);
            }
            if (reviewId != null) {
                ReviewEntity review = em.find(ReviewEntity.class, reviewId);
                review.setTransaction(newTransaction);
                newTransaction.getReviews().add(review);
            }
            if (newPayment.getModeOfPayment() == ModeOfPaymentEnum.CASH_ON_DELIVERY || (newPayment.getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD && newPayment.getPaymentStatus() == PaymentStatusEnum.UNPAID)) {
                newTransaction.setTransactionStatus(TransactionStatusEnum.PENDING_PAYMENT);

            } else if (newPayment.getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD && newPayment.getPaymentStatus() == PaymentStatusEnum.PAID) {
                newTransaction.setTransactionStatus(TransactionStatusEnum.PAID);
            }

            if (creditCardId != null) {
                CreditCardEntity creditCard = em.find(CreditCardEntity.class, creditCardId);
                creditCard.getPayments().add(newPayment);
                newPayment.setCreditCard(creditCard);
            }
            offer.setOfferStatus(OfferStatusEnum.ACCEPTED);
            em.persist(newPayment);
            em.persist(newTransaction);
            em.flush();
            return newTransaction;
        } else {
            throw new CreateNewTransactionException("New Transaction provided not complete!");
        }
    }

    @Override
    //retrieving all transaction incl. cancelled transactions
    public List<TransactionEntity> retrieveAllTransactions() {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t");

        return query.getResultList();
    }

    @Override
    //retrieving all non-cancelled transactions
    public List<TransactionEntity> retrieveAllValidTransactions() {
        Query query = em.createQuery("SELECT t FROM TransactionEntity t WHERE NOT t.transactionStatus = :status");
        query.setParameter("status", TransactionStatusEnum.CANCELLED);

        return query.getResultList();
    }

    @Override
    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException {
        TransactionEntity transaction = em.find(TransactionEntity.class, transactionId);

        if (transaction != null) {
            transaction.getDelivery();
            transaction.getOffer();
            transaction.getPayment();
            transaction.getReviews().size();
            return transaction;
        } else {
            throw new TransactionNotFoundException("Transaction ID " + transactionId + " does not exist!");
        }
    }
    
    //This is for auto-completion for the transaction part. So when the transaction
    @Override
    @Schedule(hour = "12")
    public void automateTransactionStatus() {
        try {
            List<OfferEntity> offers = offerEntitySessionBeanLocal.retrieveAllOffers();
            Date todayDate = new Date();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
            todayDate = dateFormatter.parse(dateFormatter.format(new Date()));
            for(OfferEntity offer: offers) {
                Date offerEndDate = dateFormatter.parse(dateFormatter.format(offer.getRentalEndDate()));
                if(!offerEndDate.after(todayDate) && (offer.getTransaction().getTransactionStatus() == TransactionStatusEnum.ONGOING)) {
                    offer.getTransaction().setTransactionStatus(TransactionStatusEnum.COMPLETED);
                }
            }
        } catch(ParseException ex) {
            System.out.println("Invalid date, please try again.");
        }
    }
    
    @Override
    public void updateTransactionStatus(Long transactionId, TransactionStatusEnum transactionStatus) throws UpdateTransactionStatusException, TransactionNotFoundException {
        if (transactionId == null) {
            throw new UpdateTransactionStatusException("Incomplete information provided; unable to update transaction status!");
        } else {
            TransactionEntity transaction = retrieveTransactionByTransactionId(transactionId);

            if (transaction.getDelivery() != null) {
                if (transaction.getTransactionStatus() == TransactionStatusEnum.RECEIVED) {
                    transaction.getDelivery().setDeliveryStatus(DeliveryStatusEnum.DELIVERED);
                }
            }
            
        }
    }

    @Override
    public void cancelTransaction(Long transactionId) throws TransactionNotFoundException, TransactionAlreadyCancelledException {
        TransactionEntity transaction = retrieveTransactionByTransactionId(transactionId);

        if (transaction.getTransactionStatus() != TransactionStatusEnum.CANCELLED) {
            transaction.setTransactionStatus(TransactionStatusEnum.CANCELLED);
            transaction.getOffer().setOfferStatus(OfferStatusEnum.CANCELLED);

        } else {
            throw new TransactionAlreadyCancelledException("The transaction has already been cancelled!");
        }
    }
}

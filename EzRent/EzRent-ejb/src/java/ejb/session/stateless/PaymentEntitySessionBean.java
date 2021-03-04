/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.PaymentEntity;
import entity.TransactionEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.PaymentNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdatePaymentFailException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class PaymentEntitySessionBean implements PaymentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;
    @EJB
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    public Long createNewPayment(PaymentEntity payment, Long creditCardId, Long transactionId) throws CreditCardNotFoundException, TransactionNotFoundException, CreateNewPaymentException {
        if (payment == null) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Please provide a valid payment!");
        }

        TransactionEntity transaction = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);

        if (transaction.getOffer().getListing().getModeOfPayment() == ModeOfPaymentEnum.CREDIT_CARD && creditCardId == null) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Please provide a credit card!");
        } else if (transaction.getOffer().getListing().getModeOfPayment() == ModeOfPaymentEnum.CASH_ON_DELIVERY && creditCardId != null) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Only cash on delivery allowed!");
        }

        CreditCardEntity creditCard = null;
        if (creditCardId != null) {
            creditCard = creditCardEntitySessionBeanLocal.retrieveCreditCardByCreditCardId(creditCardId);
            payment.setCreditCard(creditCard);
            payment.setPaymentStatus(PaymentStatusEnum.PAID);
            transaction.setTransactionStatus(TransactionStatusEnum.PAID);
            payment.setModeOfPayment(ModeOfPaymentEnum.CREDIT_CARD);
        } else {
            //if COD
            //when delivery is shipped, change transaction status to received
            //change payment status to paid
            //to be done in the delivery SB
            payment.setModeOfPayment(ModeOfPaymentEnum.CASH_ON_DELIVERY);
        }

        if (creditCard != null && !creditCard.getCustomer().getUserId().equals(transaction.getOffer().getCustomer().getUserId())) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Invalid credit card!");
        }

        payment.setTransaction(transaction);
        transaction.setPayment(payment);

        try {
            validate(payment);
            em.persist(payment);
            em.flush();
            return payment.getPaymentId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewPaymentException("CreateNewPaymentException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewPaymentException("CreateNewPaymentException: Payment with same comment ID already exists!");
            } else {
                throw new CreateNewPaymentException("CreateNewPaymentException: " + ex.getMessage());
            }
        }
    }

    public List<PaymentEntity> retrieveAllPayments() {
        Query query = em.createQuery("SELECT p FROM PaymentEntity p");

        return query.getResultList();
    }

    public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException {
        if (paymentId == null) {
            throw new PaymentNotFoundException("PaymentNotFoundException: Payment id is null!");
        }
        
        PaymentEntity payment = em.find(PaymentEntity.class, paymentId);

        if (payment == null) {
            throw new PaymentNotFoundException("PaymentNotFoundException: Payment ID " + paymentId + " not found!");
        }

        return payment;
    }

    public Long updatePaymentStatus(Long paymentId, PaymentStatusEnum status) throws PaymentNotFoundException, UpdatePaymentFailException {
        if (status == null) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: Payment status is null!");
        }

        PaymentEntity payment = retrievePaymentByPaymentId(paymentId);

        payment.setPaymentStatus(status);
        em.merge(payment);
        em.flush();
        return payment.getPaymentId();
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(PaymentEntity payment) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<PaymentEntity>> errors = validator.validate(payment);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }

    }

}

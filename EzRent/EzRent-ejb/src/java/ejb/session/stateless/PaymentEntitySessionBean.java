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
import util.enumeration.DeliveryStatusEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.PaymentNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdatePaymentFailException;
import util.exception.UpdateTransactionStatusException;
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

    //cash on delivery
    @Override
    public Long createNewCashPayment(PaymentEntity payment, Long transactionId) throws TransactionNotFoundException, CreateNewPaymentException {
        if (payment == null || transactionId == null) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Please provide a valid payment/transaction!");
        }

        TransactionEntity transaction = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);
        if (transaction.getOffer().getListing().getModeOfPayment() != ModeOfPaymentEnum.CASH_ON_DELIVERY) {
            throw new CreateNewPaymentException("CreateNewPaymentException: The listing does not allow cash on delivery!");
        }

        //set mode of payment to cash on delivery
        payment.setModeOfPayment(ModeOfPaymentEnum.CASH_ON_DELIVERY);
        //set payment status to unpaids
        payment.setPaymentStatus(PaymentStatusEnum.UNPAID);

        //set bi-directional with transaction
        payment.setTransaction(transaction);
        transaction.setPayment(payment);

        return this.createNewPaymentHelper(payment);
    }

    @Override
    public Long createNewCreditCardPayement(PaymentEntity payment, Long creditCardId, Long transactionId) throws UpdateTransactionStatusException, CreditCardNotFoundException, TransactionNotFoundException, CreateNewPaymentException {
        if (payment == null || transactionId == null || creditCardId == null) {
            throw new CreateNewPaymentException("CreateNewPaymentException: Please provide a valid payment/transaction ID/credit card ID!");
        }

        //retrieve transaction
        TransactionEntity transaction = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);
        if (transaction.getOffer().getListing().getModeOfPayment() != ModeOfPaymentEnum.CREDIT_CARD) {
            throw new CreateNewPaymentException("CreateNewPaymentException: The listing does not allow credit card payment!");
        }
        //set bi-directional with transaction
        payment.setTransaction(transaction);
        transaction.setPayment(payment);

        //retrieve credit card
        CreditCardEntity creditCard = creditCardEntitySessionBeanLocal.retrieveCreditCardByCreditCardId(creditCardId);
        //set bi ass with payment
        payment.setCreditCard(creditCard);
        creditCard.getPayments().add(payment);

        payment.setModeOfPayment(ModeOfPaymentEnum.CREDIT_CARD);
        //set payment to paid
        payment.setPaymentStatus(PaymentStatusEnum.PAID);

        //set transaction to paid
        transactionEntitySessionBeanLocal.markTransactionPaid(transaction.getTransactionId());

        return this.createNewPaymentHelper(payment);
    }

    private Long createNewPaymentHelper(PaymentEntity payment) throws CreateNewPaymentException {
        try {
            validate(payment);
            em.persist(payment);
            em.flush();
            return payment.getPaymentId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewPaymentException("CreateNewPaymentException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewPaymentException("CreateNewPaymentException: Payment with same ID already exists!");
            } else {
                throw new CreateNewPaymentException("CreateNewPaymentException: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<PaymentEntity> retrieveAllPayments() {
        Query query = em.createQuery("SELECT p FROM PaymentEntity p");

        return query.getResultList();
    }

    @Override
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

    // only for cash on delivery
    @Override
    public Long markCashPaymentPaid(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException {
        PaymentEntity payment = this.retrievePaymentByPaymentId(paymentId);

        if (payment.getPaymentStatus() != PaymentStatusEnum.UNPAID) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: " + payment.getPaymentStatus() + " payment cannot be marked as paid!");
        }

        if (payment.getTransaction().getDelivery().getDeliveryStatus() != DeliveryStatusEnum.DELIVERED) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: Deliery has yet been completed!");
        }

        payment.setPaymentStatus(PaymentStatusEnum.PAID);
        return this.updatePaymentStatusHelper(payment);
    }

    @Override
    public Long refundPayment(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException {
        PaymentEntity payment = this.retrievePaymentByPaymentId(paymentId);

        if (payment.getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: " + payment.getPaymentStatus() + " payment cannot be refunded!");
        }

        payment.setPaymentStatus(PaymentStatusEnum.REFUNDED);
        return this.updatePaymentStatusHelper(payment);
    }

    @Override
    public Long voidPayment(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException {
        PaymentEntity payment = this.retrievePaymentByPaymentId(paymentId);

        if (payment.getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: " + payment.getPaymentStatus() + " payment cannot be refunded!");
        }

        payment.setPaymentStatus(PaymentStatusEnum.VOID);
        return this.updatePaymentStatusHelper(payment);
    }

    private Long updatePaymentStatusHelper(PaymentEntity payment) throws UpdatePaymentFailException, UpdatePaymentFailException {
        try {
            em.merge(payment);
            em.flush();
            return payment.getPaymentId();
        } catch (PersistenceException ex) {
            throw new UpdatePaymentFailException("UpdatePaymentFailException: " + ex.getMessage());
        }
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

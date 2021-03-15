/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.PaymentNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdatePaymentFailException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author Yuxin
 */
@Local
public interface PaymentEntitySessionBeanLocal {

    public Long createNewCashPayment(PaymentEntity payment, Long transactionId) throws TransactionNotFoundException, CreateNewPaymentException;

    public Long createNewCreditCardPayement(PaymentEntity payment, Long creditCardId, Long transactionId) throws UpdateTransactionStatusException, CreditCardNotFoundException, TransactionNotFoundException, CreateNewPaymentException;

    public List<PaymentEntity> retrieveAllPayments();

    public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException;

    public Long markCashPaymentPaid(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException;

    public Long refundPayment(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException;

    public Long voidPayment(Long paymentId) throws PaymentNotFoundException, UpdatePaymentFailException;
    
}

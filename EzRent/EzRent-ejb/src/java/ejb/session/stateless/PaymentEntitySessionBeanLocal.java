/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.PaymentStatusEnum;
import util.exception.CreateNewPaymentException;
import util.exception.CreditCardNotFoundException;
import util.exception.PaymentNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdatePaymentFailException;

/**
 *
 * @author Yuxin
 */
@Local
public interface PaymentEntitySessionBeanLocal {

    public Long createNewPayment(PaymentEntity payment, Long creditCardId, Long transactionId) throws CreditCardNotFoundException, TransactionNotFoundException, CreateNewPaymentException;

    public List<PaymentEntity> retrieveAllPayments();

    public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException;

    public Long updatePaymentStatus(Long paymentId, PaymentStatusEnum status) throws PaymentNotFoundException, UpdatePaymentFailException;
}

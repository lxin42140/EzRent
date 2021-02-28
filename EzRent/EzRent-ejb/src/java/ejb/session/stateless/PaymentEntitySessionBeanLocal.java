/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.PaymentNotFoundException;

/**
 *
 * @author Yuxin
 */
@Local
public interface PaymentEntitySessionBeanLocal {

    public List<PaymentEntity> retrieveAllPayments();

    public PaymentEntity retrievePaymentByPaymentId(Long paymentId) throws PaymentNotFoundException;

    public void updatePayment(PaymentEntity payment);
    
}

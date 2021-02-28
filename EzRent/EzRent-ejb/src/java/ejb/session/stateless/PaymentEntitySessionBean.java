/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaymentEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.PaymentNotFoundException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class PaymentEntitySessionBean implements PaymentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    @Override
    public List<PaymentEntity> retrieveAllPayments() {
        Query query = em.createQuery("Select p FROM PaymentEntity p");
        
        return query.getResultList();
    }
    
    @Override
    public PaymentEntity retrievePaymentByPaymentId (Long paymentId) throws PaymentNotFoundException {
        PaymentEntity payment = em.find(PaymentEntity.class, paymentId);
        if(payment != null) {
            
            payment.getTransaction();
            payment.getCreditCard();
            return payment;
        } else {
            throw new PaymentNotFoundException("Payment ID:" + paymentId + " not found!");
        }
    }
    
    @Override
    public void updatePayment(PaymentEntity payment) {
        em.merge(payment);
    }
    
    
}

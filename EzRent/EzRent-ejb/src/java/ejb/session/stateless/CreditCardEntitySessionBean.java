/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class CreditCardEntitySessionBean implements CreditCardEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public CreditCardEntity createNewCreditCard (Long customerId, CreditCardEntity newCreditCard) throws CreateNewCreditCardException {
        if(newCreditCard != null) {
            CustomerEntity customer = em.find(CustomerEntity.class, customerId);
            if (customer.equals(null)) {
                throw new CreateNewCreditCardException("Failed to create new credit card; Customer not found!");
            } else {
                newCreditCard.setCustomer(customer);
                customer.getCreditCards().add(newCreditCard);
                em.persist(newCreditCard);
                em.flush();
                return newCreditCard;
            }
        } else {
            throw new CreateNewCreditCardException("Credit Card not provided!");
        }
    }
    
    @Override
    public List<CreditCardEntity> retrieveAllCreditCardByCustomerId(Long customerId) throws CustomerNotFoundException{
        Query query = em.createQuery("Select cc FROM CreditCardEntity cc, IN (cc.customer) c WHERE c.userId =:userId");
        query.setParameter("userId", customerId);
        
        return query.getResultList();
    }
    
    @Override
    public CreditCardEntity retrieveCreditCardByCreditCardId (Long creditCardId) throws CreditCardNotFoundException {
        CreditCardEntity creditCard = em.find(CreditCardEntity.class, creditCardId);
        
        if(creditCard != null) {
            creditCard.getCustomer();
            creditCard.getPayments().size();
            return creditCard;
        } else {
            throw new CreditCardNotFoundException("Credit Card Id: " + creditCardId + " not found!");
        }
    }
    
   @Override
   public void updateCreditCardDetails(CreditCardEntity creditCard) {
        
       em.merge(creditCard);
   }
   
   @Override
   public void deleteCreditCard (Long creditCardId) throws CreditCardNotFoundException{
       CreditCardEntity ccToDelete = retrieveCreditCardByCreditCardId(creditCardId);
       ccToDelete.getCustomer().getCreditCards().remove(ccToDelete);
       ccToDelete.getPayments().remove(ccToDelete);
       em.remove(ccToDelete);
   }
}

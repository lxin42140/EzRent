/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Yuxin
 */
@Local
public interface CreditCardEntitySessionBeanLocal {

    public CreditCardEntity createNewCreditCard(Long customerId, CreditCardEntity newCreditCard) throws CreateNewCreditCardException;

    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;

    public List<CreditCardEntity> retrieveAllCreditCardByCustomerId(Long customerId) throws CustomerNotFoundException;

    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException;

    public void updateCreditCardDetails(CreditCardEntity creditCard);

    
}

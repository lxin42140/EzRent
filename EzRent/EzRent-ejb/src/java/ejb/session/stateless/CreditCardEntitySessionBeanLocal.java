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
import util.exception.DeleteCreditCardException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author Yuxin
 */
@Local
public interface CreditCardEntitySessionBeanLocal {

    public Long createNewCreditCard(Long customerId, CreditCardEntity newCreditCard) throws CreateNewCreditCardException, CustomerNotFoundException;

    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;

    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException;

    public Long updateCreditCardDetails(Long creditCardId, CreditCardEntity updatedCreditCard) throws UpdateCreditCardException, CreditCardNotFoundException;

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCardEntity;
import entity.CustomerEntity;
import entity.PaymentEntity;
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
import util.enumeration.PaymentStatusEnum;
import util.exception.CreateNewCreditCardException;
import util.exception.CreditCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.UpdateCreditCardException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class CreditCardEntitySessionBean implements CreditCardEntitySessionBeanLocal {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCreditCard(Long customerId, CreditCardEntity newCreditCard) throws CreateNewCreditCardException, CustomerNotFoundException {
        if (customerId == null) {
            throw new CreateNewCreditCardException("CreateNewCreditCardException: Please provide a valid customer id!");
        }

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        // set binary relationship with customer
        customer.getCreditCards().add(newCreditCard);
        newCreditCard.setCustomer(customer);

        try {
            validate(newCreditCard);

            em.persist(newCreditCard);
            em.flush();

            return newCreditCard.getCreditCardId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewCreditCardException("CreateNewCreditCardException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCreditCardException("CreateNewCreditCardException: Credit card with same card number already exists!");
            } else {
                throw new CreateNewCreditCardException("CreateNewCreditCardException: " + ex.getMessage());
            }
        }
    }

    @Override
    public CreditCardEntity retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException {
        if (creditCardId == null) {
            throw new CreditCardNotFoundException("CreditCardNotFoundException: Please enter a valid credit card!");
        }

        CreditCardEntity creditCard = em.find(CreditCardEntity.class, creditCardId);

        if (creditCard == null || creditCard.getIsDeleted()) {
            throw new CreditCardNotFoundException("CreditCardNotFoundException: Credit card with id " + creditCardId + " does not exist!");
        }

        // Not necessary to return payments as of now
        return creditCard;
    }
    
    @Override
    public List<CreditCardEntity> retrieveCreditCardsByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT c FROM CreditCardEntity c WHERE c.customer.userId = :inCustId AND c.isDeleted = FALSE");
        query.setParameter("inCustId", customerId);
        return query.getResultList();
    }

    // cannot physcically delete a credit card as it might be invovled with paid payments
    @Override
    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException {
        if (creditCardId == null) {
            throw new DeleteCreditCardException("DeleteCreditCardException: Please provide a valid credit card id!");
        }

        CreditCardEntity ccToDelete = this.retrieveCreditCardByCreditCardId(creditCardId);

        // cannot delete credit card involved in unpaid payments
        if (!ccToDelete.getPayments().isEmpty()) {
            for (PaymentEntity payment : ccToDelete.getPayments()) {
                if (payment.getPaymentStatus() == PaymentStatusEnum.UNPAID) {
                    throw new DeleteCreditCardException("DeleteCreditCardException: Credit card is associated with unpaid payment!");
                }
            }
        }

        ccToDelete.setIsDeleted(true);
        em.merge(ccToDelete);
    }

    @Override
    public Long updateCreditCardDetails(Long creditCardId, CreditCardEntity updatedCreditCard) throws UpdateCreditCardException, CreditCardNotFoundException {
        if (creditCardId == null) {
            throw new CreditCardNotFoundException("CreditCardNotFoundException: Please provide a valid id!");
        }

        CreditCardEntity creditCardToUpdate = this.retrieveCreditCardByCreditCardId(creditCardId);

        if (!creditCardToUpdate.getCardName().equals(updatedCreditCard.getCardName())) {
            creditCardToUpdate.setCardName(updatedCreditCard.getCardName());
        }
        if (!creditCardToUpdate.getCardNumber().equals(updatedCreditCard.getCardNumber())) {
            creditCardToUpdate.setCardNumber(updatedCreditCard.getCardNumber());
        }
        if (!creditCardToUpdate.getCvv().equals(updatedCreditCard.getCvv())) {
            creditCardToUpdate.setCvv(updatedCreditCard.getCvv());
        }
        if (!creditCardToUpdate.getExpiryDate().equals(updatedCreditCard.getExpiryDate())) {
            creditCardToUpdate.setExpiryDate(updatedCreditCard.getExpiryDate());
        }

        try {
            validate(creditCardToUpdate);

            em.merge(creditCardToUpdate);
            em.flush();
            return creditCardToUpdate.getCreditCardId();
        } catch (ValidationFailedException ex) {
            throw new UpdateCreditCardException("UpdateCreditCardException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new UpdateCreditCardException("UpdateCreditCardException: Credit card with same card number already exists!");
            } else {
                throw new UpdateCreditCardException("UpdateCreditCardException: " + ex.getMessage());
            }
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CreditCardEntity creditCardEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CreditCardEntity>> errors = validator.validate(creditCardEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

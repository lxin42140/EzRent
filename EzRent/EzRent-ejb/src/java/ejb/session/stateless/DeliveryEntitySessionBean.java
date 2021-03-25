/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DeliveryEntity;
import entity.TransactionEntity;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.DeliveryStatusEnum;
import util.exception.CreateNewDeliveryException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.DeliveryNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateDeliveryException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Li Xin
 */
@Stateless
public class DeliveryEntitySessionBean implements DeliveryEntitySessionBeanLocal {

    @EJB
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;
    @EJB
    private DeliveryCompanyEntitySessionBeanLocal deliveryCompanyEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewDelivery(DeliveryEntity newDeliveryEntity, Long transactionId) throws DeliveryCompanyNotFoundException, CreateNewDeliveryException, TransactionNotFoundException {
        if (newDeliveryEntity == null) {
            throw new CreateNewDeliveryException("CreateNewDeliveryException: Please provide a valid delivery!");
        }

        // new delivery will start with pending
        newDeliveryEntity.setDeliveryStatus(DeliveryStatusEnum.PENDING_DELIVERY);
        // update timestamp
        Calendar cal = Calendar.getInstance();
        newDeliveryEntity.setLastUpateDate(cal.getTime());
        
        newDeliveryEntity.setDeliveryCompany(deliveryCompanyEntitySessionBeanLocal.retrieveDeliveryCompanyById(5l));
        
        TransactionEntity transaction = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);
        //bi assoc between delivery and transaction
        newDeliveryEntity.setTransaction(transaction);
        transaction.setDelivery(newDeliveryEntity);

        try {
            validate(newDeliveryEntity);
            em.persist(newDeliveryEntity);
            em.flush();
            return newDeliveryEntity.getDeliveryId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new CreateNewDeliveryException("CreateNewAdminstratorException: " + ex.getMessage());
        }
    }

    @Override
    public Long updateDeliveryStatus(Long deliveryId, DeliveryStatusEnum newDeliveryStatus) throws UpdateDeliveryException, DeliveryNotFoundException {
        if (deliveryId == null || newDeliveryStatus == null) {
            throw new UpdateDeliveryException("UpdateDeliveryException: Please provide valid delivery id/status");
        }

        DeliveryEntity existingDeliveryEntity = em.find(DeliveryEntity.class, deliveryId);
        if (existingDeliveryEntity.getDeliveryStatus() == newDeliveryStatus) {
            throw new UpdateDeliveryException("UpdateDeliveryException: New state should not be same as existing state!");
        }

        boolean invalidState = false;
        String invalidReason = "";
        switch (existingDeliveryEntity.getDeliveryStatus()) {
            // pending -> shipped
            case PENDING_DELIVERY:
                if (newDeliveryStatus != DeliveryStatusEnum.SHIPPED) {
                    invalidState = true;
                }
                invalidReason = "Next state should be shipped!";
                break;
            // shipped -> deliverying / lost
            case SHIPPED:
                if (newDeliveryStatus != DeliveryStatusEnum.DELIVERING || newDeliveryStatus != DeliveryStatusEnum.LOST) {
                    invalidState = true;
                }
                invalidReason = "Next state should be delivering or lost!";
                break;
            // deliverying -> lost / delivered
            case DELIVERING:
                if (newDeliveryStatus != DeliveryStatusEnum.DELIVERED || newDeliveryStatus != DeliveryStatusEnum.LOST) {
                    invalidState = true;
                }
                invalidReason = "Next state should be delivered or lost!";
                break;
            case DELIVERED:
            // if cod, change payment to paid
            // update the transaction status
            case LOST:
                // if credit card payment, mark payment status as REFUND
                // update transaction status
                invalidState = true;
                invalidReason = "There is no next state!";
                break;
        }
        if (invalidState) {
            throw new UpdateDeliveryException("UpdateDeliveryException: " + invalidReason);
        }

        // update status
        existingDeliveryEntity.setDeliveryStatus(newDeliveryStatus);

        // update timestamp
        Calendar cal = Calendar.getInstance();
        existingDeliveryEntity.setLastUpateDate(cal.getTime());

        try {
            validate(existingDeliveryEntity);
            em.merge(existingDeliveryEntity);
            em.flush();
            return existingDeliveryEntity.getDeliveryId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateDeliveryException("UpdateDeliveryException: " + ex.getMessage());
        }
    }

    @Override
    public List<DeliveryEntity> retrieveAllDeliveries() {
        Query query = em.createNamedQuery("retrieveAllDeliveries");
        return query.getResultList();
    }

    @Override
    public DeliveryEntity retrieveDeliveryByDeliveryId(Long deliveryId) throws DeliveryNotFoundException {
        if (deliveryId == null) {
            throw new DeliveryNotFoundException("DeliveryNotFoundException: Please enter a valid ID!");
        }

        Query query = em.createNamedQuery("retrieveDeliveryByDeliveryId");
        query.setParameter("inDeliveryId", deliveryId);

        try {
            return (DeliveryEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new DeliveryNotFoundException("DeliveryNotFoundException: Delivery with id " + deliveryId + " does not exist!");
        }
    }

    @Override
    public List<DeliveryEntity> retrieveDeliveriesByStatus(DeliveryStatusEnum deliveryStatus) {
        Query query = em.createNamedQuery("retrieveDeliveryByDeliveryStatus");
        query.setParameter("inDeliveryStatus", deliveryStatus);

        return query.getResultList();
    }

    private void validate(DeliveryEntity deliveryEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<DeliveryEntity>> errors = validator.validate(deliveryEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

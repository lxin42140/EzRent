/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DeliveryCompanyEntity;
import java.util.List;
import java.util.Set;
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
import util.enumeration.UserAccessRightEnum;
import util.exception.CreateNewDeliveryCompanyException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.UpdateDeliveryCompanyException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Li Xin
 */
@Stateless
public class DeliveryCompanyEntitySessionBean implements DeliveryCompanyEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewDeliveryCompany(DeliveryCompanyEntity newDeliveryCompanyEntity) throws CreateNewDeliveryCompanyException {
        if (newDeliveryCompanyEntity == null) {
            throw new CreateNewDeliveryCompanyException("CreateNewDeliveryCompanyException: Invalid new delivery company");
        }

        newDeliveryCompanyEntity.setAccessRight(UserAccessRightEnum.DELIVERY_COMPANY);

        try {
            validate(newDeliveryCompanyEntity);
            em.persist(newDeliveryCompanyEntity);
            em.flush();
            return newDeliveryCompanyEntity.getUserId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewDeliveryCompanyException("CreateNewDeliveryCompanyException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewDeliveryCompanyException("CreateNewDeliveryCompanyException: Company with the same name/UEN already exists!");
            } else {
                throw new CreateNewDeliveryCompanyException("CreateNewDeliveryCompanyException: " + ex.getMessage());
            }
        }
    }

    @Override
    public Long updateDeliveryCompanyDetails(Long deliveryCompanyId, DeliveryCompanyEntity deliveryCompanyToUpdate) throws DeliveryCompanyNotFoundException, UpdateDeliveryCompanyException {
        if (deliveryCompanyId == null || deliveryCompanyToUpdate == null) {
            throw new UpdateDeliveryCompanyException("UpdateDeliveryCompanyException: Please provide valid delivery company/company ID");
        }

        DeliveryCompanyEntity existinDeliveryCompanyEntity = em.find(DeliveryCompanyEntity.class, deliveryCompanyId);
        if (existinDeliveryCompanyEntity == null) {
            throw new DeliveryCompanyNotFoundException("DeliveryCompanyNotFoundException: Delivery company with ID " + deliveryCompanyId + " does not exist!");
        }

        // update company name
        if (!deliveryCompanyToUpdate.getCompanyName().equals(existinDeliveryCompanyEntity.getCompanyName())) {
            existinDeliveryCompanyEntity.setCompanyName(deliveryCompanyToUpdate.getCompanyName());
        }
        // update POC contact number
        if (!deliveryCompanyToUpdate.getCompanyContactNumber().equals(existinDeliveryCompanyEntity.getCompanyContactNumber())) {
            existinDeliveryCompanyEntity.setCompanyContactNumber(deliveryCompanyToUpdate.getCompanyContactNumber());
        }
        // update company UEN
        if (!deliveryCompanyToUpdate.getCompanyUEN().equals(existinDeliveryCompanyEntity.getCompanyUEN())) {
            existinDeliveryCompanyEntity.setCompanyUEN(deliveryCompanyToUpdate.getCompanyUEN());
        }

        try {
            validate(existinDeliveryCompanyEntity);
            em.merge(existinDeliveryCompanyEntity);
            em.flush();
            return existinDeliveryCompanyEntity.getUserId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateDeliveryCompanyException("UpdateDeliveryCompanyException: " + ex.getMessage());
        }
    }

    @Override
    public List<DeliveryCompanyEntity> retrieveAllDeliveryCompanies() {
        Query query = em.createNamedQuery("retrieveAllUndeletedDeliveryCompanies");
        return query.getResultList();
    }

    @Override
    public List<DeliveryCompanyEntity> retrieveAllDisabledDeliveryCompanies() {
        Query query = em.createNamedQuery("retrieveAllDisabledDeliveryCompanies");
        return query.getResultList();
    }

    @Override
    public DeliveryCompanyEntity retrieveDeliveryCompanyById(Long companyId) throws DeliveryCompanyNotFoundException {
        if (companyId == null) {
            throw new DeliveryCompanyNotFoundException("DeliveryCompanyNotFoundException: Please enter a valid ID!");
        }

        Query query = em.createNamedQuery("retrieveDeliveryCompanyById");
        query.setParameter("inCompanyId", companyId);

        try {
            return (DeliveryCompanyEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new DeliveryCompanyNotFoundException("DeliveryCompanyNotFoundException: Company with id " + companyId + " does not exist!");
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(DeliveryCompanyEntity deliveryCompanyEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<DeliveryCompanyEntity>> errors = validator.validate(deliveryCompanyEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

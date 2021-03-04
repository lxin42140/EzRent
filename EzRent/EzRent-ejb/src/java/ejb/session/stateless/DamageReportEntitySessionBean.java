/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.DamageReportEntity;
import entity.TransactionEntity;
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
import util.enumeration.DamageReportEnum;
import util.exception.CreateNewDamageReportException;
import util.exception.CustomerNotFoundException;
import util.exception.DamageReportNotFoundException;
import util.exception.DeleteDamageReportException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateDamageReportException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Ziyue
 */
@Stateless
public class DamageReportEntitySessionBean implements DamageReportEntitySessionBeanLocal {

    @EJB(name = "TransactionEntitySessionBeanLocal")
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    
    
    @Override
    public Long createNewDamageReport(Long customerId, Long transactionId, DamageReportEntity damageReportEntity) throws CreateNewDamageReportException, CustomerNotFoundException, TransactionNotFoundException {
        
        if (customerId == null) {
            throw new CreateNewDamageReportException("CreateNewDamageReportException: customer ID can't be null!");
        }
        
        if (transactionId == null) {
            throw new CreateNewDamageReportException("CreateNewDamageReportException: transaction ID can't be null!");
        }
        
        if (damageReportEntity == null) {
            throw new CreateNewDamageReportException("CreateNewDamageReportException: damage report can't be null!");
        }
        
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        
        TransactionEntity transactionEntity = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);
        
        //Check if said transaction belongs to either the lessor or lessee
        if(!transactionEntity.getOffer().getCustomer().equals(customerEntity) && !transactionEntity.getOffer().getListing().getLessor().equals(customerEntity)) {        
            throw new CreateNewDamageReportException("CreateNewDamageReportException: Transaction indicated not linked to either lessor or lessee!");
        } 
        
        //Associate damage report with customer
        customerEntity.getDamageReports().add(damageReportEntity);
        damageReportEntity.setCustomer(customerEntity);
        
        //Associate damage report with transaction
        damageReportEntity.setTransaction(transactionEntity);
        
        try {
            validate(damageReportEntity);
            
            em.persist(damageReportEntity);
            em.flush();
            return damageReportEntity.getDamageReportId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewDamageReportException("CreateNewDamageReportException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewDamageReportException("CreateNewDamageReportException: Damage Report with same Damage Report ID already exists!");
            } else {
                throw new CreateNewDamageReportException("CreateNewDamageReportException: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<DamageReportEntity> retrieveAllDamageReports() {
        Query query = em.createQuery("SELECT d FROM DamageReportEntity d");
        return query.getResultList();
    }
    
    @Override
    public List<DamageReportEntity> retrieveAllPendingDamageReports() {
        Query query = em.createQuery("SELECT d FROM DamageReportEntity d WHERE d.damageReport = :status");
        query.setParameter("status", DamageReportEnum.PENDING);

        return query.getResultList();
    }
    
    @Override
    public List<DamageReportEntity> retrieveAllResolvedDamageReports() {
        Query query = em.createQuery("SELECT d FROM DamageReportEntity d WHERE d.damageReport = :status");
        query.setParameter("status", DamageReportEnum.RESOLVED);

        return query.getResultList();
    }
    
    @Override
    public DamageReportEntity retrieveDamageReportByDamageReportId(Long damageReportId) throws DamageReportNotFoundException {
        if (damageReportId == null) {
            throw new DamageReportNotFoundException("DamageReportNotFoundException: Damage Report Id is null!");
        }
        
        DamageReportEntity damageReportEntity = em.find(DamageReportEntity.class, damageReportId);
        
        if (damageReportEntity == null) {
            throw new DamageReportNotFoundException("DamageReportNotFoundException: Damage Report Id " + damageReportId + " does not exist!");
        }
        
        return damageReportEntity;
    }
    
    @Override
    public void updateDamageReportDetails(Long damageReportId, DamageReportEntity damageReportEntityToUpdate) throws UpdateDamageReportException, DamageReportNotFoundException {
        if (damageReportEntityToUpdate == null || damageReportId == null) {
            throw new UpdateDamageReportException("UpdateDamageReportException: Damage Report Entity/ID is null!");
        }
        
        DamageReportEntity existingDamageReportEntity = retrieveDamageReportByDamageReportId(damageReportId);
        
        //update description
        if (!existingDamageReportEntity.getDamageDescription().equals(damageReportEntityToUpdate.getDamageDescription())) {
            existingDamageReportEntity.setDamageDescription(damageReportEntityToUpdate.getDamageDescription());
        }
        
        //update photo link
        if (!existingDamageReportEntity.getDamagePhotoLink().equals(damageReportEntityToUpdate.getDamagePhotoLink())) {
            existingDamageReportEntity.setDamagePhotoLink(damageReportEntityToUpdate.getDamagePhotoLink());
        }
        
        try {
            validate(existingDamageReportEntity);
            em.merge(existingDamageReportEntity);
            em.flush();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateDamageReportException("UpdateDamageReportException: " + ex.getMessage());
        }
    }
    
    @Override
    public void resolveDamageReport(Long damageReportId) throws DamageReportNotFoundException, UpdateDamageReportException {
        DamageReportEntity damageReportEntity = retrieveDamageReportByDamageReportId(damageReportId);
        damageReportEntity.setDamageReport(DamageReportEnum.RESOLVED);
        
        try {
            validate(damageReportEntity);
            em.merge(damageReportEntity);
            em.flush();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateDamageReportException("UpdateDamageReportException: " + ex.getMessage());
        }
    }
    
    @Override
    public void deleteDamageReport(Long damageReportId) throws DamageReportNotFoundException, DeleteDamageReportException {
        DamageReportEntity damageReportEntity = retrieveDamageReportByDamageReportId(damageReportId);
        
        damageReportEntity.getCustomer().getDamageReports().remove(damageReportEntity);
        damageReportEntity.setCustomer(null);
        
        try {
            em.remove(damageReportEntity);
        } catch (PersistenceException ex) {
            throw new DeleteDamageReportException("DeleteDamageReportException: " + ex.getMessage());
        }
    }
    
    private void validate(DamageReportEntity damageReportEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<DamageReportEntity>> errors = validator.validate(damageReportEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
}

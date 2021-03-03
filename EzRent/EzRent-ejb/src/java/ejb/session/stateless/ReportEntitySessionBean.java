/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ListingEntity;
import entity.ReportEntity;
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
import util.exception.CreateNewReportException;
import util.exception.CustomerNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.ReportNotFoundException;
import util.exception.UpdateReportException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Ziyue
 */
@Stateless
public class ReportEntitySessionBean implements ReportEntitySessionBeanLocal {

    @EJB(name = "ListingEntitySessionBeanLocal")
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomerReport(Long reporterId, Long violatingCustomerId, ReportEntity reportEntity) throws CustomerNotFoundException, ListingNotFoundException, CreateNewReportException {
        
        if (reporterId == null) {
            throw new CreateNewReportException("CreateNewReportException: reporter ID can't be null!");
        }
        
        if (violatingCustomerId == null) {
            throw new CreateNewReportException("CreateNewReportException: violating customer ID can't be null!");
        }
        
        if (reportEntity == null) {
            throw new CreateNewReportException("CreateNewReportException: report can't be null!");
        }
        
        CustomerEntity reporter = customerEntitySessionBeanLocal.retrieveCustomerById(reporterId);
 
        CustomerEntity violatingCustomer = customerEntitySessionBeanLocal.retrieveCustomerById(violatingCustomerId);
        
        if (!reporter.equals(violatingCustomer)) {
            throw new CreateNewReportException("CreateNewReportException: Reporter can't report themselves!");
        }
        
        //Associate report with reporter
        reporter.getReports().add(reportEntity);
        reportEntity.setCustomer(reporter);
        
        //Associate report with violating customer
        reportEntity.setViolatingCustomer(violatingCustomer);
        
        try {
            validate(reportEntity);
            
            em.persist(reportEntity);
            em.flush();
            return reportEntity.getReportId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewReportException("CreateNewReportException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewReportException("CreateNewReportException: Report with same Report ID already exists!");
            } else {
            throw new CreateNewReportException("CreateNewReportException: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public Long createNewListingReport(Long reporterId, Long listingId, ReportEntity reportEntity) throws CustomerNotFoundException, ListingNotFoundException, CreateNewReportException {
        
        if (reporterId == null) {
            throw new CreateNewReportException("CreateNewReportException: reporter ID can't be null!");
        }
        
        if (listingId == null) {
            throw new CreateNewReportException("CreateNewReportException: listing ID can't be null!");
        }
        
        if (reportEntity == null) {
            throw new CreateNewReportException("CreateNewReportException: report can't be null!");
        }
        
        CustomerEntity reporter = customerEntitySessionBeanLocal.retrieveCustomerById(reporterId);
 
        ListingEntity listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
        
        if (!listingEntity.getLessor().equals(reporter)) {
            throw new CreateNewReportException("CreateNewReportException: Reporter cannot report their own listing!");
        }
        
        //Associate report with reporter
        reporter.getReports().add(reportEntity);
        reportEntity.setCustomer(reporter);
        
        //Associate report with listing
        reportEntity.setViolatingListing(listingEntity);
        
        try {
            validate(reportEntity);
            
            em.persist(reportEntity);
            em.flush();
            return reportEntity.getReportId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewReportException("CreateNewReportException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewReportException("CreateNewReportException: Report with same Report ID already exists!");
            } else {
            throw new CreateNewReportException("CreateNewReportException: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<ReportEntity> retrieveAllReports() {
        Query query = em.createQuery("SELECT r FROM ReportEntity r");
        return query.getResultList();
    }
    
    @Override
    public List<ReportEntity> retrieveAllOpenReports() {
        Query query = em.createQuery("SELECT r FROM ReportEntity r WHERE r.isClosed = FALSE");
        return query.getResultList();
    }
    
    @Override
    public List<ReportEntity> retrieveAllClosedReports() {
        Query query = em.createQuery("SELECT r FROM ReportEntity r WHERE r.isClosed = TRUE");
        return query.getResultList();
    }
    
    @Override
    public ReportEntity retrieveReportByReportId(Long reportId) throws ReportNotFoundException {
        if (reportId == null) {
            throw new ReportNotFoundException("ReportNotFoundException: Report ID is null!");
        }
        
        ReportEntity reportEntity = em.find(ReportEntity.class, reportId);
        
        if (reportEntity == null) {
             throw new ReportNotFoundException("ReportNotFoundException: Report ID " + reportId + " does not exist!");
        }
        
        return reportEntity;
    }
    
    @Override
    public void updateReportDetails(Long reportId, ReportEntity reportEntityToUpdate) throws UpdateReportException, ReportNotFoundException {
        if (reportEntityToUpdate == null || reportId == null) {
            throw new UpdateReportException("UpdateReportException: Report Entity/ID is null!");
        }
        
        ReportEntity existingReportEntity = retrieveReportByReportId(reportId);
        
        // update description
        existingReportEntity.setReportDescription(reportEntityToUpdate.getReportDescription());
        
        // update report type
        existingReportEntity.setReportIssue(reportEntityToUpdate.getReportIssue());

        try {
            validate(existingReportEntity);
            em.merge(existingReportEntity);
            em.flush();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateReportException("UpdateReportException: " + ex.getMessage());
        }
    }
    
    @Override
    public void closeReport(Long reportId) throws ReportNotFoundException {
        ReportEntity reportEntity = retrieveReportByReportId(reportId);
        
        reportEntity.setIsClosed(true);
        em.merge(reportEntity);
    }
    
    private void validate(ReportEntity reportEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ReportEntity>> errors = validator.validate(reportEntity);

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

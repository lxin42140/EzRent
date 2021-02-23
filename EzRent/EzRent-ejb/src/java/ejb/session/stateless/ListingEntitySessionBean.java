/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ListingEntity;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import util.exception.CreateNewListingException;


/**
 *
 * @author kiyon
 */
@Stateless
public class ListingEntitySessionBean implements ListingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createListing(CustomerEntity customer, ListingEntity listing) throws CreateNewListingException {
        
        if (listing.getMaxRentalDuration() < listing.getMinRentalDuration()) {
            throw new CreateNewListingException("CreateNewListingException: Maximum rental duration is less than minimum rental duration!");
        }
        
        validate(listing);
        
        try {
            em.persist(listing);
            em.flush();
            return listing.getListingId();
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewListingException("CreateNewListingException: Listing with same listing ID already exists!");
            } else {
                throw new CreateNewListingException("CreateNewListingException: " + ex.getMessage());
            }
        }
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(ListingEntity listing) throws CreateNewListingException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ListingEntity>> errors = validator.validate(listing);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new CreateNewListingException("CreateNewListingException: Invalid inputs!\n" + errorMessage);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.TagEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import util.enumeration.AvailabilityEnum;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.UpdateListingFailException;

/*
TODO:
    ENSURE THAT CATEGORIES ARE LEAF CATEGORIES
    
    Since listings can only belong to leaf categories, what happens when a new category is added?
*/


/**
 *
 * @author kiyon
 */
@Stateless
public class ListingEntitySessionBean implements ListingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    /*
    uncomment when Customer Entity SB is done
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    */

    @Override
    public Long createListing(CustomerEntity customer, ListingEntity listing, List<CategoryEntity> categories, List<TagEntity> tags) throws CreateNewListingException, CustomerNotFoundException {
        if (categories.isEmpty()) {
            throw new CreateNewListingException("CreateNewListingException: Listing must have a category!");
        }
        
        try {
            //set bidirectional relationship with lessor
            listing.setCustomer(customer);
            customer.getListings().add(listing);
            
            //set unidirectional relationship with category and tag
            for (CategoryEntity category : categories) {
                listing.getCategories().add(category);
            }
            
            for (TagEntity tag : tags) {
                listing.getTags().add(tag);
            }
            
            validateNewListing(listing);
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
    
    @Override
    public List<ListingEntity> retrieveAllListings() {
        Query query = em.createNamedQuery("SELECT l FROM ListingEntity l WHERE l.isDeleted = FALSE");
        return (List<ListingEntity>) query.getResultList();
    }
    
    @Override
    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException {
        Query query = em.createQuery("SELECT l FROM ListingEntity l WHERE l.listingId =:inListingId");
        query.setParameter("inListingId", listingId);
        
        try {
            ListingEntity listing = (ListingEntity) query.getSingleResult();
            
            if (listing.getIsDeleted() == true) {
                throw new ListingNotFoundException("ListingNotFoundException: Listing has been deleted!");
            }
            return listing;
        } catch(NoResultException ex) {
            throw new ListingNotFoundException("ListingNotFoundException: Listing id " + listingId + "does not exist!");
        }
    }
    
    //For users
    @Override
    public Long updateListingDetails(Long listingId, ListingEntity newListing) throws ListingNotFoundException, UpdateListingFailException {
        
        ListingEntity listing = retrieveListingByListingId(listingId);
        
        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new UpdateListingFailException("UpdateListingFailException: Cannot update listing that is not available!");
        }
        
        listing.setListingName(newListing.getListingName());
        listing.setPrice(newListing.getPrice());
        listing.setDescription(newListing.getDescription());
        listing.setLocation(newListing.getLocation());
        listing.setMinRentalDuration(newListing.getMinRentalDuration());
        listing.setMaxRentalDuration(newListing.getMaxRentalDuration());
        listing.setItemCondition(newListing.getItemCondition());
        listing.setDeliveryOption(newListing.getDeliveryOption());
        listing.setModeOfPayment(newListing.getModeOfPayment());
        
        validateUpdatedListing(listing);
        
        em.merge(listing);
        em.flush();
        
        return listing.getListingId();
    }
    
    @Override
    //throws CustomerNotFoundException too
    public void likeListing(Long customerId, Long listingId) throws ListingNotFoundException {
        ListingEntity listing = retrieveListingByListingId(listingId);
        //CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerByCustId(customerId);
        //remove bottom code with above code when method has been created
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.userId =:inUserId");
        query.setParameter("inUserId", customerId);
        List<CustomerEntity> customerList = (List<CustomerEntity>) query.getResultList();
        CustomerEntity customer = customerList.get(0);
        
        boolean isLiked = false;
        
        for (CustomerEntity currentLikedCustomer : listing.getLikedCustomers()) {
            if (currentLikedCustomer.getUserId().equals(customerId)) {
                isLiked = true;
                //remove customer to unlike the listing, remove association
                listing.getLikedCustomers().remove(currentLikedCustomer);
                currentLikedCustomer.getLikedListings().remove(listing);
                break;
            }
        }
        
        if (!isLiked) {
            listing.getLikedCustomers().add(customer);
            customer.getLikedListings().add(listing);
        }
    }
    
    @Override
    public Long deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException {
        ListingEntity listing = retrieveListingByListingId(listingId);
        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new DeleteListingException("DeleteListingException: Cannot delete listing that is not available!");
        }
        listing.setDeleted();
        return listingId;
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
    
    private void validateNewListing(ListingEntity listing) throws CreateNewListingException {
        String errorMessage = validate(listing);
        
        if (errorMessage.length() > 0) {
            throw new CreateNewListingException("CreateNewListingException: Invalid inputs!\n" + errorMessage);
        }
    }
    
    private void validateUpdatedListing(ListingEntity listing) throws UpdateListingFailException {
        String errorMessage = validate(listing);
        
        if (errorMessage.length() > 0) {
            throw new UpdateListingFailException("UpdateListingFailException: Invalid inputs!\n" + errorMessage);
        }
    }

    private String validate(ListingEntity listing) {       
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ListingEntity>> errors = validator.validate(listing);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }
        
        if (listing.getMaxRentalDuration() < listing.getMinRentalDuration()) {
            errorMessage += "\n\t + Maximum rental duration is less than minimum rental duration";
        }
        
        return errorMessage;
    }
    
}

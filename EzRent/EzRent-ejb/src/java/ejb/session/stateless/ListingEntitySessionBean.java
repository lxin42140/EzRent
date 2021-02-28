/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.CommentEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.TagEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import util.enumeration.AvailabilityEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CommentNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateListingFailException;

/*
TODO:   
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

    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;
    private CommentEntitySessionBeanLocal commentEntitySessionBeanLocal;
    

    @Override
    public Long createListing(Long customerId, ListingEntity listing, List<Long> categoriesId, List<Long> tagsId) throws CreateNewListingException, CustomerNotFoundException, CategoryNotFoundException, TagNotFoundException {
        if (listing == null) {
            throw new CreateNewListingException("CreateNewListingException: Invalid listing!");
        } else if (categoriesId.isEmpty()) {
            throw new CreateNewListingException("CreateNewListingException: Listing must have a category!");
        }
        
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        
        try {
            listing.setLessor(customer);
            customer.getListings().add(listing);
            
            for (Long categoryId : categoriesId) {
                CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryById(categoryId);
                if (!category.getSubCategories().isEmpty()) {
                    throw new CreateNewListingException("CreateNewListingException: Category selected must be leaf category!");
                }
                listing.getCategories().add(category);
            }
            
            for (Long tagId : tagsId) {
                TagEntity tag = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
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
        if (listingId == null) {
            throw new ListingNotFoundException("ListingNotFoundException: Listing id is null!");
        }
        
        ListingEntity listing = em.find(ListingEntity.class, listingId);
        if (listing == null || listing.getIsDeleted()) {
            throw new ListingNotFoundException("ListingNotFoundException: Listing id " + listingId + "does not exist!");
        }
        
        return listing;
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
    public void likeListing(Long customerId, Long listingId) throws ListingNotFoundException, CustomerNotFoundException {
        ListingEntity listing = retrieveListingByListingId(listingId);
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        
        boolean isLiked = false;
        
        List<CustomerEntity> likedCustomerList = listing.getLikedCustomers();
        //unlike a listing
        for (CustomerEntity currentLikedCustomer : likedCustomerList) {
            if (currentLikedCustomer.getUserId().equals(customerId)) {
                isLiked = true;
                likedCustomerList.remove(currentLikedCustomer);
                currentLikedCustomer.getLikedListings().remove(listing);
                break;
            }
        }
        
        //like a listing
        if (!isLiked) {
            listing.getLikedCustomers().add(customer);
            customer.getLikedListings().add(listing);
        }
    }
    
    @Override
    public Long deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException, OfferNotFoundException, CommentNotFoundException {
        ListingEntity listing = retrieveListingByListingId(listingId);
        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new DeleteListingException("DeleteListingException: Cannot delete listing that is not available!");
        }
        listing.setDeleted();
        
        listing.getLessor().getListings().remove(listing); //remove listing from lessor
        for (CustomerEntity likedCustomer : listing.getLikedCustomers()) { //remove listing from customer's liked listings
            likedCustomer.getLikedListings().remove(listing);
        }
        
        for (OfferEntity offer : listing.getOffers()) {
            offerEntitySessionBeanLocal.rejectOffer(offer.getOfferId());
        }
        
        for (CommentEntity comment : listing.getComments()) {
            commentEntitySessionBeanLocal.deleteComment(comment.getCommentId());
        }
        
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

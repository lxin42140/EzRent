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
import util.exception.DeleteCommentException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateListingFailException;
import util.exception.UpdateOfferException;
import util.exception.ValidationFailedException;

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
    public ListingEntity createNewListing(Long customerId, Long categoryId, List<Long> tagsId, ListingEntity listing) throws CreateNewListingException, CustomerNotFoundException, CategoryNotFoundException, TagNotFoundException {
        if (customerId == null) {
            throw new CreateNewListingException("CreateNewListingException: Invalid customer id!");
        }
        if (listing == null) {
            throw new CreateNewListingException("CreateNewListingException: Invalid listing!");
        }
        if (categoryId == null) {
            throw new CreateNewListingException("CreateNewListingException: Listing must have a category!");
        }

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryById(categoryId);

        //bi-associate listing with owner
        listing.setListingOwner(customer);
        customer.getListings().add(listing);

        //bi-associate listing with category
        listing.setCategory(category);
        category.getListings().add(listing);

        //bi-associate listing with tags, if any
        if (tagsId != null && !tagsId.isEmpty()) {
            for (Long tagId : tagsId) {
                TagEntity tag = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                listing.getTags().add(tag);
                tag.getListings().add(listing);
            }
        }

        try {
            validate(listing);

            em.persist(listing);
            em.flush();
            return listing;
        } catch (ValidationFailedException ex) {
            throw new CreateNewListingException("CreateNewListingException: " + ex.getMessage());
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
        Query query = em.createQuery("SELECT l FROM ListingEntity l WHERE l.isDeleted = FALSE");
        return query.getResultList();
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

    @Override
    public List<ListingEntity> retrieveListingByCustomerId(Long customerId) throws CustomerNotFoundException {
        if(customerId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Please enter a valid customer ID!");
        }
        
        Query query = em.createQuery("select l from ListingEntity l where l.listingOwner =: incustomerId and l.isDeleted = FALSE");
        query.setParameter("incustomerId", customerId);
        
        return query.getResultList();
    }
    //For users
    @Override
    public ListingEntity updateListingDetails(ListingEntity newListing, Long newCategoryId, List<Long> newTagIds) throws ListingNotFoundException, UpdateListingFailException {

        ListingEntity listing = retrieveListingByListingId(newListing.getListingId());

        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new UpdateListingFailException("UpdateListingFailException: Cannot update listing that is not available for rental!");
        }

        try {
            listing.setListingName(newListing.getListingName());
            listing.setPrice(newListing.getPrice());
            listing.setDescription(newListing.getDescription());
            listing.setLocation(newListing.getLocation());
            listing.setMinRentalDuration(newListing.getMinRentalDuration());
            listing.setMaxRentalDuration(newListing.getMaxRentalDuration());
            listing.setItemCondition(newListing.getItemCondition());
            listing.setDeliveryOption(newListing.getDeliveryOption());
            listing.setModeOfPayment(newListing.getModeOfPayment());

            if (newCategoryId != null && newCategoryId.equals(listing.getCategory().getCategoryId())) {
                listing.setCategory(categoryEntitySessionBeanLocal.retrieveCategoryById(newCategoryId));
            }

            // remove all existing tags
            for (TagEntity tag : listing.getTags()) {
                tag.getListings().remove(listing);
            }
            listing.getTags().clear();

            // add new tag to listing
            for (Long tagId : newTagIds) {
                TagEntity newTag = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                listing.getTags().add(newTag);
                newTag.getListings().add(listing);
            }

            validate(listing);
            em.merge(listing);
            em.flush();
            return listing;
        } catch (ValidationFailedException | PersistenceException | CategoryNotFoundException | TagNotFoundException ex) {
            throw new UpdateListingFailException("UpdateListingFailException: " + ex.getMessage());
        }
    }

    @Override
    public void toggleListingLikeDislike(Long customerId, Long listingId) throws ListingNotFoundException, CustomerNotFoundException {
        ListingEntity listing = this.retrieveListingByListingId(listingId);
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        //dislike a listing
        if (customer.getLikedListings().contains(listing)) {
            listing.getLikedCustomers().remove(customer);
            customer.getLikedListings().remove(listing);
        } else {
            listing.getLikedCustomers().add(customer);
            customer.getLikedListings().add(listing);
        }

        em.merge(listing);
    }

    @Override
    public void deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException {
        ListingEntity listing = retrieveListingByListingId(listingId);

        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new DeleteListingException("DeleteListingException: Cannot delete listing that is not available!");
        }

        listing.setIsDeleted(true);

        try {
            // remove listing from customer's liked listings
            for (CustomerEntity likedCustomer : listing.getLikedCustomers()) {
                likedCustomer.getLikedListings().remove(listing); // remove listing from liked customer
                listing.getLikedCustomers().remove(likedCustomer); // remove customer from liekd listing
            }

            // reject every offer associated with listing
            for (OfferEntity offer : listing.getOffers()) {
                offerEntitySessionBeanLocal.rejectOffer(offer.getOfferId());
            }

            // remove every comment associated with the listing
            for (CommentEntity comment : listing.getComments()) {
                commentEntitySessionBeanLocal.deleteCommentForListing(comment.getCommentId());
            }

            em.merge(listing);
        } catch (OfferNotFoundException | CommentNotFoundException | DeleteCommentException | UpdateOfferException ex) {
            em.getTransaction().rollback();
            throw new DeleteListingException("DeleteListingException: " + ex.getMessage());
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(ListingEntity listing) throws ValidationFailedException {
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
        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

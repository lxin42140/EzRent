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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import javax.ejb.EJB;
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
import util.exception.LikeListingException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.RetrievePopularListingsException;
import util.exception.TagNotFoundException;
import util.exception.UpdateListingFailException;
import util.exception.UpdateOfferException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class ListingEntitySessionBean implements ListingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;
    @EJB
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
    public List<ListingEntity> retrieveListingsByListingName(String listingName) {
        Query query = em.createQuery("select l from ListingEntity l where l.listingName like :inListingName");
        query.setParameter("inListingName", "%" + listingName + "%");
        return query.getResultList();
    }

    @Override
    public List<ListingEntity> retrieveListingByCustomerId(Long customerId) throws CustomerNotFoundException {
        if (customerId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Please enter a valid customer ID!");
        }

        Query query = em.createQuery("select l from ListingEntity l where l.listingOwner =: incustomerId and l.isDeleted = FALSE");
        query.setParameter("incustomerId", customerId);

        return query.getResultList();
    }

    @Override
    public List<ListingEntity> retrieveMostPopularListingsForCategory(Long categoryId, Long customerId) throws RetrievePopularListingsException {
        if (categoryId == null || customerId == null) {
            throw new RetrievePopularListingsException("RetrievePopularListingsException: Please provide valid category/customer ID!");
        }

        List<ListingEntity> recommendedListings = new ArrayList<>();
        Query query = em.createQuery("select l from ListingEntity l where l.category =:inCategoryId and l.listingOwner !=:inCustomerId");
        query.setParameter("inCategoryId", categoryId);
        query.setParameter("inCustomerId", customerId);

        PriorityQueue<ListingEntity> pq = new PriorityQueue<>(query.getResultList());
        for (int i = 0; i < 3 && !pq.isEmpty(); i++) {
            recommendedListings.add(pq.poll());
        }
        return recommendedListings;
    }

    @Override
    public ListingEntity retrieveLatestListing() {
        Query query = em.createQuery("select l from ListingEntity l order by l.dateOfPost DESC");
        return (ListingEntity) query.getResultList().get(0);
    }

    @Override
    public ListingEntity retrieveMostPopularListing() {
        Query query = em.createQuery("select l from ListingEntity l");
        PriorityQueue<ListingEntity> pq = new PriorityQueue<>(query.getResultList());
        return pq.poll();
    }

    @Override
    public List<ListingEntity> retrieveFavouriteListingsForCustomer(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        return customerEntity.getLikedListings();
    }

    @Override
    public List<ListingEntity> retrieveListingsByCategoryName(String categoryName) {
        Query query = em.createQuery("select l from ListingEntity l where l.category.categoryName like :inCategoryName");
        query.setParameter("inCategoryName", "%" + categoryName + "%");
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
    public void toggleListingLikeDislike(Long customerId, Long listingId) throws LikeListingException, ListingNotFoundException, CustomerNotFoundException {
        ListingEntity listing = this.retrieveListingByListingId(listingId);
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        if (listing.getListingOwner().equals(customer)) {
            throw new LikeListingException("LikeListingException: Cannot like own listings!");
        }
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

    //NOT PROVIDED YET
//    @Override
//    public List<ListingEntity> retrieveListingsByTags(List<Long> tagIds, String condition) {
//        List<ListingEntity> listingEntitys = new ArrayList<>();
//
//        if (tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR"))) {
//            return listingEntitys;
//        } else {
//            if (condition.equals("OR")) {
//                Query query = em.createQuery("SELECT DISTINCT l FROM ListingEntity l, IN (l.tags) te WHERE te.tagId IN :inTagIds ORDER BY l.listingName ASC");
//                query.setParameter("inTagIds", tagIds);
//                listingEntitys = query.getResultList();
//            } else // AND
//            {
//                String selectClause = "SELECT l FROM  ListingEntity l";
//                String whereClause = "";
//                Boolean firstTag = true;
//                Integer tagCount = 1;
//
//                for (Long tagId : tagIds) {
//                    selectClause += ", IN (l.tags) te" + tagCount;
//
//                    if (firstTag) {
//                        whereClause = "WHERE te1.tagId = " + tagId;
//                        firstTag = false;
//                    } else {
//                        whereClause += " AND te" + tagCount + ".tagId = " + tagId;
//                    }
//
//                    tagCount++;
//                }
//
//                String jpql = selectClause + " " + whereClause + " ORDER BY l.listingName ASC";
//                Query query = em.createQuery(jpql);
//                listingEntitys = query.getResultList();
//            }
//
//            Collections.sort(listingEntitys, (x, y) -> x.getListingName().compareTo(y.getListingName()));
//            return listingEntitys;
//        }
//    }
    public void persist(Object object) {
        em.persist(object);
    }
}

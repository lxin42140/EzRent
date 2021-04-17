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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
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
//import util.exception.CommentNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
//import util.exception.DeleteCommentException;
import util.exception.DeleteListingException;
import util.exception.ToggleListingLikeUnlikeException;
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
//    @EJB
//    private CommentEntitySessionBeanLocal commentEntitySessionBeanLocal;

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

        //associate listing with category
        listing.setCategory(category);

        //associate listing with tags, if any
        if (tagsId != null && !tagsId.isEmpty()) {
            for (Long tagId : tagsId) {
                TagEntity tag = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                listing.getTags().add(tag);
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

    //retrieve listings of the particular user
    @Override
    public List<ListingEntity> retrieveAllListingByCustId(Long custId) throws CustomerNotFoundException {
        if (custId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer id is null");
        }

        Query query = em.createQuery("SELECT l FROM ListingEntity l WHERE l.listingOwner.userId = :inCustId AND l.isDeleted = FALSE");
        query.setParameter("inCustId", custId);
        List<ListingEntity> list = query.getResultList();

        return query.getResultList();
    }

    @Override
    public List<ListingEntity> retrieveListingsByListingName(String listingName) throws ListingNotFoundException {
        if (listingName == null || listingName.length() == 0) {
            throw new ListingNotFoundException("ListingNotFoundException: Listing name is empty!");
        }
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
        Query query = em.createQuery("select l from ListingEntity l where l.category.categoryId =:inCategoryId and l.listingOwner.userId !=:inCustomerId");
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
        Query query = em.createQuery("select l from ListingEntity l, in (l.likedCustomers) c where c.userId =:inCustomerId");
        query.setParameter("inCustomerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<ListingEntity> retrieveListingsByCategoryName(String categoryName) {
        Query query = em.createQuery("select l from ListingEntity l where l.category.categoryName like :inCategoryName");
        query.setParameter("inCategoryName", "%" + categoryName + "%");

        if (query.getResultList().isEmpty()) {
            Query query1 = em.createQuery("SELECT c FROM CategoryEntity c WHERE c.categoryName =:inCategoryName");
            query1.setParameter("inCategoryName", categoryName);
            CategoryEntity category = (CategoryEntity) query1.getSingleResult();
            if (!category.getSubCategories().isEmpty()) {
                List<ListingEntity> allSubCategoryListings = new ArrayList<>();
                for (CategoryEntity categoryEntity : category.getSubCategories()) {
                    Query query3 = em.createQuery("select l from ListingEntity l where l.category.categoryId =:inCategoryId");
                    query3.setParameter("inCategoryId", categoryEntity.getCategoryId());
                    allSubCategoryListings.addAll(query3.getResultList());
                }
                return allSubCategoryListings;
            }
        }
        return query.getResultList();
    }

    @Override
    public List<ListingEntity> retrieveListingsByTags(List<Long> tagIds) {
        List<ListingEntity> listingEntitys = new ArrayList<>();

        if (tagIds == null || tagIds.isEmpty()) {
            return listingEntitys;
        } else {
            String selectClause = "SELECT l FROM  ListingEntity l";
            String whereClause = "";
            Boolean firstTag = true;
            Integer tagCount = 1;

            for (Long tagId : tagIds) {
                selectClause += ", IN (l.tags) te" + tagCount;

                if (firstTag) {
                    whereClause = "WHERE te1.tagId = " + tagId;
                    firstTag = false;
                } else {
                    whereClause += " AND te" + tagCount + ".tagId = " + tagId;
                }

                tagCount++;
            }

            String jpql = selectClause + " " + whereClause + " ORDER BY l.listingName ASC";
            Query query = em.createQuery(jpql);
            listingEntitys = query.getResultList();

            Collections.sort(listingEntitys, (x, y) -> x.getListingName().compareTo(y.getListingName()));
            return listingEntitys;
        }
    }

    @Override
    public List<ListingEntity> retrieveListingsByTag(Long tagId) {
        try {
            List<ListingEntity> allListings = this.retrieveAllListings();
            Iterator<ListingEntity> iterator = allListings.iterator();
            TagEntity tag = tagEntitySessionBeanLocal.retrieveTagByTagId(1l);
            while (iterator.hasNext()) {
                ListingEntity listing = iterator.next();

                if (!listing.getTags().contains(tag)) {
                    iterator.remove();
                }
            }
            return allListings;
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    //For users
    @Override
    public ListingEntity updateListingDetails(ListingEntity newListing, Long newCategoryId, List<Long> newTagIds) throws ListingNotFoundException, UpdateListingFailException {

        ListingEntity listing = retrieveListingByListingId(newListing.getListingId());

        if (listing.getAvailability() != AvailabilityEnum.AVAILABLE) {
            throw new UpdateListingFailException("UpdateListingFailException: Cannot update listing that is not available for rental!");
        }

        try {
            if (newListing.getListingName() != null && !newListing.getListingName().equals(listing.getListingName())) {
                listing.setListingName(newListing.getListingName());
            }
            if (newListing.getPrice() != null && !newListing.getPrice().equals(listing.getPrice())) {
                listing.setPrice(newListing.getPrice());
            }
            if (newListing.getDescription() != null && !newListing.getDescription().equals(listing.getDescription())) {
                listing.setDescription(newListing.getDescription());
            }
            if (newListing.getLocation() != null && !newListing.getLocation().equals(listing.getLocation())) {
                listing.setLocation(newListing.getLocation());
            }
            if (newListing.getMinRentalDuration() != null && !newListing.getMinRentalDuration().equals(listing.getMinRentalDuration())) {
                listing.setMinRentalDuration(newListing.getMinRentalDuration());
            }
            if (newListing.getMaxRentalDuration() != null && !newListing.getMaxRentalDuration().equals(listing.getMaxRentalDuration())) {
                listing.setMaxRentalDuration(newListing.getMaxRentalDuration());
            }
            if (newListing.getItemCondition() != null && !newListing.getItemCondition().equals(listing.getItemCondition())) {
                listing.setItemCondition(newListing.getItemCondition());
            }
            if (newListing.getDeliveryOption() != null && !newListing.getDeliveryOption().equals(listing.getDeliveryOption())) {
                listing.setDeliveryOption(newListing.getDeliveryOption());
            }
            if (newListing.getModeOfPayment() != null && !newListing.getModeOfPayment().equals(listing.getModeOfPayment())) {
                listing.setModeOfPayment(newListing.getModeOfPayment());
            }
            if (newListing.getFilePathName() != null && !newListing.getFilePathName().equals(listing.getFilePathName())) {
                listing.setFilePathName(newListing.getFilePathName());
            }

            if (newCategoryId != null && newCategoryId != 0 && !newCategoryId.equals(listing.getCategory().getCategoryId())) {
                listing.setCategory(categoryEntitySessionBeanLocal.retrieveCategoryById(newCategoryId));
            }

            if (newTagIds != null && !newTagIds.isEmpty()) {
                listing.getTags().clear();
                // add new tag to listing
                for (Long tagId : newTagIds) {
                    TagEntity newTag = tagEntitySessionBeanLocal.retrieveTagByTagId(tagId);
                    listing.getTags().add(newTag);
                }
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
    public void toggleListingLikeDislike(Long customerId, Long listingId) throws ToggleListingLikeUnlikeException, ListingNotFoundException, CustomerNotFoundException {
        ListingEntity listing = this.retrieveListingByListingId(listingId);
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        
        em.refresh(listing);
        em.refresh(customer);
        
        if (listing.getListingOwner().equals(customer)) {
            throw new ToggleListingLikeUnlikeException("LikeListingException: Cannot like own listings!");
        }
        try {
            //dislike a listing
            if (customer.getLikedListings().contains(listing)) {
                customer.getLikedListings().remove(listing);
                listing.getLikedCustomers().remove(customer);
            } else {
                customer.getLikedListings().add(listing);
                listing.getLikedCustomers().add(customer);
            }
            
            em.flush();
        } catch (Exception ex) {
            throw new ToggleListingLikeUnlikeException("Something went wrong! " + ex.getMessage());
        }
    }

    @Override
    public void markListingAsPopular(Long listingId) throws ListingNotFoundException, TagNotFoundException {
        ListingEntity listingEntity = this.retrieveListingByListingId(listingId);
        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagName("Popular");
        listingEntity.getTags().add(tagEntity);
        em.merge(listingEntity);
    }

    @Override
    public void unmarkListingAsPopular(Long listingId) throws ListingNotFoundException, TagNotFoundException {
        ListingEntity listingEntity = this.retrieveListingByListingId(listingId);
        TagEntity tagEntity = tagEntitySessionBeanLocal.retrieveTagByTagName("Popular");
        listingEntity.getTags().remove(tagEntity);
        em.merge(listingEntity);
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
            List<CommentEntity> comments = new ArrayList<>(listing.getComments());
            listing.getComments().clear();
            for (CommentEntity comment : comments) {
                comment.setListing(null);
            }

            em.merge(listing);

            for (CommentEntity comment : comments) {
                em.remove(comment);
            }

        } catch (OfferNotFoundException | UpdateOfferException ex) {
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

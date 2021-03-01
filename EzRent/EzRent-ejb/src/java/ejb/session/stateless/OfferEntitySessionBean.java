/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.OfferStatusEnum;
import util.exception.CreateNewOfferException;
import util.exception.CustomerNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UpdateOfferException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class OfferEntitySessionBean implements OfferEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @Override
    public Long createNewOffer(OfferEntity offer, Long customerId, Long listingId) throws CreateNewOfferException, ListingNotFoundException, CustomerNotFoundException {

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);

        // check whether the lessor is making offer on his own listing
        if (listing.getLessor().getUserId().equals(customerId)) {
            throw new CreateNewOfferException("CreateNewOfferException: User cannot create an offer on his/her own listing!");
        }

        //bi-associate offer with listing
        listing.getOffers().add(offer);
        offer.setListing(listing);

        //set bidirectional relationship between offer and customer
        customer.getOffers().add(offer);
        offer.setCustomer(customer);

        try {
            validate(offer);

            em.persist(offer);
            em.flush();
            return offer.getOfferId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewOfferException("CreateNewOfferException: " + ex.getMessage());

        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewOfferException("CreateNewOfferException: Offer with same offer ID already exists!");
            } else {
                throw new CreateNewOfferException("CreateNewOfferException: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<OfferEntity> retrieveAllOffers() {
        Query query = em.createQuery("SELECT o FROM OfferEntity o");
        return query.getResultList();
    }

    @Override
    public OfferEntity retrieveOfferByOfferId(Long offerId) throws OfferNotFoundException {
        if (offerId == null) {
            throw new OfferNotFoundException("OfferNotFoundException: Offer id is null!");
        }

        OfferEntity offer = em.find(OfferEntity.class, offerId);

        if (offer == null) {
            throw new OfferNotFoundException("OfferNotFoundException: Offer id " + offerId + " does not exist!");
        }
        return offer;
    }

    @Override
    public List<OfferEntity> retrieveAllOffersByCustomer(Long customerId) {
        Query query = em.createQuery("SELECT o FROM OfferEntity o WHERE o.customer.userId =:inUserId SORT BY o.offerStatus DESC, o.dateOffered ASC");
        query.setParameter("inUserId", customerId);
        return query.getResultList();
    }

    @Override
    public void acceptOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException {

        OfferEntity offer = this.retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.ACCEPTED);

        //create new transaction
        
        em.merge(offer);
    }

    @Override
    public void rejectOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.REJECTED);
        em.merge(offer);
    }

    @Override
    public void cancelOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.CANCELLED);

        //delete any created transaction
        
        em.merge(offer);
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(OfferEntity offer) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<OfferEntity>> errors = validator.validate(offer);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

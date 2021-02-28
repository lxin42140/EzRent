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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.OfferStatusEnum;
import util.exception.CreateNewOfferException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;

/**
 *
 * @author kiyon
 */
@Stateless
public class OfferSessionBean implements OfferSessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    /*
    uncomment when Customer Entity SB is done
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    */
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    //throws CustomerNotFoundException too
    public Long createOffer(OfferEntity offer, Long customerId, Long listingId) throws CreateNewOfferException, ListingNotFoundException {
        
        //CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerByCustId(customerId);
        //remove bottom code with above code when method has been created
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.userId =:inUserId");
        query.setParameter("inUserId", customerId);
        List<CustomerEntity> customerList = (List<CustomerEntity>) query.getResultList();
        CustomerEntity customer = customerList.get(0);
        
        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
        
        if (listing.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new CreateNewOfferException("CreateNewOfferException: User cannot create an offer on his/her own listing!");
        }
        
        //set bidirectional relationship between listing and offer
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
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewOfferException("CreateNewOfferException: Offer with same offer ID already exists!");
            } else {
                throw new CreateNewOfferException("CreateNewOfferException: " + ex.getMessage());
            }
        }
    }
    
    public List<OfferEntity> retrieveAllOffers() {
        Query query = em.createQuery("SELECT o FROM OfferEntity o");
        return (List<OfferEntity>) query.getResultList();
    }
    
    public OfferEntity retrieveOfferByOfferId(Long offerId) throws OfferNotFoundException {
        Query query = em.createQuery("SELECT o FROM OfferEntity o WHERE o.offerId =:inOfferId");
        query.setParameter("inOfferId", offerId);
        
        try {
            return (OfferEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new OfferNotFoundException("OfferNotFoundException: Offer id " + offerId + " does not exist!");
        }
    }
    
    public List<OfferEntity> retrieveAllOffersByCustomer(Long customerId) {
        Query query = em.createQuery("SELECT o FROM OfferEntity o WHERE o.customer.userId =:inUserId SORT BY o.offerStatus DESC, o.dateOffered ASC");
        query.setParameter("inUserId", customerId);
        return (List<OfferEntity>) query.getResultList();
    }
    
    //Called by system
    public void acceptOffer(Long offerId) throws OfferNotFoundException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);
        offer.setOfferStatus(OfferStatusEnum.ACCEPTED);
    }
    
    //Called by system
    public void rejectOffer(Long offerId) throws OfferNotFoundException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);
        offer.setOfferStatus(OfferStatusEnum.REJECTED);
    }
    
    //Called by user
    public void cancelOffer(Long offerId) throws OfferNotFoundException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);
        offer.setOfferStatus(OfferStatusEnum.CANCELLED);
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
    
    private void validate(OfferEntity offer) throws CreateNewOfferException {       
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<OfferEntity>> errors = validator.validate(offer);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }
        
        if (errorMessage.length() > 0) {
            throw new CreateNewOfferException("CreateNewOfferException: Invalid inputs!\n" + errorMessage);
        }
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Schedule;
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
import util.enumeration.PaymentStatusEnum;
import util.exception.CreateNewOfferException;
import util.exception.CustomerNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateOfferException;
import util.exception.UpdateTransactionStatusException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class OfferEntitySessionBean implements OfferEntitySessionBeanLocal {

    @EJB(name = "TransactionEntitySessionBeanLocal")
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @Override
    public Long createNewOffer(OfferEntity offer, Long customerId, Long listingId) throws CreateNewOfferException, ListingNotFoundException, CustomerNotFoundException {

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);

        // check whether the lessor is making offer on his own listing
        if (listing.getListingOwner().getUserId().equals(customerId)) {
            throw new CreateNewOfferException("CreateNewOfferException: User cannot create an offer on his/her own listing!");
        }

        //bi-associate offer with listing
        listing.getOffers().add(offer);
        offer.setListing(listing);

        //set bidirectional relationship between offer and customer
        customer.getOffers().add(offer);
        offer.setCustomer(customer);

        // set timestamp
        Calendar cal = Calendar.getInstance();
        offer.setLastUpdatedDate(cal.getTime());

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

    //when offer is accepted, a transaction and payment entity needs to be created
    @Override
    public void acceptOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException {

        OfferEntity offer = this.retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.ACCEPTED);
        Calendar cal = Calendar.getInstance();
        offer.setLastUpdatedDate(cal.getTime());
        /*
        
        
        Need to create new payment and transaction
        
         */

        em.merge(offer);
    }

    @Override
    public void rejectOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.REJECTED);
        Calendar cal = Calendar.getInstance();
        offer.setLastUpdatedDate(cal.getTime());
        em.merge(offer);
    }

    @Override
    // cancell created transaction
    public void cancelOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException, UpdateTransactionStatusException, TransactionNotFoundException {
        OfferEntity offer = retrieveOfferByOfferId(offerId);

        if (offer.getOfferStatus() != OfferStatusEnum.ONGOING) {
            throw new UpdateOfferException("UpdateOfferException: Offer is no longer pending action!");
        }

        offer.setOfferStatus(OfferStatusEnum.CANCELLED);
        Calendar cal = Calendar.getInstance();
        offer.setLastUpdatedDate(cal.getTime());

        if (offer.getTransaction() != null) {
            transactionEntitySessionBeanLocal.markTransactionCancelled(offer.getTransaction().getTransactionId());
        }

        em.merge(offer);
    }

    @Schedule(hour = "12", info = "automateOfferCancellation")
    @Override
    public void automateOfferCancellation() {
        try {
            List<OfferEntity> offers = this.retrieveAllOffers();
            Date today = new Date();
            for (OfferEntity offer : offers) {
                if (offer.getOfferStatus() == OfferStatusEnum.ACCEPTED
                        && offer.getTransaction().getPayment().getPaymentStatus() == PaymentStatusEnum.UNPAID
                        && today.getTime() - offer.getLastUpdatedDate().getTime() >= 86400000) { // >= 24 hours
                    this.cancelOffer(offer.getOfferId());
                }
            }
        } catch (OfferNotFoundException | TransactionNotFoundException | UpdateOfferException | UpdateTransactionStatusException ex) {
            // Need to log the exception
        }
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

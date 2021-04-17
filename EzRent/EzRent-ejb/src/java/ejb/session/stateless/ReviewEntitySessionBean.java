/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ReviewEntity;
import entity.TransactionEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import util.exception.CreateNewReviewException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReviewException;
import util.exception.ReviewNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class ReviewEntitySessionBean implements ReviewEntitySessionBeanLocal {

    @EJB
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewReview(Long customerId, Long transactionId, ReviewEntity newReview) throws CreateNewReviewException, TransactionNotFoundException, CustomerNotFoundException {
        if (customerId == null) {
            throw new CreateNewReviewException("CreateNewReviewException: customer id is null!");
        }

        if (transactionId == null) {
            throw new CreateNewReviewException("CreateNewReviewException: transaction id is null!");
        }
        if (newReview == null) {
            throw new CreateNewReviewException("CreateNewReviewException: Please provide a valid review!");
        }

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        TransactionEntity transaction = transactionEntitySessionBeanLocal.retrieveTransactionByTransactionId(transactionId);

        //bi-associate with customer
        newReview.setCustomer(customer);
        customer.getReviews().add(newReview);

        //bi-associate with transaction
        newReview.setTransaction(transaction);
        transaction.getReviews().add(newReview);

        try {
            validate(newReview);

            em.persist(newReview);
            em.flush();
            
            if (transaction.getOffer().getCustomer().getUserId().equals(customerId)) {
                //means customer is leaving review for the listing owner
                updateAverageRatingForCustomer(transaction.getOffer().getListing().getListingOwner().getUserId(), newReview.getReviewId());
            } else {
                updateAverageRatingForCustomer(transaction.getOffer().getCustomer().getUserId(), newReview.getReviewId());
            }
            
            
            
            return newReview.getReviewId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewReviewException("CreateNewReviewException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewReviewException("CreateNewReviewException: Review with same ID already exists!");
            } else {
                throw new CreateNewReviewException("CreateNewReviewException: " + ex.getMessage());
            }
        } catch (ReviewNotFoundException ex){
            throw new CreateNewReviewException("CreateNewReviewException: " + ex.getMessage());
        }
    }

    @Override
    public List<ReviewEntity> retrieveAllReviewsCreatedByCustomer(Long customerId) throws CustomerNotFoundException {
        if (customerId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: customer id is null!");
        }
        Query query = em.createQuery("Select r from ReviewEntity r where r.customer.userId =:inUserId");
        query.setParameter("inUserId", customerId);

        return query.getResultList();
    }

    @Override
    public List<ReviewEntity> retrieveAllReviewsOnCustomer(Long customerId) throws CustomerNotFoundException {
        if (customerId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: customer id is null!");
        }
        
        Query query = em.createQuery("select r from ReviewEntity r where r.transaction.offer.customer.userId =:inCustomerId and not r.customer.userId =:inCustomerId");
        query.setParameter("inCustomerId", customerId);
        
        Query secondQuery = em.createQuery("select r from ReviewEntity r where r.transaction.offer.listing.listingOwner.userId =:inCustomerId and not r.customer.userId =:inCustomerId");
        secondQuery.setParameter("inCustomerId", customerId);

        return (List<ReviewEntity>)Stream.concat(query.getResultList().stream(), secondQuery.getResultList().stream()).collect(Collectors.toList());
        
//        return this.customerEntitySessionBeanLocal.retrieveCustomerById(customerId).getReviews();
    }

    @Override
    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException {
        if (reviewId == null) {
            throw new ReviewNotFoundException("ReviewNotFoundException: review id is null!");
        }

        ReviewEntity review = em.find(ReviewEntity.class, reviewId);

        if (review == null) {
            throw new ReviewNotFoundException("ReviewNotFoundException: Review with id " + reviewId + " does not exist!");
        }

        return review;
    }

    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException, DeleteReviewException {
        if (reviewId == null) {
            throw new ReviewNotFoundException("ReviewNotFoundException: review id is null!");
        }

        ReviewEntity reviewToDelete = this.retrieveReviewByReviewId(reviewId);

        try {
            //remove customer from review
            reviewToDelete.getCustomer().getReviews().remove(reviewToDelete);
            //remove transaction from review
            reviewToDelete.getTransaction().getReviews().remove(reviewToDelete);

            em.remove(reviewToDelete);
        } catch (PersistenceException ex) {
            em.getTransaction().rollback();
            throw new DeleteReviewException("DeleteReviewException: " + ex.getMessage());
        }
    }
    
    @Override
    public void updateAverageRatingForCustomer(Long customerId, Long reviewId) throws CustomerNotFoundException, ReviewNotFoundException {
        
        CustomerEntity currentCustomer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        ReviewEntity review = retrieveReviewByReviewId(reviewId);
        

        int numReviews = retrieveAllReviewsOnCustomer(customerId).size();
        
        if(numReviews == 1) {
            currentCustomer.setAverageRating(review.getRatingNumber().doubleValue());
            System.out.println("Current Rating For one person --> " + currentCustomer.getAverageRating());
            
        } else {

            Double newRating = (currentCustomer.getAverageRating() * (numReviews - 1) + review.getRatingNumber()) / numReviews;
//            Double newRating = (currentCustomer.getAverageRating() + review.getRatingNumber()) / 2.0;
            currentCustomer.setAverageRating(newRating);
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(ReviewEntity reviewEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ReviewEntity>> errors = validator.validate(reviewEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

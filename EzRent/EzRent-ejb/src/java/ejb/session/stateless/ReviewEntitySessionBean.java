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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewReviewException;
import util.exception.CustomerNotFoundException;
import util.exception.ReviewNotFoundException;

/**
 *
 * @author Yuxin
 */
@Stateless
public class ReviewEntitySessionBean implements ReviewEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public ReviewEntity createNewReview (Long customerId, Long transactionId, ReviewEntity newReview) throws CreateNewReviewException {
        if(newReview != null) {
            CustomerEntity customer = em.find(CustomerEntity.class, customerId);
            TransactionEntity transaction = em.find(TransactionEntity.class, transactionId);
            if (customer.equals(null)) {
                throw new CreateNewReviewException("Failed to create new review; Customer not found!");
            } else if (transaction.equals(null)) {
                throw new CreateNewReviewException("Failed to create new review; Transaction not found!");
            } else {
                newReview.setCustomer(customer);
                customer.getReviews().add(newReview);
                newReview.setTransaction(transaction);
                transaction.getReviews().add(newReview);
                em.persist(newReview);
                em.flush();
            }
            return newReview;
        } else {
            throw new CreateNewReviewException("Review not provided!");
        }
    }
    
    @Override
    public List<ReviewEntity> retrieveAllReviewsByCustomerId(Long customerId) throws CustomerNotFoundException{
        Query query = em.createQuery("Select r FROM ReviewEntity r, IN (r.customer) c WHERE c.userId =:userId");
        query.setParameter("userId", customerId);
        
        return query.getResultList();
    }
    
    @Override
    public List<ReviewEntity> retrieveAllReviews() {
        Query query = em.createQuery("Select r FROM ReviewEntity r");
        
        return query.getResultList();
    }
    
    @Override
    public ReviewEntity retrieveReviewByReviewId (Long reviewId) throws ReviewNotFoundException {
        ReviewEntity review = em.find(ReviewEntity.class, reviewId);
        
        if(review != null) {
            review.getCustomer();
            review.getTransaction();
            return review;
        } else {
            throw new ReviewNotFoundException("Review ID:" + reviewId + " not found!");
        }
    }
    
    @Override
    public void updateReview(ReviewEntity review) {
        em.merge(review);
    }
    
    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException{
        ReviewEntity reviewToDelete = retrieveReviewByReviewId(reviewId);
        reviewToDelete.getCustomer().getReviews().remove(reviewToDelete);
        reviewToDelete.getTransaction().getReviews().remove(reviewToDelete);
        em.remove(reviewToDelete);
        
    }
}

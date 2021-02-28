/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewReviewException;
import util.exception.CustomerNotFoundException;
import util.exception.ReviewNotFoundException;

/**
 *
 * @author Yuxin
 */
@Local
public interface ReviewEntitySessionBeanLocal {

    public ReviewEntity createNewReview(Long customerId, Long transactionId, ReviewEntity newReview) throws CreateNewReviewException;

    public List<ReviewEntity> retrieveAllReviewsByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<ReviewEntity> retrieveAllReviews();

    public void updateReview(ReviewEntity review);

    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;

    public void deleteReview(Long reviewId) throws ReviewNotFoundException;
    
}

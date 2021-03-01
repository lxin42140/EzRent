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
import util.exception.DeleteReviewException;
import util.exception.ReviewNotFoundException;
import util.exception.TransactionNotFoundException;

/**
 *
 * @author Yuxin
 */
@Local
public interface ReviewEntitySessionBeanLocal {

    public Long createNewReview(Long customerId, Long transactionId, ReviewEntity newReview) throws CreateNewReviewException, TransactionNotFoundException, CustomerNotFoundException;

    public List<ReviewEntity> retrieveAllReviewsCreatedByCustomer(Long customerId) throws CustomerNotFoundException;

    public List<ReviewEntity> retrieveAllReviewsOnCustomer(Long customerId) throws CustomerNotFoundException;

    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;

    public void deleteReview(Long reviewId) throws ReviewNotFoundException, DeleteReviewException;



    
}

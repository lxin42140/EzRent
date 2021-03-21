/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import javax.ejb.Local;
import util.exception.CommentNotFoundException;
import util.exception.CreateNewCommentException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCommentException;
import util.exception.ListingNotFoundException;

/**
 *
 * @author kiyon
 */
@Local
public interface CommentEntitySessionBeanLocal {

    public CommentEntity retrieveCommentByCommentId(Long commentId) throws CommentNotFoundException;

    public CommentEntity createNewComment(Long listingId, Long customerId, Long parentCommentId, CommentEntity newComment) throws ListingNotFoundException, CustomerNotFoundException, CreateNewCommentException, CommentNotFoundException;

    public CommentEntity createNewCommentWithNoParentComment(Long listingId, Long customerId, CommentEntity newComment) throws ListingNotFoundException, CustomerNotFoundException, CreateNewCommentException, CommentNotFoundException;

    public void deleteCommentById(Long commentId, Long customerId) throws CommentNotFoundException, DeleteCommentException, CustomerNotFoundException;

    public void deleteCommentForListing(Long commentId) throws CommentNotFoundException, DeleteCommentException;

}

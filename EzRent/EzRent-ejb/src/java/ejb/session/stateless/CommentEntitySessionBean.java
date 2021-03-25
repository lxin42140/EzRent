/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CommentNotFoundException;
import util.exception.CreateNewCommentException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCommentException;
import util.exception.ListingNotFoundException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class CommentEntitySessionBean implements CommentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @Override
    public CommentEntity createNewComment(Long listingId, Long customerId, Long parentCommentId, CommentEntity newComment) throws ListingNotFoundException, CustomerNotFoundException, CreateNewCommentException, CommentNotFoundException {
        if (newComment == null) {
            throw new CreateNewCommentException("CreateNewCommentException: Please provide a valid new comment!");
        }

        if (newComment.getMessage().isEmpty()) {
            throw new CreateNewCommentException("CreateNewCommentException: Please provide a message!");
        }

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        CommentEntity parentCommentEntity = this.retrieveCommentByCommentId(parentCommentId);

        //set bidrectional entity with parent comment
        parentCommentEntity.getReplies().add(newComment);
        newComment.setParentComment(parentCommentEntity);

        //comments that act as replies should not be associated with listing, but parent comment only
        return createCommentHelper(customer, null, newComment);
    }

    @Override
    public CommentEntity createNewCommentWithNoParentComment(Long listingId, Long customerId, CommentEntity newComment) throws ListingNotFoundException, CustomerNotFoundException, CreateNewCommentException, CommentNotFoundException {
        if (newComment == null) {
            throw new CreateNewCommentException("CreateNewCommentException: Please provide a valid new comment!");
        }

        if (newComment.getMessage().isEmpty()) {
            throw new CreateNewCommentException("CreateNewCommentException: Please provide a message!");
        }

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);

        return createCommentHelper(customer, listing, newComment);
    }

    // helper method to persist comment into db
    private CommentEntity createCommentHelper(CustomerEntity customer, ListingEntity listing, CommentEntity newComment) throws CreateNewCommentException {
        //uni-directional with customer
        //set sender
        newComment.setSender(customer);

        //for parent comment
        if (listing != null) {
            //set bidirectional with listing
            listing.getComments().add(newComment);
            newComment.setListing(listing);
        }

        try {
            validate(newComment);
            em.persist(newComment);
            em.flush();
            return newComment;
        } catch (ValidationFailedException ex) {
            throw new CreateNewCommentException("CreateNewCommentException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCommentException("CreateNewCommentException: Comment with same comment ID already exists!");
            } else {
                throw new CreateNewCommentException("CreateNewCommentException: " + ex.getMessage());
            }
        }
    }

    @Override
    public CommentEntity retrieveCommentByCommentId(Long commentId) throws CommentNotFoundException {
        if (commentId == null) {
            throw new CommentNotFoundException("CommentNotFoundException: Comment id is null!");
        }

        CommentEntity comment = em.find(CommentEntity.class, commentId);

        if (comment == null) {
            throw new CommentNotFoundException("COmmentNotFoundException: Comment id " + commentId + " does not exist!");
        }

        return comment;
    }

    @Override
    public void deleteCommentById(Long commentId, Long customerId) throws CommentNotFoundException, DeleteCommentException, CustomerNotFoundException {
        if (commentId == null) {
            throw new DeleteCommentException("DeleteCommentException: Please enter a valid comment id!");
        }

        if (customerId == null) {
            throw new DeleteCommentException("DeleteCommentException: Please enter a valid customer id!");
        }

        CommentEntity commentToDelete = this.retrieveCommentByCommentId(commentId);
        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        if (!commentToDelete.getSender().equals(customer)) {
            throw new DeleteCommentException("DeleteCommentException: Cannot delete comment not sent by sender!");
        }

        //deleting a reply
        if (commentToDelete.getParentComment() != null) {
            CommentEntity parentComment = commentToDelete.getParentComment();
            parentComment.getReplies().remove(commentToDelete);
            em.flush();
            commentToDelete.setParentComment(null);
            em.remove(commentToDelete);
        } else if (!commentToDelete.getReplies().isEmpty()) { //comment with active reply, mark as deleted
            commentToDelete.setIsDeleted(true);
            em.merge(commentToDelete);
            em.flush();
        } else {
            //comment has no replies
            //remove from db
            commentToDelete.getListing().getComments().remove(commentToDelete); // delete comment from listing
            commentToDelete.setListing(null); // delete listing from comment
            em.remove(commentToDelete);
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CommentEntity commentEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentEntity>> errors = validator.validate(commentEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

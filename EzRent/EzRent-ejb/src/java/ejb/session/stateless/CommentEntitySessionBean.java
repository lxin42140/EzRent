/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CommentEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.util.List;
import java.util.Set;
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
import util.exception.ListingNotFoundException;

/**
 *
 * @author kiyon
 */
@Stateless
public class CommentEntitySessionBean implements CommentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @Override
    public Long createNewComment(Long listingId, Long customerId, Long parentId, CommentEntity comment) throws ListingNotFoundException, CustomerNotFoundException, CreateNewCommentException, CommentNotFoundException {

        CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);

        if (parentId != null) {
            CommentEntity parentComment = retrieveCommentByCommentId(parentId);
            comment.setParentComment(parentComment);
            parentComment.getReplies().add(comment);
        }

        comment.setSender(customer);

        listing.getComments().add(comment);
        comment.setListing(listing);

        try {
            validate(comment);
            em.persist(comment);
            em.flush();
            return comment.getCommentId();
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
        if (comment == null || comment.getIsDeleted()) {
            throw new CommentNotFoundException("COmmentNotFoundException: Comment id " + commentId + " does not exist!");
        }
        
        return comment;
    }

    @Override
    public void deleteComment(Long commentId) throws CommentNotFoundException {
        CommentEntity deleteComment = retrieveCommentByCommentId(commentId);
        
        deleteComment.getListing().getComments().remove(deleteComment);
        deleteComment.setListing(null);
        //recursive call to delete all replies
        for (CommentEntity reply : deleteComment.getReplies()) {
            deleteComment(reply.getCommentId());
        }
        deleteComment.setDeleted();
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CommentEntity comment) throws CreateNewCommentException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CommentEntity>> errors = validator.validate(comment);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new CreateNewCommentException("CreateNewCommentException: Invalid inputs!\n" + errorMessage);
        }
    }

}

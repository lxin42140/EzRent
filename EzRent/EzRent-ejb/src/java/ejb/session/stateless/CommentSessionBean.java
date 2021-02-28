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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CommentNotFoundException;
import util.exception.CreateNewCommentException;
import util.exception.CreateNewListingException;
import util.exception.ListingNotFoundException;
import util.exception.UpdateListingFailException;

/**
 *
 * @author kiyon
 */
@Stateless
public class CommentSessionBean implements CommentSessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    /*
    uncomment when Customer Entity SB is done
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    */
    

    //throws CustomerNotFoundException also
    public Long createNewComment(Long listingId, Long customerId, Long parentId, CommentEntity comment) throws ListingNotFoundException, CreateNewCommentException, CommentNotFoundException {
        
        //CustomerEntity customer = customerEntitySessionBeanLocal.retrieveCustomerByCustId(customerId);
        //remove bottom code with above code when method has been created
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.userId =:inUserId");
        query.setParameter("inUserId", customerId);
        List<CustomerEntity> customerList = (List<CustomerEntity>) query.getResultList();
        CustomerEntity customer = customerList.get(0);
        
        ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
        
        if (parentId != null) {
            CommentEntity parentComment = retrieveCommentByCommentId(parentId);
            comment.setParentComment(parentComment);
            parentComment.getReplies().add(comment);
        }
        //unidirectional relationship between comment and customer
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
    
    public CommentEntity retrieveCommentByCommentId(Long commentId) throws CommentNotFoundException {
        Query query = em.createQuery("SELECT c FROM CommentEntity c WHERE c.commentId =:inCommentId");
        query.setParameter("inCommentId", commentId);
        try {
            CommentEntity comment = (CommentEntity) query.getSingleResult();
            if (comment.getIsDeleted()) {
                throw new CommentNotFoundException("COmmentNotFoundException: Comment id " + commentId + " does not exist!");
            }
            return comment;
        } catch (NoResultException ex) {
            throw new CommentNotFoundException("COmmentNotFoundException: Comment id " + commentId + " does not exist!");
        }
    }
    
    public void deleteComment(Long commentId) throws CommentNotFoundException {
        CommentEntity deleteComment = retrieveCommentByCommentId(commentId);
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

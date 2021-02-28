/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import entity.TagEntity;
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
import util.exception.CreateNewTagException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagFailException;

/**
 *
 * @author kiyon
 */
@Stateless
public class TagEntitySessionBean implements TagEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    public Long createNewTag(TagEntity tag) throws CreateNewTagException {
        try {
            validateNewTag(tag);
            em.persist(tag);
            em.flush();
            return tag.getTagId();
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewTagException("CreateNewTagException: Tag with same tag ID already exists!");
            } else {
                throw new CreateNewTagException("CreateNewTagException: " + ex.getMessage());
            }
        }
    }
    
    public List<TagEntity> retrieveAllTags() {
        Query query = em.createQuery("SELECT t FROM TagEntity t ORDER BY t.tagName");
        return query.getResultList();
    }
    
    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException {
        Query query = em.createQuery("SELECT t FROM TagEntity t WHERE t.tagId =:inTagId");
        query.setParameter("inTagId", tagId);
        
        try {
            return (TagEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new TagNotFoundException("TagNotFoundException: Category id " + tagId + " does not exist!");
        }
    }
    
    public Long updateTagName(Long tagId, String newName) throws TagNotFoundException, UpdateTagFailException {
        TagEntity tag = retrieveTagByTagId(tagId);
        tag.setTagName(newName);
        validateUpdatedTag(tag);
        em.merge(tag);
        em.flush();
        return tag.getTagId();
    }
    
    public void deleteTag(Long tagId) throws TagNotFoundException {
        TagEntity tag = retrieveTagByTagId(tagId);
        List<ListingEntity> listings = listingEntitySessionBeanLocal.retrieveAllListings();
        for (ListingEntity listing : listings) {
            List<TagEntity> tags = listing.getTags();
            if (tags.contains(tag)) {
                tags.remove(tag);
            }
        }
        em.remove(tag);
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
    
    private void validateNewTag(TagEntity tag) throws CreateNewTagException {
        String errorMessage = validate(tag);
        if (errorMessage.length() > 0) {
            throw new CreateNewTagException("CreateNewTagException: Invalid inputs!\n" + errorMessage);
        }
    }
    
    private void validateUpdatedTag(TagEntity tag) throws UpdateTagFailException {
        String errorMessage = validate(tag);
        if (errorMessage.length() > 0) {
            throw new UpdateTagFailException("UpdateTagFailException: Invalid inputs!\n" + errorMessage);
        }
    }


    private String validate(TagEntity tag) {       
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<TagEntity>> errors = validator.validate(tag);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }
        
        return errorMessage;
    } 
    
}

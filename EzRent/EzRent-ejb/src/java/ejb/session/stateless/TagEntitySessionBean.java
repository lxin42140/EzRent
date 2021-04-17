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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagFailException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class TagEntitySessionBean implements TagEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public TagEntity createNewTag(TagEntity tag) throws CreateNewTagException {
        if (tag == null) {
            throw new CreateNewTagException("CreateNewTagException: Please provide a valid new tag!");
        }
        try {
            validate(tag);
            em.persist(tag);
            em.flush();
            return tag;
        } catch (ValidationFailedException ex) {
            throw new CreateNewTagException("CreateNewTagException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewTagException("CreateNewTagException: Tag with same tag name already exists!");
            } else {
                throw new CreateNewTagException("CreateNewTagException: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<TagEntity> retrieveAllTags() {
        Query query = em.createQuery("SELECT t FROM TagEntity t ORDER BY t.tagName");
        return query.getResultList();
    }

    @Override
    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException {
        if (tagId == null) {
            throw new TagNotFoundException("TagNotFoundException: Invalid tag id!");
        }

        TagEntity tag = em.find(TagEntity.class, tagId);
        if (tag == null) {
            throw new TagNotFoundException("TagNotFoundException: Tag with id " + tagId + " does not exist!");
        }

        return tag;
    }

    @Override
    public TagEntity updateTagName(Long tagId, String newName) throws TagNotFoundException, UpdateTagFailException {
        if (tagId == null) {
            throw new UpdateTagFailException("UpdateTagFailException: Please provide a valid tag id!");
        }

        if (newName == null || newName.length() == 0) {
            throw new UpdateTagFailException("UpdateTagFailException: Please provide a valid new name!");
        }

        TagEntity tag = this.retrieveTagByTagId(tagId);
        tag.setTagName(newName);

        try {
            validate(tag);
            em.merge(tag);
            em.flush();
            return tag;
        } catch (ValidationFailedException ex) {
            throw new UpdateTagFailException("UpdateTagFailException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new UpdateTagFailException("UpdateTagFailException: Tag with same tag name already exists!");
            } else {
                throw new UpdateTagFailException("UpdateTagFailException: " + ex.getMessage());
            }
        }

    }

    @Override
    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException {
        if (tagId == null) {
            throw new DeleteTagException("DeleteTagException: Please provide a valid ID!");
        }

        TagEntity tag = this.retrieveTagByTagId(tagId);
        Query query = em.createQuery("SELECT l FROM ListingEntity l, IN (l.tags) t WHERE t.tagId =:intagId");
        query.setParameter("intagId", tagId);
        
        List<ListingEntity> associatedListings = query.getResultList();
        for (ListingEntity listing : associatedListings) {
            listing.getTags().remove(tag); // remove tag from listing
        }

        try {
            em.remove(tag);
        } catch (PersistenceException ex) {
            throw new DeleteTagException("DeleteTagException: " + ex.getMessage());
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(TagEntity tag) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<TagEntity>> errors = validator.validate(tag);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

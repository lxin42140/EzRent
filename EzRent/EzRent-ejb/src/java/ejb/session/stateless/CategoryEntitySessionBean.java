/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.UpdateCategoryFailException;
import util.exception.ValidationFailedException;

/**
 *
 * @author kiyon
 */
@Stateless
public class CategoryEntitySessionBean implements CategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    //Created by ADMIN (to be implemented)
    @Override
    // subcategory
    public Long createNewCategoryWithParentCategory(CategoryEntity category, Long parentCategoryId) throws CreateNewCategoryException, CategoryNotFoundException {

        if (parentCategoryId == null) {
            throw new CreateNewCategoryException("CreateNewCategoryException: Please provide a valid parent category id!");
        }

        CategoryEntity parentCategory = em.find(CategoryEntity.class, parentCategoryId);
        if (parentCategory == null) {
            throw new CategoryNotFoundException("CategoryNotFoundException: Parent category with id " + parentCategoryId + " does not exist!");
        }
        
        // parent category has listings attached already or has sub categories
        Query query = em.createQuery("SELECT l FROM ListingEntity l WHERE l.category.categoryId =:inCategoryId");
        query.setParameter("inCategoryId", parentCategoryId);
        if (!query.getResultList().isEmpty()) {
            throw new CreateNewCategoryException("CreateNewCategoryException: Invalid parent category!");
        }

        //set bidirectional relationship
        parentCategory.getSubCategories().add(category);
        category.setParentCategory(parentCategory);
        
        return this.createNewCategoryWithoutParentCategory(category);
    }

    @Override
    // parent category
    public Long createNewCategoryWithoutParentCategory(CategoryEntity category) throws CreateNewCategoryException {
        try {
            validate(category);
            em.persist(category);
            em.flush();
            return category.getCategoryId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewCategoryException("CreateNewCategoryException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCategoryException("CreateNewCategoryException: Category with same category name already exists!");
            } else {
                throw new CreateNewCategoryException("CreateNewCategoryException: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<CategoryEntity> retrieveAllLeafCategory() {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c WHERE size(c.subCategories) = 0 ORDER BY c.categoryName");
        return query.getResultList();
    }

    @Override
    public List<CategoryEntity> retrieveAllCategory() {
        Query query = em.createQuery("SELECT c FROM CategoryEntity c");
        return query.getResultList();
    }

    @Override
    public CategoryEntity retrieveCategoryById(Long categoryId) throws CategoryNotFoundException {
        if (categoryId == null) {
            throw new CategoryNotFoundException("CategoryNotFoundException: Category id is null!");
        }

        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        if (category == null) {
            throw new CategoryNotFoundException("CategoryNotFoundException: Category id " + categoryId + " does not exist!");
        }

        return category;
    }

    @Override
    public Long updateCategoryName(Long categoryId, String newCategoryName) throws CategoryNotFoundException, UpdateCategoryFailException {
        if (newCategoryName == null || newCategoryName.length() == 0) {
            throw new UpdateCategoryFailException("UpdateCategoryFailException: Please provide a valid new name!");
        }

        CategoryEntity category = this.retrieveCategoryById(categoryId);
        category.setCategoryName(newCategoryName);

        try {
            validate(category);
            em.merge(category);
            em.flush();
            return category.getCategoryId();
        } catch (ValidationFailedException ex) {
            throw new UpdateCategoryFailException("UpdateCategoryFailException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new UpdateCategoryFailException("UpdateCategoryFailException: Category with same category name already exists!");
            } else {
                throw new UpdateCategoryFailException("UpdateCategoryFailException: " + ex.getMessage());
            }
        }

    }

    //If category is a leaf category and has listings linked to it, then throw exception
    @Override
    public void deleteLeafCategory(Long categoryId) throws DeleteCategoryException, CategoryNotFoundException {
        if (categoryId == null) {
            throw new DeleteCategoryException("DeleteCategoryException: Please provide a valid category id!");
        }

        CategoryEntity category = this.retrieveCategoryById(categoryId);

        //check whether category is leaf category
        if (!category.getSubCategories().isEmpty()) {
            throw new DeleteCategoryException("DeleteCategoryException: Category to be deleted is not a leaf category!");
        }

        //Check whether any listing uses it
        Query query = em.createQuery("SELECT l FROM ListingEntity l WHERE l.category.categoryId =:inCategoryId");
        query.setParameter("inCategoryId", categoryId);
        if (!query.getResultList().isEmpty()) {
            throw new DeleteCategoryException("DeleteCategoryException: Category is in use!");
        }
        
        //leaf category with no listings attached, can delete
        if (category.getParentCategory() != null) {
            category.getParentCategory().getSubCategories().remove(category); //remove this leaf category from parent category
            category.setParentCategory(null); // set parent category to null
        }

        try {
            em.remove(category);
        } catch (PersistenceException ex) {
            throw new DeleteCategoryException("DeleteCategoryException: " + ex.getMessage());
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CategoryEntity category) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryEntity>> errors = validator.validate(category);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.ListingEntity;
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

/**
 *
 * @author kiyon
 */
@Stateless
public class CategoryEntitySessionBean implements CategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    //Created by ADMIN (to be implemented)
    @Override
    public Long createNewCategory(CategoryEntity category, Long parentCategoryId) throws CreateNewCategoryException, CategoryNotFoundException {
        
        CategoryEntity parentCategory; //parent category
        if (parentCategoryId != null) {
            parentCategory = retrieveCategoryById(parentCategoryId);
            //set bidirectional relationship
            parentCategory.getSubCategories().add(category);
            category.setParentCategory(parentCategory);
        }
        try {
            validateNewCategory(category);       
            em.persist(category);
            em.flush();
            return category.getCategoryId();
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCategoryException("CreateNewCategoryException: Category with same category ID already exists!");
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
        CategoryEntity category = retrieveCategoryById(categoryId);
        category.setCategoryName(newCategoryName);
        validateUpdatedCategory(category);
        em.merge(category);
        em.flush();
        return category.getCategoryId();
    }
    
    //If category is a leaf category and has listings linked to it, then throw exception
    @Override
    public void deleteCategory(Long categoryId) throws DeleteCategoryException, CategoryNotFoundException {
        CategoryEntity category = retrieveCategoryById(categoryId);
        
        if (!category.getSubCategories().isEmpty()) { //not leaf category
            if (category.getParentCategory() == null) { //is most parent category
                //set all direct sub categories' parent category to null
                for (CategoryEntity subCategory : category.getSubCategories()) {
                    subCategory.setParentCategory(null);
                }
            } else { //has both parent and sub category
                //for each sub category, link it to the parent category.
                //set bidirectional relationship
                CategoryEntity parentCategory = category.getParentCategory();
                for (CategoryEntity subCategory : category.getSubCategories()) {
                    subCategory.setParentCategory(parentCategory);
                    parentCategory.getSubCategories().add(subCategory);
                }
            }
        } else { //leaf category
            //check if any listings are under this category
            //if there is, throw exception
            List<ListingEntity> resultList = listingEntitySessionBeanLocal.retrieveAllListings();
            for (ListingEntity listing : resultList) {
                for (CategoryEntity listingCategory : listing.getCategories()) {
                    if (listingCategory.getCategoryId().equals(categoryId)) {
                        throw new DeleteCategoryException("DeleteCategoryException: Category contains listing(s)!");
                    }
                }
            }
        }
        em.remove(category);
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
    
    private void validateNewCategory(CategoryEntity category) throws CreateNewCategoryException {
        String errorMessage = validate(category);
        if (errorMessage.length() > 0) {
            throw new CreateNewCategoryException("CreateNewCategoryException: Invalid inputs!\n" + errorMessage);
        }
    }
    
    private void validateUpdatedCategory(CategoryEntity category) throws UpdateCategoryFailException {
        String errorMessage = validate(category);
        if (errorMessage.length() > 0) {
            throw new UpdateCategoryFailException("UpdateCategoryFailException: Invalid inputs!\n" + errorMessage);
        }
    }


    private String validate(CategoryEntity category) {       
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CategoryEntity>> errors = validator.validate(category);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }
        
        return errorMessage;
    } 
}

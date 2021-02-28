/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.UpdateCategoryFailException;

/**
 *
 * @author kiyon
 */
@Local
public interface CategoryEntitySessionBeanLocal {

    public Long createNewCategory(CategoryEntity category, Long parentCategoryId) throws CreateNewCategoryException, CategoryNotFoundException;

    public List<CategoryEntity> retrieveAllLeafCategory();

    public CategoryEntity retrieveCategoryById(Long categoryId) throws CategoryNotFoundException;

    public Long updateCategoryName(Long categoryId, String newCategoryName) throws CategoryNotFoundException, UpdateCategoryFailException;

    public void deleteCategory(Long categoryId) throws DeleteCategoryException, CategoryNotFoundException;
}

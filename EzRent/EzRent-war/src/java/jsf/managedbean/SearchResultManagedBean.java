/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CategoryNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Li Xin
 */
@Named(value = "searchResultManagedBean")
@ViewScoped
public class SearchResultManagedBean implements Serializable {

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*Filtered results*/
    private List<ListingEntity> filteredListings;
    private CustomerEntity filteredCustomer;

    /*No results*/
    private boolean noResult;
    private String noResultString;

    public SearchResultManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedUsernameToFilter");
        Long categoryId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedCategoryToFilter");
        List<Long> tagIds = (List<Long>) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedTagsToFilter");

        try {
            if (username != null) {
                this.filteredCustomer = customerEntitySessionBeanLocal.retrieveCustomerByUsername(username);
                return;
            }
            if (categoryId != null) {
                this.filteredListings = categoryEntitySessionBeanLocal.retrieveCategoryById(categoryId).getListings();
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching category!";
                }
                return;
            }
            if (tagIds != null) {
                String filterCondition = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tagFilterCondition");
                this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByTags(tagIds, filterCondition);
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching tags!";
                }
            }
        } catch (CustomerNotFoundException ex) {
            noResult = true;
            noResultString = "No customer with matching username in the database!";
        } catch (CategoryNotFoundException ex) {
            noResult = true;
            noResultString = "No listings with matching category!";
        }
    }

    public List<ListingEntity> getFilteredListings() {
        return filteredListings;
    }

    public void setFilteredListings(List<ListingEntity> filteredListings) {
        this.filteredListings = filteredListings;
    }

    public CustomerEntity getFilteredCustomer() {
        return filteredCustomer;
    }

    public void setFilteredCustomer(CustomerEntity filteredCustomer) {
        this.filteredCustomer = filteredCustomer;
    }

}

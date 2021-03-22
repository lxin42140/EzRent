/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.RequestEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.RequestEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Li Xin
 */
@Named(value = "searchResultManagedBean")
@ViewScoped
public class SearchResultManagedBean implements Serializable {

    @EJB
    private RequestEntitySessionBeanLocal requestEntitySessionBeanLocal;
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*Filtered results*/
    private List<ListingEntity> filteredListings;
    private List<RequestEntity> filteredRequests;
    private CustomerEntity filteredCustomer;

    /*No results*/
    private boolean noResult;
    private String noResultString;

    public SearchResultManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filterUsername");
        try {
            if (username != null) {
                this.filteredCustomer = customerEntitySessionBeanLocal.retrieveCustomerByUsername(username.toLowerCase().trim());
                return;
            }
        } catch (CustomerNotFoundException ex) {
            noResult = true;
            noResultString = "No customer with matching username in the database!";
        }

        String categoryName = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filterCategory");
        if (categoryName != null) {
            this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByCategoryName(categoryName.trim());
            if (filteredListings.isEmpty()) {
                noResult = true;
                noResultString = "No listings with matching category!";
            }
            return;
        }

        String listingName = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filterListing");
        if (listingName != null) {
            this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByListingName(listingName.trim());
            if (filteredListings.isEmpty()) {
                noResult = true;
                noResultString = "No listings with matching name!";
            }
            return;
        }

        String requestName = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filterRequest");
        if (requestName != null) {
            this.filteredRequests = requestEntitySessionBeanLocal.retrieveRequestsByRequestName(requestName.trim());
            if (filteredListings.isEmpty()) {
                noResult = true;
                noResultString = "No requests with matching name!";
            }
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

    public List<RequestEntity> getFilteredRequests() {
        return filteredRequests;
    }

    public void setFilteredRequests(List<RequestEntity> filteredRequests) {
        this.filteredRequests = filteredRequests;
    }

    public String getNoResultString() {
        return noResultString;
    }

    public void setNoResultString(String noResultString) {
        this.noResultString = noResultString;
    }

    public boolean isNoResult() {
        return noResult;
    }

    public void setNoResult(boolean noResult) {
        this.noResult = noResult;
    }

}

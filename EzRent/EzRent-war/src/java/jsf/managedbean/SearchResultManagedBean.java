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
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import util.exception.CustomerNotFoundException;
import util.exception.LikeListingException;
import util.exception.ListingNotFoundException;

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

    /*For filtered user*/
    private CustomerEntity filteredCustomer;
    private List<ListingEntity> listingEntities;
    private int rating;
    private String date;
    private Boolean viewListing;

    /*Filtered results*/
    private List<ListingEntity> filteredListings;
    private List<RequestEntity> filteredRequests;

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
                this.listingEntities = listingEntitySessionBeanLocal.retrieveAllListingByCustId(this.filteredCustomer.getUserId());
                viewListing = true;
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

    public void toggleLikeListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            CustomerEntity customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            ListingEntity listingToLikeDislike = (ListingEntity) event.getComponent().getAttributes().get("listingToLikeDislike");

            listingEntitySessionBeanLocal.toggleListingLikeDislike(customerEntity.getUserId(), listingToLikeDislike.getListingId());
            listingToLikeDislike = listingEntitySessionBeanLocal.retrieveListingByListingId(listingToLikeDislike.getListingId());

            if (this.listingEntities.size() > 0) {
                this.listingEntities.remove(listingToLikeDislike);
                this.listingEntities.add(listingToLikeDislike);
            }

            if (this.filteredListings.size() > 0) {
                this.filteredListings.remove(listingToLikeDislike);
                this.filteredListings.add(listingToLikeDislike);
            }

        } catch (IOException | ListingNotFoundException | CustomerNotFoundException | LikeListingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public void viewListingDetails(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedListingIdToView", (Long) event.getComponent().getAttributes().get("selectedListingIdToView"));
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/listingOperations/listingDetails.xhtml");
        } catch (IOException ex) {
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

    /**
     * *****************TO VIEW USER PROFILE IN SEARCH
     */
    public void clearMultiViewState() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        PrimeFaces.current().multiViewState().clearAll(viewId, true, this::showMessage);
    }

    private void showMessage(String clientId) {
        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
    }

    public CustomerEntity getCurrentCustomer() {
        return this.filteredCustomer;
    }

    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }

    public int getRating() {
        rating = this.filteredCustomer.getAverageRating().intValue();
        return rating;
    }

    public String getDate() {
        Date joinedDate = this.filteredCustomer.getDateJoined();
        date = new SimpleDateFormat("dd MMM yyyy").format(joinedDate);
        return date;
    }

    public Boolean getViewListing() {
        return viewListing;
    }

    public void setViewListing(Boolean viewListing) {
        this.viewListing = viewListing;
    }
}

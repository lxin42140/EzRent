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
import java.util.ArrayList;
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
import util.exception.DeleteRequestException;
import util.exception.FavouriteRequestException;
import util.exception.ToggleListingLikeUnlikeException;
import util.exception.ListingNotFoundException;
import util.exception.RequestNotFoundException;

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
    private List<RequestEntity> requestEntities;
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
        this.filteredListings = new ArrayList<>();
        this.filteredRequests = new ArrayList<>();
        this.listingEntities = new ArrayList<>();
    }

    @PostConstruct
    public void postConstruct() {

        String selectedOption = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedOption");
        if (selectedOption == null) {
            selectedOption = "";
        }

        switch (selectedOption) {
            case "username":
                try {
                String searchQuery = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredCustomer = customerEntitySessionBeanLocal.retrieveCustomerByUsername(searchQuery.toLowerCase().trim());
                this.listingEntities = listingEntitySessionBeanLocal.retrieveAllListingByCustId(this.filteredCustomer.getUserId());
                this.requestEntities = requestEntitySessionBeanLocal.retrieveRequestsByCustId(this.filteredCustomer.getUserId());
                viewListing = true;
            } catch (CustomerNotFoundException ex) {
                noResult = true;
                noResultString = "No customer with matching username in the database!";
            }
            break;
            case "category": {
                String searchQuery = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByCategoryName(searchQuery.trim());
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching category!";
                }
                break;
            }
            case "listing": {
                String searchQuery = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByListingName(searchQuery.trim());
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching name!";
                }
                break;
            }
            case "request": {
                String searchQuery = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredRequests = requestEntitySessionBeanLocal.retrieveRequestsByRequestName(searchQuery.trim());
                if (this.filteredRequests.isEmpty()) {
                    noResult = true;
                    noResultString = "No requests with matching name!";
                }
                break;
            }
            case "tags": {
                List<Long> searchQuery = (List<Long>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByTags(searchQuery);
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching tags!";
                }
                break;
            }
            case "tag": {
                Long searchQuery = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("searchQuery");
                this.filteredListings = listingEntitySessionBeanLocal.retrieveListingsByTag(searchQuery);
                if (filteredListings.isEmpty()) {
                    noResult = true;
                    noResultString = "No listings with matching tag!";
                }
                break;
            }
            default:
                noResult = true;
                noResultString = "Invalid category!";
                break;
        }

    }

    public void toggleLikeListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            CustomerEntity customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

            if (this.listingEntities.size() > 0) {
                ListingEntity listingToLikeDislike = (ListingEntity) event.getComponent().getAttributes().get("FilteredCustomerListing");
                listingEntitySessionBeanLocal.toggleListingLikeDislike(customerEntity.getUserId(), listingToLikeDislike.getListingId());
                listingToLikeDislike = listingEntitySessionBeanLocal.retrieveListingByListingId(listingToLikeDislike.getListingId());

                this.listingEntities.remove(listingToLikeDislike);
                this.listingEntities.add(listingToLikeDislike);
            }

            if (this.filteredListings.size() > 0) {
                ListingEntity listingToLikeDislike = (ListingEntity) event.getComponent().getAttributes().get("FilteredListing");
                listingEntitySessionBeanLocal.toggleListingLikeDislike(customerEntity.getUserId(), listingToLikeDislike.getListingId());
                listingToLikeDislike = listingEntitySessionBeanLocal.retrieveListingByListingId(listingToLikeDislike.getListingId());

                this.filteredListings.remove(listingToLikeDislike);
                this.filteredListings.add(listingToLikeDislike);
            }
        } catch (IOException | ListingNotFoundException | CustomerNotFoundException | ToggleListingLikeUnlikeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public void toggleLikeFilterRequest(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            RequestEntity requestToLikeDislike = (RequestEntity) event.getComponent().getAttributes().get("filteredRequest");
            CustomerEntity customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

            if (requestToLikeDislike.getCustomer().equals(customerEntity)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to like own request!", null));
                return;
            }

            requestEntitySessionBeanLocal.toggleRequestLikeDislike(customerEntity.getUserId(), requestToLikeDislike.getRequestId());

            //update list
            this.filteredRequests.remove(requestToLikeDislike);
            this.filteredRequests.add(requestEntitySessionBeanLocal.retrieveRequestByRequestId(requestToLikeDislike.getRequestId()));
        } catch (IOException | RequestNotFoundException | CustomerNotFoundException | FavouriteRequestException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong while trying to like the request! " + ex.getMessage(), null));
        }
    }

    public void toggleLikeFilterCustomerRequest(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            RequestEntity requestToLikeDislike = (RequestEntity) event.getComponent().getAttributes().get("customerRequest");
            CustomerEntity customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

            if (requestToLikeDislike.getCustomer().equals(customerEntity)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to like own request!", null));
                return;
            }

            requestEntitySessionBeanLocal.toggleRequestLikeDislike(customerEntity.getUserId(), requestToLikeDislike.getRequestId());

            //update list
            this.requestEntities.remove(requestToLikeDislike);
            this.requestEntities.add(requestEntitySessionBeanLocal.retrieveRequestByRequestId(requestToLikeDislike.getRequestId()));
        } catch (IOException | RequestNotFoundException | CustomerNotFoundException | FavouriteRequestException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong while trying to like the request! " + ex.getMessage(), null));
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

    public List<RequestEntity> getRequestEntities() {
        return requestEntities;
    }

    public void setRequestEntities(List<RequestEntity> requestEntities) {
        this.requestEntities = requestEntities;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.RequestEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.RequestEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;
import util.exception.FavouriteRequestException;
import util.exception.ToggleListingLikeUnlikeException;
import util.exception.ListingNotFoundException;
import util.exception.RequestNotFoundException;

/**
 *
 * @author Li Xin
 */
@Named(value = "favouritesManagedBean")
@ViewScoped
public class FavouritesManagedBean implements Serializable {

    @EJB
    private RequestEntitySessionBeanLocal requestEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*current customer*/
    private CustomerEntity customerEntity;

    /*favs*/
    private List<ListingEntity> favouriteListings;
    private List<RequestEntity> favouriteRequests;

    //toggle
    private Boolean viewListings;

    public FavouritesManagedBean() {
        this.favouriteListings = new ArrayList<>();
        this.favouriteRequests = new ArrayList<>();
        this.viewListings = true;
    }

    @PostConstruct
    public void postConstruct() {
        this.customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        try {
            this.favouriteRequests = requestEntitySessionBeanLocal.retrieveFavouriteRequestsForCustomer(customerEntity.getUserId());
            this.favouriteListings = listingEntitySessionBeanLocal.retrieveFavouriteListingsForCustomer(customerEntity.getUserId());
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong while trying to retrieve favourites!", null));
        }
    }

    public void redirectToShowListingDetails(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedListingIdToView", (Long) event.getComponent().getAttributes().get("selectedListingIdToView"));
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/listingOperations/listingDetails.xhtml");
        } catch (IOException ex) {
        }
    }

    public void toggleDislikeListing(ActionEvent event) {
        try {
            ListingEntity listingToDislike = (ListingEntity) event.getComponent().getAttributes().get("favouritesListing");
            listingEntitySessionBeanLocal.toggleListingLikeDislike(this.customerEntity.getUserId(), listingToDislike.getListingId());

            // remove unliked listings
            this.favouriteListings.remove(listingToDislike);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing unliked!", null));
        } catch (CustomerNotFoundException | ToggleListingLikeUnlikeException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public void toggleDislikeRequest(ActionEvent event) {
        try {

            RequestEntity requestToLikeDislike = (RequestEntity) event.getComponent().getAttributes().get("favouritesRequest");

            requestEntitySessionBeanLocal.toggleRequestLikeDislike(this.customerEntity.getUserId(), requestToLikeDislike.getRequestId());

            //remove request from list
            favouriteRequests.remove(requestToLikeDislike);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request unliked!", null));
        } catch (RequestNotFoundException | CustomerNotFoundException | FavouriteRequestException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong while trying to like the request! " + ex.getMessage(), null));
        }
    }

    public void toggleViewListings() {
        this.viewListings = !this.viewListings;
    }

    public List<ListingEntity> getFavouriteListings() {
        return favouriteListings;
    }

    public void setFavouriteListings(List<ListingEntity> favouriteListings) {
        this.favouriteListings = favouriteListings;
    }

    public List<RequestEntity> getFavouriteRequests() {
        return favouriteRequests;
    }

    public void setFavouriteRequests(List<RequestEntity> favouriteRequests) {
        this.favouriteRequests = favouriteRequests;
    }

    public boolean isViewListings() {
        return viewListings;
    }

    public void setViewListings(boolean viewListings) {
        this.viewListings = viewListings;
    }

}

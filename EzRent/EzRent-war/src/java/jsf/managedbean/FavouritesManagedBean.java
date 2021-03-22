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
import util.exception.LikeListingException;
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

    /**
     * Creates a new instance of FavouritesManagedBean
     */
    public FavouritesManagedBean() {
        this.favouriteListings = new ArrayList<>();
        this.favouriteRequests = new ArrayList<>();
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

    public void toggleDislikeListing(ActionEvent event) {
        try {
            ListingEntity listingToDislike = (ListingEntity) event.getComponent().getAttributes().get("listingToDislike");
            listingEntitySessionBeanLocal.toggleListingLikeDislike(this.customerEntity.getUserId(), listingToDislike.getListingId());

            // remove unliked listings
            this.favouriteListings.remove(listingToDislike);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing unliked!", null));
        } catch (CustomerNotFoundException | LikeListingException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public void toggleDislikeRequest(ActionEvent event) {
        try {
            RequestEntity requestToDislike = (RequestEntity) event.getComponent().getAttributes().get("requestToDislike");
            requestEntitySessionBeanLocal.toggleRequestLikeDislike(this.customerEntity.getUserId(), requestToDislike.getRequestId());
            // remove unliked listings
            this.favouriteRequests.remove(requestToDislike);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request unliked!", null));
        } catch (CustomerNotFoundException | FavouriteRequestException | RequestNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
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

}

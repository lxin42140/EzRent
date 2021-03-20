/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateListingFailException;

/**
 *
 * @author Li Xin
 */
@Named(value = "listingsManagedBean")
@ViewScoped
public class ListingsManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @Inject
    private ListingManagedBean viewListingManagedBean;

    /*View all listings*/
    private List<ListingEntity> listingEnities;

    /*Create new Listing*/
    private ListingEntity newListingEntity;
    private Long selectedCategoryId;
    private List<Long> selectedTagIds;

    /*Update Listing*/
    private ListingEntity selectedListingToUpdate;
    private Long selectedCategoryIdToUpdate;
    private List<Long> selectedTagIdsToUpdate;

    public ListingsManagedBean() {
        this.newListingEntity = new ListingEntity();
    }

    @PostConstruct
    public void postConstruct() {
        this.listingEnities = listingEntitySessionBeanLocal.retrieveAllListings();
        this.newListingEntity = new ListingEntity();
    }

    /*
    1. Need to update the path for login page
    2. Need to add p:growl
     */
    public void createNewListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            //reset
            this.newListingEntity = new ListingEntity();
            this.selectedCategoryId = null;
            this.selectedTagIds.clear();

            // add new listing to all listings
            this.listingEnities.add(listingEntitySessionBeanLocal.createNewListing(customer.getUserId(), selectedCategoryId, selectedTagIds, newListingEntity));

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New listing successfully created!", null));
        } catch (CategoryNotFoundException | CreateNewListingException | CustomerNotFoundException | TagNotFoundException | IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new listing: " + ex.getMessage(), null));
        }
    }

    public void updateListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            this.selectedListingToUpdate = listingEntitySessionBeanLocal.updateListingDetails(selectedListingToUpdate, selectedCategoryIdToUpdate, selectedTagIdsToUpdate);

            // add the updated listing to list
            this.listingEnities.remove(this.selectedListingToUpdate);
            this.listingEnities.add(this.selectedListingToUpdate);
            //reset
            this.selectedListingToUpdate = null;
            this.selectedCategoryIdToUpdate = null;
            this.selectedTagIdsToUpdate.clear();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing successfully created!", null));
        } catch (IOException | ListingNotFoundException | UpdateListingFailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the listing: " + ex.getMessage(), null));
        }
    }

    public void deleteListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            ListingEntity listingEntityToDelete = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToDelete");
            listingEntitySessionBeanLocal.deleteListing(listingEntityToDelete.getListingId());

            //remove deleted listings from list
            this.listingEnities.remove(listingEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing successfully deleted!", null));
        } catch (IOException | DeleteListingException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the listing: " + ex.getMessage(), null));
        }
    }

    public void toggleLikeListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            ListingEntity listingToLikeDislike = (ListingEntity) event.getComponent().getAttributes().get("listingToLikeDislike");
            CustomerEntity customerToLikeDislike = (CustomerEntity) event.getComponent().getAttributes().get("customerToLikeDislike");

            listingEntitySessionBeanLocal.toggleListingLikeDislike(customerToLikeDislike.getUserId(), listingToLikeDislike.getListingId());

            // add the updated listing to list
            this.listingEnities.remove(listingToLikeDislike);
            this.listingEnities.add(listingToLikeDislike);
        } catch (IOException | ListingNotFoundException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }

    }

    public ListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ListingManagedBean viewListingManagedBean) {
        this.viewListingManagedBean = viewListingManagedBean;
    }

    public ListingEntity getNewListingEntity() {
        return newListingEntity;
    }

    public void setNewListingEntity(ListingEntity newListingEntity) {
        this.newListingEntity = newListingEntity;
    }

    public ListingEntity getSelectedListingToUpdate() {
        return selectedListingToUpdate;
    }

    public void setSelectedListingToUpdate(ListingEntity selectedListingToUpdate) {
        this.selectedListingToUpdate = selectedListingToUpdate;
    }

    public List<ListingEntity> getListingEnities() {
        return listingEnities;
    }

    public void setListingEnities(List<ListingEntity> listingEnities) {
        this.listingEnities = listingEnities;
    }

}

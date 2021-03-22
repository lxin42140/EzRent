/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.LikeListingException;
import util.exception.ListingNotFoundException;
import util.exception.TagNotFoundException;

/**
 *
 * @author Li Xin
 */
@Named(value = "listingsManagedBean")
@ViewScoped
public class ListingsManagedBean implements Serializable {

    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*View all listings*/
    private List<ListingEntity> listingEnities;

    /*Create new Listing*/
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    private final List<String> deliveryOptions = Arrays.asList("Mail", "Meet-up");
    private final List<String> modeOfPaymentOptions = Arrays.asList("Cash on delivery", "Credit card");

    private ListingEntity newListingEntity;
    private String selectedDeliveryOption;
    private String selectedPaymentOption;
    private Long selectedCategoryId;
    private List<Long> selectedTagIds;

    public ListingsManagedBean() {
        this.newListingEntity = new ListingEntity();
    }

    @PostConstruct
    public void postConstruct() {
        this.listingEnities = listingEntitySessionBeanLocal.retrieveAllListings();
        this.newListingEntity = new ListingEntity();
        this.tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
        this.categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategory();
        //redirected to home page after listing has been deleted
        if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("ListingDeleted") != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing successfully deleted!", null));
        }
    }

    public void viewListingDetails(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedListingIdToView", (Long) event.getComponent().getAttributes().get("selectedListingIdToView"));
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/listingOperations/listingDetails.xhtml");
        } catch (IOException ex) {
        }
    }

    /*
    1. Need to update the path for login page
     */
    public void createNewListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            //set delivery option
            if (this.selectedDeliveryOption.equals("Mail")) {
                this.newListingEntity.setDeliveryOption(DeliveryOptionEnum.DELIVERY);
            } else {
                this.newListingEntity.setDeliveryOption(DeliveryOptionEnum.MEETUP);
            }

            //set payment option
            if (this.selectedPaymentOption.equals("Cash on delivery")) {
                this.newListingEntity.setModeOfPayment(ModeOfPaymentEnum.CASH_ON_DELIVERY);
            } else {
                this.newListingEntity.setModeOfPayment(ModeOfPaymentEnum.CREDIT_CARD);
            }

            // set date
            this.newListingEntity.setDateOfPost(new Date());

            //retrieve logged in customer
            CustomerEntity customer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

            // add new listing to all listings
            this.listingEnities.add(listingEntitySessionBeanLocal.createNewListing(customer.getUserId(), selectedCategoryId, selectedTagIds, newListingEntity));

            //reset
            this.newListingEntity = new ListingEntity();
            this.selectedCategoryId = null;
            this.selectedDeliveryOption = null;
            this.selectedPaymentOption = null;
            this.selectedTagIds.clear();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New listing successfully created!", null));
        } catch (CategoryNotFoundException | CreateNewListingException | CustomerNotFoundException | TagNotFoundException | IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new listing: " + ex.getMessage(), null));
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

            // add the updated listing to list
            this.listingEnities.remove(listingToLikeDislike);
            this.listingEnities.add(listingToLikeDislike);
        } catch (IOException | ListingNotFoundException | CustomerNotFoundException | LikeListingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred: " + ex.getMessage(), null));
        }
    }

    public ListingEntity getNewListingEntity() {
        return newListingEntity;
    }

    public void setNewListingEntity(ListingEntity newListingEntity) {
        this.newListingEntity = newListingEntity;
    }

    public List<ListingEntity> getListingEnities() {
        return listingEnities;
    }

    public void setListingEnities(List<ListingEntity> listingEnities) {
        this.listingEnities = listingEnities;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public List<Long> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedTagIds(List<Long> selectedTagIds) {
        this.selectedTagIds = selectedTagIds;
    }

    public String getSelectedDeliveryOption() {
        return selectedDeliveryOption;
    }

    public void setSelectedDeliveryOption(String selectedDeliveryOption) {
        this.selectedDeliveryOption = selectedDeliveryOption;
    }

    public List<String> getDeliveryOptions() {
        return deliveryOptions;
    }

    public String getSelectedPaymentOption() {
        return selectedPaymentOption;
    }

    public void setSelectedPaymentOption(String selectedPaymentOption) {
        this.selectedPaymentOption = selectedPaymentOption;
    }

    public List<String> getModeOfPaymentOptions() {
        return modeOfPaymentOptions;
    }

}

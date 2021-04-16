/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.TagEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.AvailabilityEnum;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.exception.CreateNewOfferException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.UpdateListingFailException;

/**
 *
 * @author Li Xin
 */
@Named(value = "viewListingManagedBean")
@ViewScoped
public class ListingManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @Inject
    private CommentsManagedBean commentsManagedBean;
    @Inject
    private ViewLesseeTransactionsManagedBean viewLesseeTransactionsManagedBean;

    private CustomerEntity currentCustomer;
    private ListingEntity listingEntity;

    /*Update Listing*/
    private ListingEntity listingEntityToUpdate;
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    private final List<String> deliveryOptions = Arrays.asList("Mail", "Meet-up");
    private final List<String> modeOfPaymentOptions = Arrays.asList("Cash on delivery", "Credit card");

    private String selectedDeliveryOption;
    private String selectedPaymentOption;
    private Long selectedCategoryId;
    private List<Long> selectedTagIds;

    public ListingManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            this.listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId(Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("listingId")));
            this.listingEntityToUpdate = listingEntitySessionBeanLocal.retrieveListingByListingId(this.listingEntity.getListingId());
            this.tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
            this.categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategory();
            this.currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            //init commentsManagedBean 
            this.commentsManagedBean.setCommentsForListing(listingEntity.getComments());
            this.commentsManagedBean.setListingToComment(listingEntity);
        } catch (ListingNotFoundException ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
            } catch (IOException ex1) {
            }
        }
    }

    public void updateListing(ActionEvent event) {
        try {
            if (currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            //updated delivery option
            if (this.selectedDeliveryOption != null) {
                if (listingEntity.getDeliveryOption() == DeliveryOptionEnum.MAIL && !this.selectedDeliveryOption.equals("Mail")) {
                    this.listingEntity.setDeliveryOption(DeliveryOptionEnum.MEETUP);
                } else {
                    this.listingEntity.setDeliveryOption(DeliveryOptionEnum.MAIL);
                }
            }

            //updated mode of payment
            if (this.selectedPaymentOption != null) {
                if (listingEntity.getModeOfPayment() == ModeOfPaymentEnum.CASH_ON_DELIVERY && !this.selectedPaymentOption.equals("Cash on delivery")) {
                    this.listingEntity.setModeOfPayment(ModeOfPaymentEnum.CREDIT_CARD);
                } else {
                    this.listingEntity.setModeOfPayment(ModeOfPaymentEnum.CASH_ON_DELIVERY);
                }
            }

            listingEntitySessionBeanLocal.updateListingDetails(this.listingEntityToUpdate, selectedCategoryId, selectedTagIds);
            //retrieve and set updated listings
            this.listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId(this.listingEntityToUpdate.getListingId());
            this.listingEntityToUpdate = listingEntitySessionBeanLocal.retrieveListingByListingId(this.listingEntityToUpdate.getListingId());
            //reset
            this.selectedDeliveryOption = "";
            this.selectedPaymentOption = "";
            this.selectedCategoryId = null;
            this.selectedTagIds.clear();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing successfully updated!", null));
        } catch (IOException | ListingNotFoundException | UpdateListingFailException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the listing: " + ex.getMessage(), null));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();
            this.listingEntityToUpdate.setFilePathName(event.getFile().getFileName());

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void deleteListing() {
        try {
            if (currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            listingEntitySessionBeanLocal.deleteListing(this.listingEntity.getListingId());

            //redirect to home page after delete
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("ListingDeleted", true);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");

        } catch (IOException | DeleteListingException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the listing: " + ex.getMessage(), null));
        }
    }

    public Boolean disableMakeOffer() {
        //return true (disabled) if customer is the listing owner, or listing is unavailable
        if (listingEntity.getAvailability() == AvailabilityEnum.RENTED_OUT
                || ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")
                && currentCustomer.getUserId().equals(listingEntity.getListingOwner().getUserId()))) {
            return true;
        }
        return false;
    }

    /*
        Update path to redirect to offer page
     */
    public void makeOffer(ActionEvent event) {
        try {
            if (currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("listingToOffer", listingEntity);

            getViewLesseeTransactionsManagedBean().makeOffer();

            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while making an offer: " + ex.getMessage(), null));
        } catch (CreateNewOfferException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void redirectToSearchByCategory() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedOption", "category");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchQuery", this.listingEntity.getCategory().getCategoryName());
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
    }

    public void redirectToSearchByUser() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedOption", "username");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchQuery", this.listingEntity.getListingOwner().getUserName());
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
    }

    public void redirectToSearchByAllTags() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedOption", "tags");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchQuery", this.listingEntity.getTags().stream().map(x -> x.getTagId()).collect(Collectors.toList()));
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
    }

    public void redirectToSearchByTag(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filterTag", (Long) event.getComponent().getAttributes().get("tagToFilter"));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedOption", "tag");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("searchQuery", (Long) event.getComponent().getAttributes().get("tagToFilter"));
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public ListingEntity getListingEntity() {
        return listingEntity;
    }

    public void setListingEntity(ListingEntity listingEntity) {
        this.listingEntity = listingEntity;
    }

    public CommentsManagedBean getCommentsManagedBean() {
        return commentsManagedBean;
    }

    public void setCommentsManagedBean(CommentsManagedBean commentsManagedBean) {
        this.commentsManagedBean = commentsManagedBean;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public List<String> getDeliveryOptions() {
        return deliveryOptions;
    }

    public List<String> getModeOfPaymentOptions() {
        return modeOfPaymentOptions;
    }

    public String getSelectedDeliveryOption() {
        return selectedDeliveryOption;
    }

    public String getSelectedPaymentOption() {
        return selectedPaymentOption;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public List<Long> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedDeliveryOption(String selectedDeliveryOption) {
        this.selectedDeliveryOption = selectedDeliveryOption;
    }

    public void setSelectedPaymentOption(String selectedPaymentOption) {
        this.selectedPaymentOption = selectedPaymentOption;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public void setSelectedTagIds(List<Long> selectedTagIds) {
        this.selectedTagIds = selectedTagIds;
    }

    public ListingEntity getListingEntityToUpdate() {
        return listingEntityToUpdate;
    }

    public void setListingEntityToUpdate(ListingEntity listingEntityToUpdate) {
        this.listingEntityToUpdate = listingEntityToUpdate;
    }

    /**
     * @return the viewLesseeTransactionsManagedBean
     */
    public ViewLesseeTransactionsManagedBean getViewLesseeTransactionsManagedBean() {
        return viewLesseeTransactionsManagedBean;
    }

    /**
     * @param viewLesseeTransactionsManagedBean the
     * viewLesseeTransactionsManagedBean to set
     */
    public void setViewLesseeTransactionsManagedBean(ViewLesseeTransactionsManagedBean viewLesseeTransactionsManagedBean) {
        this.viewLesseeTransactionsManagedBean = viewLesseeTransactionsManagedBean;
    }

}

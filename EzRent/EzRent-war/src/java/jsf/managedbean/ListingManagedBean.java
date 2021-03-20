/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
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

    @Inject
    private CommentsManagedBean commentsManagedBean;

    private ListingEntity listingEntity;

    /*Update Listing*/
    private ListingEntity selectedListingToUpdate;
    private Long selectedCategoryIdToUpdate;
    private List<Long> selectedTagIdsToUpdate;

    /*
    2. Inject OfferManagedBean
     */
    public ListingManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            this.listingEntity = listingEntitySessionBeanLocal.retrieveListingByListingId((Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedListingIdToView"));
            this.commentsManagedBean.setCommentsForListing(listingEntity.getComments());
        } catch (ListingNotFoundException ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
            } catch (IOException ex1) {
            }
        }
    }

    public void updateListing(ActionEvent event) {
        try {
            if (!(Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/login.xhtml");
            }

            this.listingEntity = listingEntitySessionBeanLocal.updateListingDetails(selectedListingToUpdate, selectedCategoryIdToUpdate, selectedTagIdsToUpdate);

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

            //redirect to home page after delete
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("ListingDeleted", true);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");

        } catch (IOException | DeleteListingException | ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the listing: " + ex.getMessage(), null));
        }
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

}

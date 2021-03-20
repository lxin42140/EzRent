/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ListingEntity;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Li Xin
 */
@Named(value = "viewListingManagedBean")
@ViewScoped
public class ListingManagedBean implements Serializable {
    
    @Inject
    private CommentsManagedBean commentsManagedBean;
    
    private ListingEntity listingEntity;

    /*
    2. Inject OfferManagedBean
     */
    public ListingManagedBean() {
    }
    
    public ListingEntity getListingEntity() {
        // set comments for a listing
        commentsManagedBean.setCommentsForListing(listingEntity.getComments());
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

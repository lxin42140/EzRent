/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.ListingEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Li Xin
 */
@Named(value = "listingsManagedBean")
@ViewScoped
public class ListingsManagedBean implements Serializable {

    @EJB(name = "ListingEntitySessionBeanLocal")
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @Inject
    private ViewListingManagedBean viewListingManagedBean;

    private ListingEntity newListingEntity;
    private ListingEntity selectedListingToUpdate;
    private List<ListingEntity> listingEnities;

    public ListingsManagedBean() {
        this.newListingEntity = new ListingEntity();
    }

    @PostConstruct
    public void postConstruct() {
        this.listingEnities = listingEntitySessionBeanLocal.retrieveAllListings();
    }

    public ViewListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ViewListingManagedBean viewListingManagedBean) {
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

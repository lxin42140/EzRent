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

/**
 *
 * @author Li Xin
 */
@Named(value = "viewListingManagedBean")
@ViewScoped
public class ViewListingManagedBean implements Serializable {

    private ListingEntity listingEntity;

    public ViewListingManagedBean() {
    }

    public ListingEntity getListingEntity() {
        return listingEntity;
    }

    public void setListingEntity(ListingEntity listingEntity) {
        this.listingEntity = listingEntity;
    }

}

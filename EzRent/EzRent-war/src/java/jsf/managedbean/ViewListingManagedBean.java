/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ListingEntity;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Li Xin
 */
@Named(value = "viewListingManagedBean")
@Dependent
public class ViewListingManagedBean implements Serializable {

    private ListingEntity listingEntity;

    public ViewListingManagedBean() {
        this.listingEntity = new ListingEntity();
    }

    public ListingEntity getListingEntity() {
        return listingEntity;
    }

    public void setListingEntity(ListingEntity listingEntity) {
        this.listingEntity = listingEntity;
    }

}

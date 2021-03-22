/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ListingEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Yuxin
 */
@Named(value = "profileListingManagedBean")
@ViewScoped
public class ProfileListingManagedBean implements Serializable{

    @EJB(name = "ListingEntitySessionBeanLocal")
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    private List<ListingEntity> listingEntities;
    
    private CustomerEntity currentCustomer;
    
    private int rating;
    
    private String date;
    
    private Boolean viewListing;
    
    public ProfileListingManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        System.out.println("POSTCONSTRUCT METHOD INVOKED --- PROFILE LISTING");
        currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        System.out.println("Current customer: " + currentCustomer.getUserId());
        listingEntities = listingEntitySessionBeanLocal.retrieveAllListingByCustId(currentCustomer.getUserId());
        viewListing = true;
    }
    
    public void clearMultiViewState() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        PrimeFaces.current().multiViewState().clearAll(viewId, true, this::showMessage);
    }

    private void showMessage(String clientId) {
        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }

    public int getRating() {
        rating = currentCustomer.getAverageRating().intValue();
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDate() {
        Date joinedDate = currentCustomer.getDateJoined();
        date = new SimpleDateFormat("dd MMM yyyy").format(joinedDate);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getViewListing() {
        return viewListing;
    }

    public void setViewListing(Boolean viewListing) {
        this.viewListing = viewListing;
    }
    
}

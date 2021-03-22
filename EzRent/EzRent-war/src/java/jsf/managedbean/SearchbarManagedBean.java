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
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Li Xin
 */
@Named(value = "searchbarManagedBean")
@ViewScoped
public class SearchbarManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*place holder for search bar*/
    private ListingEntity mostPopularListing;

    /*Filer drop down options*/
    private final List<String> filterOptions = Arrays.asList("listing", "username", "request", "category");

    /*Filter options */
    private String searchQuery;
    private String selectedOption;

    public SearchbarManagedBean() {
        this.searchQuery = "";
        this.selectedOption = "";
    }

    @PostConstruct
    public void postConstruct() {
        this.mostPopularListing = listingEntitySessionBeanLocal.retrieveMostPopularListing();
    }

    public void search() {
        boolean valid = false;
        System.out.println("****Option " + this.selectedOption);
        System.out.println("****Query " + this.searchQuery);
        if (this.selectedOption.equals("username") && !this.searchQuery.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filterUsername", this.selectedOption.toLowerCase().trim());
            valid = true;
        } else if (this.selectedOption.equals("category") && !this.searchQuery.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filterCategory", this.selectedOption);
            valid = true;
        } else if (this.selectedOption.equals("listing") && !this.searchQuery.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filterListing", this.selectedOption);
            valid = true;
        } else if (this.selectedOption.equals("request") && !this.searchQuery.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filterRequest", this.selectedOption);
            valid = true;
        }

        if (!valid) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter query!", null));
            return;
        } else {
            //reset query
            this.searchQuery = "";
            this.selectedOption = "";
        }

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
        } catch (IOException ex) {
        }
    }

    public List<String> getFilterOptions() {
        return filterOptions;
    }

    public ListingEntity getMostPopularListing() {
        return mostPopularListing;
    }

    public void setMostPopularListing(ListingEntity mostPopularListing) {
        this.mostPopularListing = mostPopularListing;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

}

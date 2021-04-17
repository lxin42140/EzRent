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
    private final List<String> filterOptions = Arrays.asList("listing", "request", "category", "username");

    /*Filter options */
    private String searchQuery;
    private String selectedOption;

    public SearchbarManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        this.mostPopularListing = listingEntitySessionBeanLocal.retrieveMostPopularListing();
        selectedOption = "";
        searchQuery = "";
    }

    public void search() {
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("searchQuery", searchQuery);
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedOption", selectedOption);
    }

}

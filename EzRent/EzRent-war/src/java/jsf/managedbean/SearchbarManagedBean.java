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
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Li Xin
 */
@Named(value = "searchbarManagedBean")
@RequestScoped
public class SearchbarManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    /*place holder for search bar*/
    private ListingEntity mostPopularListing;

    /*Filter options */
    private String selectedUsernameToFilter;
    private Long selectedCategoryToFilter;
    private List<Long> selectedTagsToFilter;
    private String tagFilterCondition;

    public SearchbarManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        this.mostPopularListing = listingEntitySessionBeanLocal.retrieveMostPopularListing();
    }

    public void search(ActionEvent actionEvent) {
        boolean valid = false;
        if (this.selectedUsernameToFilter != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedUsernameToFilter", this.selectedUsernameToFilter.toLowerCase().trim());
            //reset
            this.selectedUsernameToFilter = null;
            valid = true;
        } else if (this.selectedCategoryToFilter != null) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedCategoryToFilter", this.selectedCategoryToFilter);
            //reset
            this.selectedCategoryToFilter = null;
            valid = true;
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedTagsToFilter", this.selectedTagsToFilter);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("tagFilterCondition", this.tagFilterCondition);
            //reset
            this.selectedTagsToFilter.clear();
            valid = true;
        }
        if (!valid) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter query!", null));
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/search/searchResult.xhtml");
        } catch (IOException ex) {
        }

    }

    public ListingEntity getMostPopularListing() {
        return mostPopularListing;
    }

    public void setMostPopularListing(ListingEntity mostPopularListing) {
        this.mostPopularListing = mostPopularListing;
    }

    public String getTagFilterCondition() {
        return tagFilterCondition;
    }

    public void setTagFilterCondition(String tagFilterCondition) {
        this.tagFilterCondition = tagFilterCondition;
    }

    public String getSelectedUsernameToFilter() {
        return selectedUsernameToFilter;
    }

    public void setSelectedUsernameToFilter(String selectedUsernameToFilter) {
        this.selectedUsernameToFilter = selectedUsernameToFilter;
    }

    public Long getSelectedCategoryToFilter() {
        return selectedCategoryToFilter;
    }

    public void setSelectedCategoryToFilter(Long selectedCategoryToFilter) {
        this.selectedCategoryToFilter = selectedCategoryToFilter;
    }

    public List<Long> getSelectedTagsToFilter() {
        return selectedTagsToFilter;
    }

    public void setSelectedTagsToFilter(List<Long> selectedTagsToFilter) {
        this.selectedTagsToFilter = selectedTagsToFilter;
    }

}

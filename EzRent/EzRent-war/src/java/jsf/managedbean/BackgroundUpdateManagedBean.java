/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.ListingEntity;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Li Xin
 */
@Named(value = "backgroundUpdateManagedBean")
@ApplicationScoped
public class BackgroundUpdateManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBean;
    private ListingEntity latestListing;

    private ScheduledExecutorService scheduler;

    /**
     * Creates a new instance of BackgroundUpdateManagedBean
     */
    public BackgroundUpdateManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        this.latestListing = listingEntitySessionBean.retrieveLatestListing();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            ListingEntity newLatestListing = listingEntitySessionBean.retrieveLatestListing();
            if (newLatestListing.getDateOfPost().after(latestListing.getDateOfPost())) {
                this.latestListing = newLatestListing;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Refresh to view latest listings!", null));
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdownNow();
    }
}

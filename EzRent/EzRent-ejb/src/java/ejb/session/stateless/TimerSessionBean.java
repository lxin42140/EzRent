/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.exception.ListingNotFoundException;
import util.exception.TagNotFoundException;

/**
 *
 * @author Li Xin
 */
@Singleton
@Startup
public class TimerSessionBean {

    @EJB(name = "ListingEntitySessionBeanLocal")
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @PostConstruct
    public void postConstruct() {
    }

    @Schedule(dayOfWeek = "*", hour = "0")
    public void checkPopularProduct() {
        try {
            List<ListingEntity> allListings = listingEntitySessionBeanLocal.retrieveAllListings();
            Collections.sort(allListings, (x, y) -> y.getOffers().size() - x.getOffers().size());
            int limit = (int) Math.floor(allListings.size() * 0.3);
            for (int i = 0; i < limit; i++) {
                ListingEntity listing = allListings.get(i);
                listingEntitySessionBeanLocal.markListingAsPopular(listing.getListingId());
            }
            for (int i = limit; i < allListings.size(); i++) {
                ListingEntity listing = allListings.get(i);
                listingEntitySessionBeanLocal.unmarkListingAsPopular(listing.getListingId());
            }
        } catch (ListingNotFoundException | TagNotFoundException ex) {
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

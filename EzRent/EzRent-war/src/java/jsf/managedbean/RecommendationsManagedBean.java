/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import util.exception.RetrievePopularListingsException;

/**
 *
 * @author Li Xin
 */
@Named(value = "recommendationsManagedBean")
@RequestScoped

public class RecommendationsManagedBean implements Serializable {

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    private List<ListingEntity> recommendedListings;

    public RecommendationsManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        recommendedListings = listingEntitySessionBeanLocal.retrieveAllListings();
        if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLogin")) {
            CustomerEntity customerEntity = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            this.findRecommendedListingForCustomer(customerEntity);
        } else {
            this.findRecommendedListings();
        }
    }

    private void findRecommendedListings() {
        // determine listings with most number of offers
        PriorityQueue<ListingEntity> pq = new PriorityQueue<>(listingEntitySessionBeanLocal.retrieveAllListings());
        // add top 3 listings
        for (int i = 0; i < 3 && !pq.isEmpty(); i++) {
            recommendedListings.add(pq.poll());
        }
    }

    private void findRecommendedListingForCustomer(CustomerEntity customerEntity) {
        List<OfferEntity> offers = customerEntity.getOffers();
        TreeMap<CategoryEntity, Integer> treeMap = new TreeMap<>();
        offers.stream().map(offer -> offer.getListing().getCategory()).forEach(category -> {
            if (treeMap.containsKey(category)) {
                treeMap.put(category, treeMap.get(category) + 1);
            } else {
                treeMap.put(category, 1);
            }
        });
        try {
            CategoryEntity categoryEntity = treeMap.lastKey();
            this.recommendedListings = listingEntitySessionBeanLocal.retrieveMostPopularListingsForCategory(categoryEntity.getCategoryId(), customerEntity.getUserId());
        } catch (NoSuchElementException | RetrievePopularListingsException ex) {
            //use general recommendations instead
            this.findRecommendedListings();
        }
    }

    public List<ListingEntity> getRecommendedListings() {
        return recommendedListings;
    }

    public void setRecommendedListings(List<ListingEntity> recommendedListings) {
        this.recommendedListings = recommendedListings;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ReviewEntity;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.UserAccessRightEnum;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Yuxin
 */
@Named(value = "viewReviewManagedBean")
@ViewScoped
public class ViewReviewManagedBean implements Serializable{

    @EJB(name = "ReviewEntitySessionBeanLocal")
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;
    
    private CustomerEntity currentCustomer;
    
    private List<ReviewEntity> reviewEntities; 
    
    private Double avgRating;
    
    
    public ViewReviewManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        try{
            currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
            reviewEntities = reviewEntitySessionBeanLocal.retrieveAllReviewsOnCustomer(currentCustomer.getUserId());    
            
//            //for testing purpose - TO BE DELETED when there are enough data inits
//            ReviewEntity testReview = new ReviewEntity("Very good item! Recommended!!!", 5);
//            CustomerEntity testCustomer = new CustomerEntity("123 123", "123456", new Date(), "N/A", 0.0, "JamusLee123", "testing@mail.com", "Jamus", "Lee", UserAccessRightEnum.CUSTOMER, false, false, "password");
//            testReview.setCustomer(testCustomer);
//            
//            ReviewEntity testReview1 = new ReviewEntity("Item lousy la. How can you rent this out?", 2);
//            CustomerEntity testCustomer1 = new CustomerEntity("123 123", "123456", new Date(), "N/A", 0.0, "Loser_94", "testing@mail.com", "Jamuss", "Leeee", UserAccessRightEnum.CUSTOMER, false, false, "password");
//            testReview1.setCustomer(testCustomer1);
//            
//            ReviewEntity testReview2 = new ReviewEntity("A bit old, but still can use... Maybe rental fee should be a little cheaper considered how old it looks...", 3);
//            CustomerEntity testCustomer2 = new CustomerEntity("123 123", "123456", new Date(), "N/A", 0.0, "Jane_Tanzxc", "testing@mail.com", "Jamussss", "Leee", UserAccessRightEnum.CUSTOMER, false, false, "password");
//            testReview2.setCustomer(testCustomer2);
//            
//            reviewEntities.add(testReview1);
//            reviewEntities.add(testReview);
//            reviewEntities.add(testReview2);
//            
//            Double totalReviews = 0.0;
//            for(ReviewEntity review: reviewEntities) {
//                totalReviews += review.getRatingNumber();
//            }
//            avgRating = totalReviews / reviewEntities.size();
//            avgRating = Math.round(avgRating * 100.0) / 100.0;
        }catch(CustomerNotFoundException ex) {
        }
    }
    
    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerEntity currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
    
}

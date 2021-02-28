/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteListingException;
import util.exception.ListingNotFoundException;
import util.exception.UpdateListingFailException;

/**
 *
 * @author kiyon
 */
@Local
public interface ListingEntitySessionBeanLocal {

    public Long createListing(CustomerEntity customer, ListingEntity listing, List<CategoryEntity> categories, List<TagEntity> tags) throws CreateNewListingException, CustomerNotFoundException;
    
    public List<ListingEntity> retrieveAllListings();
    
    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException;
    
    public Long updateListingDetails(Long listingId, ListingEntity newListing) throws ListingNotFoundException, UpdateListingFailException;
    
    public void likeListing(Long customerId, Long listingId) throws ListingNotFoundException;
    
    public Long deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException;
}

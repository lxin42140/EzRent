/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewListingException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteListingException;
import util.exception.LikeListingException;
import util.exception.ListingNotFoundException;
import util.exception.RetrievePopularListingsException;
import util.exception.TagNotFoundException;
import util.exception.UpdateListingFailException;

/**
 *
 * @author kiyon
 */
@Local
public interface ListingEntitySessionBeanLocal {

    public ListingEntity createNewListing(Long customerId, Long categoryId, List<Long> tagsId, ListingEntity listing) throws CreateNewListingException, CustomerNotFoundException, CategoryNotFoundException, TagNotFoundException;

    public ListingEntity updateListingDetails(ListingEntity newListing, Long newCategoryId, List<Long> newTagIds) throws ListingNotFoundException, UpdateListingFailException;

    public List<ListingEntity> retrieveAllListings();

    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException;

    public List<ListingEntity> retrieveListingByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<ListingEntity> retrieveMostPopularListingsForCategory(Long categoryId, Long customerId) throws RetrievePopularListingsException;

    public List<ListingEntity> retrieveListingsByTags(List<Long> tagIds, String condition);

    public void deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException;

    public void toggleListingLikeDislike(Long customerId, Long listingId) throws LikeListingException, ListingNotFoundException, CustomerNotFoundException;

    public ListingEntity retrieveLatestListing();

    public ListingEntity retrieveMostPopularListing();

}

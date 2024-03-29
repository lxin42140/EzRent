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
import util.exception.ToggleListingLikeUnlikeException;
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

    public void deleteListing(Long listingId) throws ListingNotFoundException, DeleteListingException;

    public void toggleListingLikeDislike(Long customerId, Long listingId) throws ToggleListingLikeUnlikeException, ListingNotFoundException, CustomerNotFoundException;

    public List<ListingEntity> retrieveAllListings();

    public ListingEntity retrieveListingByListingId(Long listingId) throws ListingNotFoundException;

    public List<ListingEntity> retrieveAllListingByCustId(Long custId) throws CustomerNotFoundException;

    public List<ListingEntity> retrieveListingByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<ListingEntity> retrieveFavouriteListingsForCustomer(Long customerId) throws CustomerNotFoundException;

    public ListingEntity retrieveLatestListing();

    public ListingEntity retrieveMostPopularListing();

    public List<ListingEntity> retrieveMostPopularListingsForCategory(Long categoryId, Long customerId) throws RetrievePopularListingsException;

    public List<ListingEntity> retrieveListingsByCategoryName(String categoryName);

    public List<ListingEntity> retrieveListingsByListingName(String listingName) throws ListingNotFoundException;

    public List<ListingEntity> retrieveListingsByTag(Long tagId);

    public List<ListingEntity> retrieveListingsByTags(List<Long> tagIds)  throws TagNotFoundException;

    public void markListingAsPopular(Long listingId) throws ListingNotFoundException, TagNotFoundException;

    public void unmarkListingAsPopular(Long listingId) throws ListingNotFoundException, TagNotFoundException;

}

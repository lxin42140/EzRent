/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.ListingEntity;
import javax.ejb.Local;
import util.exception.CreateNewListingException;

/**
 *
 * @author kiyon
 */
@Local
public interface ListingEntitySessionBeanLocal {

    public Long createListing(CustomerEntity customer, ListingEntity listing) throws CreateNewListingException;
    
}

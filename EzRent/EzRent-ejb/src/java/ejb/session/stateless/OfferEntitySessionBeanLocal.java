/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OfferEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewOfferException;
import util.exception.CustomerNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.OfferNotFoundException;
import util.exception.UpdateOfferException;

/**
 *
 * @author kiyon
 */
@Local
public interface OfferEntitySessionBeanLocal {

    public Long createNewOffer(OfferEntity offer, Long customerId, Long listingId) throws CreateNewOfferException, ListingNotFoundException, CustomerNotFoundException;

    public List<OfferEntity> retrieveAllOffers();

    public OfferEntity retrieveOfferByOfferId(Long offerId) throws OfferNotFoundException;

    public List<OfferEntity> retrieveAllOffersByCustomer(Long customerId);

    public void acceptOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException;

    public void rejectOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException;

    public void cancelOffer(Long offerId) throws OfferNotFoundException, UpdateOfferException;

}

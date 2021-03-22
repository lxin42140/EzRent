/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RequestEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewRequestException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteRequestException;
import util.exception.FavouriteRequestException;
import util.exception.RequestNotFoundException;
import util.exception.UpdateRequestException;

/**
 *
 * @author Ziyue
 */
@Local
public interface RequestEntitySessionBeanLocal {

    public Long createNewRequest(Long customerId, RequestEntity requestEntity) throws CreateNewRequestException, CustomerNotFoundException;

    public void updateRequestDetails(Long requestId, RequestEntity requestEntityToUpdate) throws UpdateRequestException, RequestNotFoundException;

    public void deleteRequest(Long requestId) throws RequestNotFoundException, DeleteRequestException;

    public void toggleRequestLikeDislike(Long customerId, Long requestId) throws RequestNotFoundException, CustomerNotFoundException, FavouriteRequestException;

    public List<RequestEntity> retrieveAllRequests();

    public RequestEntity retrieveRequestByRequestId(Long requestId) throws RequestNotFoundException;

    public List<RequestEntity> retrieveFavouriteRequestsForCustomer(Long customerId) throws CustomerNotFoundException;

    public List<RequestEntity> retrieveRequestsByRequestName(String requestName);

}

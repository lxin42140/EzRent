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
import util.exception.RequestNotFoundException;
import util.exception.UpdateRequestException;

/**
 *
 * @author Ziyue
 */
@Local
public interface RequestEntitySessionBeanLocal {

    public Long createNewRequest(Long customerId, RequestEntity requestEntity) throws CreateNewRequestException, CustomerNotFoundException;

    public List<RequestEntity> retrieveAllRequests();

    public RequestEntity retrieveRequestByRequestId(Long requestId) throws RequestNotFoundException;

    public void updateRequestDetails(Long requestId, RequestEntity requestEntityToUpdate) throws UpdateRequestException, RequestNotFoundException;

    public void likeRequest(Long customerId, Long requestId) throws RequestNotFoundException, CustomerNotFoundException;

    public void unlikeRequest(Long customerId, Long requestId) throws RequestNotFoundException, CustomerNotFoundException;

    public void deleteRequest(Long requestId) throws RequestNotFoundException;
    
}

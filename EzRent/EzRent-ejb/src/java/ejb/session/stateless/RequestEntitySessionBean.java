/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.RequestEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewRequestException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteRequestException;
import util.exception.FavouriteRequestException;
import util.exception.RequestNotFoundException;
//import util.exception.UpdateRequestException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Ziyue
 */
@Stateless
public class RequestEntitySessionBean implements RequestEntitySessionBeanLocal {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public RequestEntity createNewRequest(Long customerId, RequestEntity requestEntity) throws CreateNewRequestException, CustomerNotFoundException {
        if (customerId == null) {
            throw new CreateNewRequestException("CreateNewRequestException: Invalid Customer ID!");
        }

        if (requestEntity == null) {
            throw new CreateNewRequestException("CreateNewRequestException: Invalid Request Entity!");
        }

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);

        //Associate with requestor
        requestEntity.setCustomer(customerEntity);
        customerEntity.getRequests().add(requestEntity);

        try {
            validate(requestEntity);
            em.persist(requestEntity);
            em.flush();
            return requestEntity;
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new CreateNewRequestException("CreateNewRequestException: " + ex.getMessage());
        }
    }

    @Override
    public List<RequestEntity> retrieveAllRequests() {
        Query query = em.createQuery("SELECT r FROM RequestEntity r");
        return query.getResultList();
    }

    @Override
    public RequestEntity retrieveRequestByRequestId(Long requestId) throws RequestNotFoundException {
        if (requestId == null) {
            throw new RequestNotFoundException("RequestNotFoundException: Request id: " + requestId + " is null!");
        }

        RequestEntity requestEntity = em.find(RequestEntity.class, requestId);
        if (requestEntity == null) {
            throw new RequestNotFoundException("RequestNotFoundException: Request id: " + requestId + " does not exist!");
        }

        return requestEntity;
    }

    @Override
    public List<RequestEntity> retrieveRequestsByRequestName(String requestName) {
        Query query = em.createQuery("select r from RequestEntity r where r.itemName like :inRequestName");
        query.setParameter("inRequestName", "%" + requestName + "%");
        return query.getResultList();
    }

    @Override
    public List<RequestEntity> retrieveFavouriteRequestsForCustomer(Long customerId) throws CustomerNotFoundException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        return customerEntity.getLikedRequests();
    }

    //retrieve all request by this particular user
    @Override
    public List<RequestEntity> retrieveRequestsByCustId(Long custId) {
        Query query = em.createQuery("SELECT r FROM RequestEntity r WHERE r.customer.userId = :inCustId");
        query.setParameter("inCustId", custId);

        return query.getResultList();
    }

//    @Override
//    public void updateRequestDetails(Long requestId, RequestEntity requestEntityToUpdate) throws UpdateRequestException, RequestNotFoundException {
//        if (requestEntityToUpdate == null || requestId == null) {
//            throw new UpdateRequestException("UpdateRequestException: Request Entity/ID input is null!");
//        }
//
//        RequestEntity existingRequestEntity = retrieveRequestByRequestId(requestId);
//
//        // update request name
//        existingRequestEntity.setRequestName(requestEntityToUpdate.getRequestName());
//        // update required date
//        existingRequestEntity.setRequiredDate(requestEntityToUpdate.getRequiredDate());
//        // update required duration
//        existingRequestEntity.setRequiredDuration(requestEntityToUpdate.getRequiredDuration());
//
//        try {
//            validate(existingRequestEntity);
//            em.merge(existingRequestEntity);
//            em.flush();
//        } catch (ValidationFailedException | PersistenceException ex) {
//            throw new UpdateRequestException("UpdateRequestException: " + ex.getMessage());
//        }
//    }
    @Override
    public void toggleRequestLikeDislike(Long customerId, Long requestId) throws RequestNotFoundException, CustomerNotFoundException, FavouriteRequestException {
        RequestEntity requestEntity = retrieveRequestByRequestId(requestId);
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerById(customerId);
        if (requestEntity.getCustomer().equals(customerEntity)) {
            throw new FavouriteRequestException("FavouriteRequestException: Cannot favourite own request!");
        }

        if (requestEntity.getLikedCustomers().contains(customerEntity)) {
            //toggle dislike
            requestEntity.getLikedCustomers().remove(customerEntity);
            customerEntity.getLikedRequests().remove(requestEntity);
        } else {
            //toggle like
            requestEntity.getLikedCustomers().add(customerEntity);
            customerEntity.getLikedRequests().add(requestEntity);
        }
    }

    @Override
    public void deleteRequest(Long requestId) throws RequestNotFoundException, DeleteRequestException {
        RequestEntity requestEntity = retrieveRequestByRequestId(requestId);

        for (CustomerEntity likedCustomer : requestEntity.getLikedCustomers()) {
            likedCustomer.getLikedRequests().remove(requestEntity);
        }
        requestEntity.getLikedCustomers().clear();

//        Disassociate requestor from request
        requestEntity.getCustomer().getRequests().remove(requestEntity);

        try {
            em.remove(requestEntity);
        } catch (PersistenceException ex) {
            throw new DeleteRequestException("DeleteRequestException: " + ex.getMessage());
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(RequestEntity requestEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<RequestEntity>> errors = validator.validate(requestEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

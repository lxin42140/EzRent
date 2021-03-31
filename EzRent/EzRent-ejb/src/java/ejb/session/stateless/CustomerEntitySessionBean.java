/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.UserAccessRightEnum;
import util.exception.CreateNewCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.UpdateCustomerException;
import util.exception.ValidationFailedException;
import util.security.CryptographicHelper;

/**
 *
 * @author Li Xin
 */
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CreateNewCustomerException {
        if (newCustomerEntity == null) {
            throw new CreateNewCustomerException("CreateNewCustomerException: Invalid new customer!");
        }

        // set access right to customer
        newCustomerEntity.setAccessRight(UserAccessRightEnum.CUSTOMER);
        newCustomerEntity.setAverageRating(0.0);
        newCustomerEntity.setDateJoined(new Date());
        newCustomerEntity.setBio("No Bio Yet...");
        newCustomerEntity.setIsDeleted(false);
        newCustomerEntity.setIsDisable(false);
        

        try {
            validate(newCustomerEntity);
            em.persist(newCustomerEntity);
            em.flush();
            return newCustomerEntity.getUserId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewCustomerException("CreateNewCustomerException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCustomerException("CreateNewCustomerException: Customer with same username/user ID already exists!");
            } else {
                throw new CreateNewCustomerException("CreateNewCustomerException: " + ex.getMessage());
            }
        }
    }

    @Override
    public Long updateCustomerProfileDetails(Long customerId, CustomerEntity customerEntityToUpdate) throws CustomerNotFoundException, UpdateCustomerException {
        if (customerEntityToUpdate == null || customerId == null) {
            throw new UpdateCustomerException("UpdateCustomerFailedException: Please provide valid customer/customer ID");
        }

        CustomerEntity existingCustomerEntity = em.find(CustomerEntity.class, customerId);
        if (existingCustomerEntity == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer with customer ID " + customerId + " does not exist!");
        }

        // update profile pic
        if (!customerEntityToUpdate.getFilePathName().equals(existingCustomerEntity.getFilePathName())) {
            existingCustomerEntity.setFilePathName(customerEntityToUpdate.getFilePathName());
        }
        // update street name
        if (!customerEntityToUpdate.getStreetName().equals(existingCustomerEntity.getStreetName())) {
            existingCustomerEntity.setStreetName(customerEntityToUpdate.getStreetName());
        }
        //update postal code
        if (!customerEntityToUpdate.getPostalCode().equals(existingCustomerEntity.getPostalCode())) {
            existingCustomerEntity.setPostalCode(customerEntityToUpdate.getPostalCode());
        }
        //update bio
        if (!customerEntityToUpdate.getBio().equals(existingCustomerEntity.getBio())) {
            existingCustomerEntity.setBio(customerEntityToUpdate.getBio());
        }
        //update average rating
        if (!customerEntityToUpdate.getAverageRating().equals(existingCustomerEntity.getAverageRating())) {
            existingCustomerEntity.setAverageRating(customerEntityToUpdate.getAverageRating());
        }
        //update first name
        if (!customerEntityToUpdate.getFirstName().equals(existingCustomerEntity.getFirstName())) {
            existingCustomerEntity.setFirstName(customerEntityToUpdate.getFirstName());
        }
        //update last name
        if (!customerEntityToUpdate.getLastName().equals(existingCustomerEntity.getLastName())) {
            existingCustomerEntity.setLastName(customerEntityToUpdate.getLastName());
        }
        //update email
        if (!customerEntityToUpdate.getEmail().equals(existingCustomerEntity.getEmail())) {
            existingCustomerEntity.setEmail(customerEntityToUpdate.getEmail());
        }

        try {
            validate(existingCustomerEntity);
            em.merge(existingCustomerEntity);
            em.flush();
            return existingCustomerEntity.getUserId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateCustomerException("UpdateCustomerFailedException: " + ex.getMessage());
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByUsernameAndPassword(String username, String password) throws CustomerNotFoundException, InvalidLoginException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidLoginException("InvalidLoginException: Please enter username/password!");
        }

        try {
            Query query = em.createNamedQuery("retrieveCustomerByUsernameAndPassword");
            query.setParameter("inUsername", username);
            CustomerEntity customer = (CustomerEntity) query.getSingleResult();

            //password stored in db is hashed with salt
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customer.getSalt()));
            if (!customer.getPassword().equals(passwordHash)) {
                throw new InvalidLoginException("InvalidLoginException: Invalid password!");
            }

            return customer;
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer with username " + username + " does not exist!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        try {
            Query query = em.createQuery("select c from CustomerEntity c where c.userName =:inUsername");
            query.setParameter("inUsername", username);
            CustomerEntity customer = (CustomerEntity) query.getSingleResult();
            return customer;
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer with username " + username + " does not exist!");
        }
    }

    @Override
    public List<CustomerEntity> retrieveAllCustomers() {
        Query query = em.createNamedQuery("retrieveAllUndeletedCustomers");
        return query.getResultList();
    }

    @Override
    public List<CustomerEntity> retrieveAllDisabledCustomers() {
        Query query = em.createNamedQuery("retrieveAllDisabledCustomers");
        return query.getResultList();
    }

    @Override
    public CustomerEntity retrieveCustomerById(Long customerId) throws CustomerNotFoundException {
        if (customerId == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Please enter a valid ID!");
        }

        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
        if (customerEntity == null) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer with id " + customerId + " does not exist!");
        }
        
        customerEntity.getConversations().size();
        customerEntity.getCreditCards().size();
        customerEntity.getDamageReports().size();
        customerEntity.getLikedListings().size();
        customerEntity.getLikedRequests().size();
        customerEntity.getListings().size();
        customerEntity.getOffers().size();
        customerEntity.getReports().size();
        customerEntity.getRequests().size();
        customerEntity.getReviews().size();
        
        return customerEntity;
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CustomerEntity newCustomerEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CustomerEntity>> errors = validator.validate(newCustomerEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

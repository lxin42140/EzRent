/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
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

/**
 *
 * @author Li Xin
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CreateNewCustomerException {
        if (newCustomerEntity.getAccessRight() != UserAccessRightEnum.CUSTOMER) {
            throw new CreateNewCustomerException("CreateNewCustomerException: Invalid access right " + newCustomerEntity.getAccessRight() + " assigned to customer!");
        }

        validate(newCustomerEntity);

        try {
            em.persist(newCustomerEntity);
            em.flush();

            return newCustomerEntity.getUserId();
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewCustomerException("CreateNewCustomerException: Customer with same user ID already exists!");
            } else {
                throw new CreateNewCustomerException("CreateNewCustomerException: " + ex.getMessage());
            }
        }
    }

    public CustomerEntity retrieveCustomerByUsernameAndPassword(String username, String password) throws CustomerNotFoundException, InvalidLoginException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidLoginException("InvalidLoginException: Please enter username/password!");
        }

        try {
            Query query = em.createNamedQuery("retrieveCustomerByUsernameAndPassword");
            query.setParameter("inUsername", username);
            CustomerEntity customer = (CustomerEntity) query.getSingleResult();

            //password stored in db is hashed with salt
            if (!customer.getPassword().equals(password + customer.getSalt())) {
                throw new InvalidLoginException("InvalidLoginException: Invalid password!");
            }
            return customer;
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("CustomerNotFoundException: Customer with username " + username + " does not exist!");
        }
    }

    public List<CustomerEntity> retrieveAllCustomers() {
        Query query = em.createNamedQuery("retrieveAllUndeletedCustomers");
        return query.getResultList();
    }

    public List<CustomerEntity> retrieveAllDisabledCustomers() {
        Query query = em.createNamedQuery("retrieveAllDisabledCustomers");
        return query.getResultList();
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(CustomerEntity newCustomerEntity) throws CreateNewCustomerException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CustomerEntity>> errors = validator.validate(newCustomerEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new CreateNewCustomerException("CreateNewCustomerException: Invalid inputs!\n" + errorMessage);
        }
    }
}

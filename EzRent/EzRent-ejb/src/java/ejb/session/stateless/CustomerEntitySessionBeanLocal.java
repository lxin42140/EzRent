/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.UpdateCustomerException;
import util.exception.UpdateCustomerFailException;

/**
 *
 * @author Li Xin
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CreateNewCustomerException;

    public Long updateCustomerProfileDetails(Long customerId, CustomerEntity customerEntityToUpdate) throws CustomerNotFoundException, UpdateCustomerException;

    public List<CustomerEntity> retrieveAllCustomers();

    public List<CustomerEntity> retrieveAllDisabledCustomers();

    public CustomerEntity retrieveCustomerById(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByUsernameAndPassword(String username, String password) throws CustomerNotFoundException, InvalidLoginException;

    public CustomerEntity updateCustomerStatus(Long customerId, Boolean isDisabled) throws UpdateCustomerFailException, CustomerNotFoundException;

}

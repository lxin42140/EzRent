/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.DeliveryCompanyEntity;
import entity.DeliveryEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.DeliveryStatusEnum;
import util.exception.DeleteUserException;
import util.exception.UpdateUserFailedException;
import util.exception.UserNotFoundException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Li Xin
 */
@Stateless
public class UserEntitySessionBean implements UserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long updateUserUsername(Long userId, String newUsername) throws UserNotFoundException, UpdateUserFailedException {
        if (userId == null || newUsername == null) {
            throw new UpdateUserFailedException("UpdateUserFailedException: Please provide valid user ID/username");
        }

        //check whether username is unique
        Query query = em.createQuery("select C from UserEntity c where c.userName :=inUsername");
        query.setParameter("inUsername", newUsername);
        List<UserEntity> userswithSameUsernames = query.getResultList();
        if (!userswithSameUsernames.isEmpty()) {
            throw new UpdateUserFailedException("UpdateUserFailedException: Users with same username already exists!");
        }

        UserEntity existingUserEntity = em.find(UserEntity.class, userId);
        if (existingUserEntity == null) {
            throw new UserNotFoundException("UserNotFoundException: User with user ID " + userId + " does not exist!");
        }
        existingUserEntity.setUserName(newUsername);

        try {
            validate(existingUserEntity);
            em.merge(existingUserEntity);
            em.flush();
            return existingUserEntity.getUserId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateUserFailedException("UpdateUserFailedException: " + ex.getMessage());
        }
    }

    @Override
    public Long updateUserPassword(Long userId, String newPassword) throws UserNotFoundException, UpdateUserFailedException {
        if (userId == null || newPassword == null) {
            throw new UpdateUserFailedException("UpdateUserFailedException: Please provide valid user ID/password");
        }

        UserEntity existingUserEntity = em.find(UserEntity.class, userId);
        if (existingUserEntity == null) {
            throw new UserNotFoundException("UserNotFoundException: User with user ID " + userId + " does not exist!");
        }
        existingUserEntity.setPassword(newPassword);

        try {
            validate(existingUserEntity);
            em.merge(existingUserEntity);
            em.flush();
            return existingUserEntity.getUserId();
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateUserFailedException("UpdateUserFailedException: " + ex.getMessage());
        }
    }

    @Override
    // NEED TO UPDATE
    public Long deleteUserById(Long userID) throws UserNotFoundException, DeleteUserException {
        if (userID == null) {
            throw new DeleteUserException("DeleteUserException: Please enter a valid userID!");
        }

        UserEntity existingUserEntity = em.find(UserEntity.class, userID);
        if (existingUserEntity == null) {
            throw new UserNotFoundException("UserNotFoundException: User with user ID " + userID + " does not exist!");
        }

        if (existingUserEntity instanceof CustomerEntity) {
            CustomerEntity tempCustomerEntity = (CustomerEntity) existingUserEntity;
            // cannot delete a customer that has an active transaction
        } else if (existingUserEntity instanceof DeliveryCompanyEntity) {
            DeliveryCompanyEntity tempDeliveryCompanyEntity = (DeliveryCompanyEntity) existingUserEntity;
            // check whether the company has active deliveres
            List<DeliveryEntity> deliveries = tempDeliveryCompanyEntity.getDeliveries();
            for (DeliveryEntity deliveryEntity : deliveries) {
                if (deliveryEntity.getDeliveryStatus() != DeliveryStatusEnum.DELIVERED || deliveryEntity.getDeliveryStatus() != DeliveryStatusEnum.LOST) {
                    throw new DeleteUserException("DeleteUserException: You are trying to delete a delivery company that has active deliveries!");
                }
            }
        }

        existingUserEntity.setIsDeleted(true);
        em.merge(existingUserEntity);
        em.flush();
        return existingUserEntity.getUserId();
    }

    @Override
    public Long toggleUserAccountDisable(Long userID, Boolean disableUser) throws UserNotFoundException {
        UserEntity existingUserEntity = em.find(UserEntity.class, userID);
        if (existingUserEntity == null) {
            throw new UserNotFoundException("UserNotFoundException: User with user ID " + userID + " does not exist!");
        }
        existingUserEntity.setIsDeleted(true);
        em.merge(existingUserEntity);
        em.flush();
        return existingUserEntity.getUserId();
    }

    @Override
    public List<UserEntity> retrieveAllUsers() {
        Query query = em.createNamedQuery("retrieveAllUndeletedUsers");
        return query.getResultList();
    }

    @Override
    public List<UserEntity> retrieveAllDisabledUsers() {
        Query query = em.createNamedQuery("retrieveAllDisabledUsers");
        return query.getResultList();
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(UserEntity userEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserEntity>> errors = validator.validate(userEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }

}

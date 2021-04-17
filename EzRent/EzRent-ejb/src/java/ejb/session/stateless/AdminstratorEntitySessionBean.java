/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdministratorEntity;
import entity.UserEntity;
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
import util.exception.AdminNotFoundException;
import util.exception.CreateNewAdminstratorException;
import util.exception.InvalidLoginException;
import util.exception.UpdateAdminFailException;
import util.exception.ValidationFailedException;
import util.security.CryptographicHelper;

/**
 *
 * @author Li Xin
 */
@Stateless
public class AdminstratorEntitySessionBean implements AdminstratorEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewAdminstrator(AdministratorEntity newAdministratorEntity) throws CreateNewAdminstratorException {
        if (newAdministratorEntity == null) {
            throw new CreateNewAdminstratorException("CreateNewAdminstratorException: Invalid new adminstrator!");
        }

        newAdministratorEntity.setAccessRight(UserAccessRightEnum.ADMINSTRATOR);

        try {
            validate(newAdministratorEntity);
            em.persist(newAdministratorEntity);
            em.flush();
            return newAdministratorEntity.getUserId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewAdminstratorException("CreateNewAdminstratorException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewAdminstratorException("CreateNewAdminstratorException: Adminstrator with same username/user ID already exists!");
            } else {
                throw new CreateNewAdminstratorException("CreateNewAdminstratorException: " + ex.getMessage());
            }
        }
    }

    @Override
    public AdministratorEntity retrieveAdminByUsernameAndPassword(String username, String password) throws AdminNotFoundException, InvalidLoginException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new InvalidLoginException("InvalidLoginException: Please enter username/password!");
        }

        try {
            Query query = em.createNamedQuery("retrieveAdminByUsernameAndPassword");
            query.setParameter("inUsername", username);
            AdministratorEntity admin = (AdministratorEntity) query.getSingleResult();

            //password stored in db is hashed with salt
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + admin.getSalt()));
            if (!admin.getPassword().equals(passwordHash)) {
                throw new InvalidLoginException("InvalidLoginException: Invalid password!");
            }

            return admin;
        } catch (NoResultException ex) {
            throw new AdminNotFoundException("AdminNotFoundException: Admin with username " + username + " does not exist!");
        }
    }

    @Override
    public List<AdministratorEntity> retrieveAllAdminstrators() {
        Query query = em.createNamedQuery("retrieveAllUndeletedAdminstrators");
        return query.getResultList();
    }

    @Override
    public List<AdministratorEntity> retrieveAllDisabledAdminstrators() {
        Query query = em.createNamedQuery("retrieveAllDisabledAdminstrators");
        return query.getResultList();
    }

    @Override
    public AdministratorEntity retrieveAdminByAdminId(Long adminId) {
        Query query = em.createQuery("SELECT a FROM AdministratorEntity a WHERE a.userId =:inUserId");
        query.setParameter("inUserId", adminId);
        return (AdministratorEntity) query.getSingleResult();
    }

    @Override
    public AdministratorEntity updateAdminStatus(Long adminId, Boolean isDisabled) throws UpdateAdminFailException, AdminNotFoundException {
        if (adminId == null) {
            throw new UpdateAdminFailException("UpdateAdminFailException: Please provide a valid admin id!");
        }
        AdministratorEntity admin = this.retrieveAdminByAdminId(adminId);
        admin.setIsDisable(isDisabled);

        try {
            validate(admin);
            em.merge(admin);
            return admin;
        } catch (ValidationFailedException | PersistenceException ex) {
            throw new UpdateAdminFailException("UpdateAdminFailException: " + ex.getMessage());
        }
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

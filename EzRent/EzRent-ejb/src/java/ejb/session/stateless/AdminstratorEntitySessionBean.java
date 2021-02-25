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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.UserAccessRightEnum;
import util.exception.CreateNewAdminstratorException;
import util.exception.ValidationFailedException;

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
    public List<AdministratorEntity> retrieveAllAdminstrators() {
        Query query = em.createNamedQuery("retrieveAllUndeletedAdminstrators");
        return query.getResultList();
    }

    @Override
    public List<AdministratorEntity> retrieveAllDisabledAdminstrators() {
        Query query = em.createNamedQuery("retrieveAllDisabledAdminstrators");
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

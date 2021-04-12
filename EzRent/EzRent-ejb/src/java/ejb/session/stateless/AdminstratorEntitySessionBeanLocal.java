/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AdministratorEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.AdminNotFoundException;
import util.exception.CreateNewAdminstratorException;
import util.exception.InvalidLoginException;
import util.exception.UpdateAdminFailException;

/**
 *
 * @author Li Xin
 */
@Local
public interface AdminstratorEntitySessionBeanLocal {

    public Long createNewAdminstrator(AdministratorEntity newAdministratorEntity) throws CreateNewAdminstratorException;

    public List<AdministratorEntity> retrieveAllAdminstrators();

    public List<AdministratorEntity> retrieveAllDisabledAdminstrators();

    public AdministratorEntity retrieveAdminByUsernameAndPassword(String username, String password) throws AdminNotFoundException, InvalidLoginException;

    public AdministratorEntity retrieveAdminByAdminId(Long adminId);

    public Long updateAdminStatus(Long adminId, Boolean isDisabled) throws UpdateAdminFailException, AdminNotFoundException;

}

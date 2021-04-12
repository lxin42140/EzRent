/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Yuxin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "retrieveAdminByUsernameAndPassword", query = "select a from AdministratorEntity a where a.userName =:inUsername"),
    @NamedQuery(name = "retrieveAllUndeletedAdminstrators", query = "select a from AdministratorEntity a where a.isDeleted = FALSE"),
    @NamedQuery(name = "retrieveAllDisabledAdminstrators", query = "SELECT a from AdministratorEntity a where a.isDisable = TRUE")
})
public class AdministratorEntity extends UserEntity implements Serializable {

    public AdministratorEntity() {
    }

    public AdministratorEntity(String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
    }

    @Override
    public String toString() {
        return "entity.AdministratorEntity[ id=" + userId + " ]";
    }
}

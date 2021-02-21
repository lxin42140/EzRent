/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class DeliveryCompany extends UserEntity implements Serializable {

    @Column(nullable = false)
    @NotNull
    private String companyName;

    @Column(nullable = false)
    @NotNull
    private String companyUEN;

    public DeliveryCompany() {
    }

    public DeliveryCompany(String companyName, String companyUEN, String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
        this.companyName = companyName;
        this.companyUEN = companyUEN;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUEN() {
        return companyUEN;
    }

    public void setCompanyUEN(String companyUEN) {
        this.companyUEN = companyUEN;
    }

    @Override
    public String toString() {
        return "entity.DeliveryCompany[ id=" + this.userId + " ]";
    }

}

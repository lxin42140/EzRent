/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.UserAccessRightEnum;
import java.util.List;

/**
 *
 * @author Yuxin
 */
@Entity
public class DeliveryCompany extends UserEntity implements Serializable {

    @Column(nullable = false, length = 50)
    @NotNull
    @Size(min = 5, max = 50)
    private String companyName;

    @Column(nullable = false, length = 50)
    @NotNull
    @Size(min = 5, max = 50)
    private String companyUEN;

    @OneToMany(mappedBy = "deliveryCompany")
    private List<DeliveryEntity> deliveries;

    public DeliveryCompany() {
    }

    public DeliveryCompany(String companyName, String companyUEN, String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
        this.companyName = companyName;
        this.companyUEN = companyUEN;
        this.deliveries = new ArrayList<>();
    }

    public List<DeliveryEntity> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryEntity> deliveries) {
        this.deliveries = deliveries;
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

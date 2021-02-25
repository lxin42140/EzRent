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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Yuxin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "retrieveAllUndeletedDeliveryCompanies", query = "select d from DeliveryCompanyEntity d where d.isDeleted = FALSE"),
    @NamedQuery(name = "retrieveAllDisabledDeliveryCompanies", query = "SELECT d from DeliveryCompanyEntity d where d.isDisable = TRUE"),
    @NamedQuery(name = "retrieveDeliveryCompanyById", query = "SELECT d from DeliveryCompanyEntity d where d.userId =:inCompanyId")
})
public class DeliveryCompanyEntity extends UserEntity implements Serializable {

    @Column(nullable = false, length = 50, unique = true)
    @NotNull
    @Size(min = 5, max = 50)
    private String companyName;

    @Column(nullable = false, length = 50, unique = true)
    @NotNull
    @Size(min = 5, max = 50)
    private String companyUEN;

    @Column(nullable = false, length = 50)
    @NotNull
    @Size(min = 5, max = 50)
    private String companyContactNumber;

    @OneToMany(mappedBy = "deliveryCompany")
    private List<DeliveryEntity> deliveries;

    public DeliveryCompanyEntity() {
    }

    public DeliveryCompanyEntity(String companyName, String companyUEN, String companyContactNumber, String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
        this.companyName = companyName;
        this.companyUEN = companyUEN;
        this.companyContactNumber = companyContactNumber;
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

    public String getCompanyContactNumber() {
        return companyContactNumber;
    }

    public void setCompanyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
    }

    @Override
    public String toString() {
        return "entity.DeliveryCompany[ id=" + this.userId + " ]";
    }

}

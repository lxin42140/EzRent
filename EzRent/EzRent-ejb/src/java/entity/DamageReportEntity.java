/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import util.enumeration.DamageReportEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class DamageReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long damageReportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DamageReportEnum damageReportStatus;

    @Column(nullable = false)
    @NotNull
    private String damageDescription;

    @Column(nullable = false)
    @NotNull
    private String damagePhotoLink;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "customerId")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "transaction")
    private TransactionEntity transaction;

    public DamageReportEntity() {
        this.damageReportStatus = DamageReportEnum.PENDING;
    }

    public DamageReportEntity(String damageDescription, String damagePhotoLink) {
        this();
        this.damageDescription = damageDescription;
        this.damagePhotoLink = damagePhotoLink;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Long getDamageReportId() {
        return damageReportId;
    }

    public DamageReportEnum getDamageReportStatus() {
        return damageReportStatus;
    }

    public void setDamageReportStatus(DamageReportEnum damageReportStatus) {
        this.damageReportStatus = damageReportStatus;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public String getDamagePhotoLink() {
        return damagePhotoLink;
    }

    public void setDamagePhotoLink(String damagePhotoLink) {
        this.damagePhotoLink = damagePhotoLink;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (damageReportId != null ? damageReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the damageReportId fields are not set
        if (!(object instanceof DamageReportEntity)) {
            return false;
        }
        DamageReportEntity other = (DamageReportEntity) object;
        if ((this.damageReportId == null && other.damageReportId != null) || (this.damageReportId != null && !this.damageReportId.equals(other.damageReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DamageReport[ id=" + damageReportId + " ]";
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

}

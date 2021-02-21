/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Yuxin
 */
@Entity
public class DeliveryCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @Column(nullable = false)
    @NotNull
    private String companyName;

    @Column(nullable = false)
    @NotNull
    private String companyUEN;

    @Column(nullable = false)
    @NotNull
    private String POCContactNumber;

    @Column(nullable = false)
    @NotNull
    private String POCEmail;

    @Column(nullable = false)
    @NotNull
    private String POCName;

    public DeliveryCompany() {
    }

    public DeliveryCompany(String companyName, String companyUEN, String POCContactNumber, String POCEmail, String POCName) {
        this();

        this.companyName = companyName;
        this.companyUEN = companyUEN;
        this.POCContactNumber = POCContactNumber;
        this.POCEmail = POCEmail;
        this.POCName = POCName;
    }

    public Long getCompanyId() {
        return companyId;
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

    public String getPOCContactNumber() {
        return POCContactNumber;
    }

    public void setPOCContactNumber(String POCContactNumber) {
        this.POCContactNumber = POCContactNumber;
    }

    public String getPOCEmail() {
        return POCEmail;
    }

    public void setPOCEmail(String POCEmail) {
        this.POCEmail = POCEmail;
    }

    public String getPOCName() {
        return POCName;
    }

    public void setPOCName(String POCName) {
        this.POCName = POCName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyId != null ? companyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the companyId fields are not set
        if (!(object instanceof DeliveryCompany)) {
            return false;
        }
        DeliveryCompany other = (DeliveryCompany) object;
        if ((this.companyId == null && other.companyId != null) || (this.companyId != null && !this.companyId.equals(other.companyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DeliveryCompany[ id=" + companyId + " ]";
    }

}

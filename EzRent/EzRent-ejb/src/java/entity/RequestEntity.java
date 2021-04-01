/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.RequestUrgencyEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(nullable = false)
    @NotNull
    private String itemName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RequestUrgencyEnum requestUrgencyEnum;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date datePosted;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date requiredStartDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date requiredEndDate;

    private String description;

    @NotNull
    @Column(nullable = false)
    private String filePathName;

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(nullable = false, name = "customerId")
    private CustomerEntity customer;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<CustomerEntity> likedCustomers;

    public RequestEntity() {
        this.likedCustomers = new ArrayList<>();
    }

    public RequestEntity(String itemName, RequestUrgencyEnum requestUrgencyEnum, Date datePosted, Date requiredStartDate, Date requiredEndDate, String description, String filePathName) {
        this();
        this.itemName = itemName;
        this.requestUrgencyEnum = requestUrgencyEnum;
        this.datePosted = datePosted;
        this.requiredStartDate = requiredStartDate;
        this.requiredEndDate = requiredEndDate;
        this.description = description;
        this.filePathName = filePathName;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    public Date getRequiredEndDate() {
        return requiredEndDate;
    }

    public void setRequiredEndDate(Date requiredEndDate) {
        this.requiredEndDate = requiredEndDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public List<CustomerEntity> getLikedCustomers() {
        return likedCustomers;
    }

    public void setLikedCustomers(List<CustomerEntity> likedCustomers) {
        this.likedCustomers = likedCustomers;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getRequiredStartDate() {
        return requiredStartDate;
    }

    public void setRequiredStartDate(Date requiredStartDate) {
        this.requiredStartDate = requiredStartDate;
    }

    public RequestUrgencyEnum getRequestUrgencyEnum() {
        return requestUrgencyEnum;
    }

    public void setRequestUrgencyEnum(RequestUrgencyEnum requestUrgencyEnum) {
        this.requestUrgencyEnum = requestUrgencyEnum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requestId != null ? requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the requestId fields are not set
        if (!(object instanceof RequestEntity)) {
            return false;
        }
        RequestEntity other = (RequestEntity) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Request[ id=" + requestId + " ]";
    }

}

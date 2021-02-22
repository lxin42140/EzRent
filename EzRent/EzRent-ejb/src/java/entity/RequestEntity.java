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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    private String requestName;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date datePosted;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date requiredDate;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer requiredDuration;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "customerId")
    private CustomerEntity customer;

    @ManyToMany
    private List<CustomerEntity> likedCustomers;

    public RequestEntity() {
        this.likedCustomers = new ArrayList<>();
    }

    public RequestEntity(String requestName, Date datePosted, Date requiredDate, Integer requiredDuration) {
        this();

        this.requestName = requestName;
        this.datePosted = datePosted;
        this.requiredDate = requiredDate;
        this.requiredDuration = requiredDuration;
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

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Integer getRequiredDuration() {
        return requiredDuration;
    }

    public void setRequiredDuration(Integer requiredDuration) {
        this.requiredDuration = requiredDuration;
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

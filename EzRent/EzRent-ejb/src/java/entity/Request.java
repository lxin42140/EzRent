/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Yuxin
 */
@Entity
public class Request implements Serializable {

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
    @Min(0)
    private Integer requiredDuration;

    public Request() {
    }

    public Request(String requestName, Date datePosted, Date requiredDate, Integer requiredDuration) {
        this();
        
        this.requestName = requestName;
        this.datePosted = datePosted;
        this.requiredDate = requiredDate;
        this.requiredDuration = requiredDuration;
    }
    
    
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.requestId == null && other.requestId != null) || (this.requestId != null && !this.requestId.equals(other.requestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Request[ id=" + requestId + " ]";
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
    
}

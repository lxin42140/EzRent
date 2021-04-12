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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.DeliveryStatusEnum;

/**
 *
 * @author Yuxin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "retrieveAllDeliveries", query = "select d from DeliveryEntity d"),
    @NamedQuery(name = "retrieveDeliveryByDeliveryStatus", query = "select d from DeliveryEntity d where d.deliveryStatus =:inDeliveryStatus"),
    @NamedQuery(name = "retrieveDeliveryByDeliveryId", query = "select d from DeliveryEntity d where d.deliveryId =:inDeliveryId")
})
public class DeliveryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DeliveryStatusEnum deliveryStatus;

    @Column(length = 255)
    private String deliveryComment;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastUpateDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "deliveryCompanyId")
    private DeliveryCompanyEntity deliveryCompany;

    @OneToOne(optional = false, mappedBy = "delivery")
    private TransactionEntity transaction;

    public DeliveryEntity() {
    }

    public DeliveryEntity(DeliveryStatusEnum deliveryStatus, String deliveryComment, Date lastUpateDate) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.lastUpateDate = lastUpateDate;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public Date getLastUpateDate() {
        return lastUpateDate;
    }

    public void setLastUpateDate(Date lastUpateDate) {
        this.lastUpateDate = lastUpateDate;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public DeliveryCompanyEntity getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(DeliveryCompanyEntity deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public DeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryComment() {
        return deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliveryId != null ? deliveryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the deliveryId fields are not set
        if (!(object instanceof DeliveryEntity)) {
            return false;
        }
        DeliveryEntity other = (DeliveryEntity) object;
        if ((this.deliveryId == null && other.deliveryId != null) || (this.deliveryId != null && !this.deliveryId.equals(other.deliveryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Delivery[ id=" + deliveryId + " ]";
    }
}

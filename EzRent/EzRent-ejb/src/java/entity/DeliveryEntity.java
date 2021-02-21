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
import util.enumeration.DeliveryStatusEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class DeliveryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DeliveryStatusEnum deliveryStatus;

    private String deliveryComment;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private DeliveryCompany deliveryCompany;

    @OneToOne(optional = false, mappedBy = "delivery")
    @Column(nullable = false)
    private TransactionEntity transaction;

    public DeliveryEntity() {
    }

    public DeliveryEntity(Long deliveryId, DeliveryStatusEnum deliveryStatus, String deliveryComment, DeliveryCompany deliveryCompany, TransactionEntity transaction) {
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.deliveryCompany = deliveryCompany;
        this.transaction = transaction;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public DeliveryCompany getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(DeliveryCompany deliveryCompany) {
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

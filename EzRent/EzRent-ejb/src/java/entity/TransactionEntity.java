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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.TransactionStatusEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class TransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date transactionStartDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date transactionEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TransactionStatusEnum transactionStatus;

    @OneToOne(optional = true)
    @JoinColumn(name = "deliveryId")
    // A transaction can be meet-up
    private DeliveryEntity delivery;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false, name = "offerId")
    private OfferEntity offer;

    @OneToOne(optional = false, mappedBy = "transaction")
    @JoinColumn(nullable = false, name = "paymentId")
    private PaymentEntity payment;

    @OneToMany
    @Size(max = 2)
    // review can be empty, but should not exceed 2 reviews
    // one review for lessor, one review for lessee
    private List<ReviewEntity> reviews;

    public TransactionEntity() {
        this.reviews = new ArrayList<>();
    }

    public TransactionEntity(Date transactionStartDate, Date transactionEndDate, TransactionStatusEnum transactionStatus, DeliveryEntity delivery, OfferEntity offer, PaymentEntity payment) {
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
        this.transactionStatus = transactionStatus;
        this.delivery = delivery;
        this.offer = offer;
        this.payment = payment;
    }

    public TransactionEntity(Date transactionStartDate, Date transactionEndDate, TransactionStatusEnum transactionStatus, OfferEntity offer, PaymentEntity payment) {
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
        this.transactionStatus = transactionStatus;
        this.offer = offer;
        this.payment = payment;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public DeliveryEntity getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryEntity delivery) {
        this.delivery = delivery;
    }

    public Date getTransactionStartDate() {
        return transactionStartDate;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public void setTransactionStartDate(Date transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    public Date getTransactionEndDate() {
        return transactionEndDate;
    }

    public void setTransactionEndDate(Date transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    public TransactionStatusEnum getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionId fields are not set
        if (!(object instanceof TransactionEntity)) {
            return false;
        }
        TransactionEntity other = (TransactionEntity) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Transaction[ id=" + transactionId + " ]";
    }

}

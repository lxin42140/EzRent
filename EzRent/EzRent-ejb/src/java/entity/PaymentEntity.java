/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class PaymentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @Positive
    private BigDecimal paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ModeOfPaymentEnum modeOfPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private PaymentStatusEnum paymentStatus;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false, name = "transactionId")
    private TransactionEntity transaction;

    @ManyToOne
    // Payment can be made via cash, so credit card can be null
    @JoinColumn(name = "creditCardId")
    private CreditCardEntity creditCard;

    public PaymentEntity() {
    }

    public PaymentEntity(Date paymentDate, BigDecimal paymentAmount, ModeOfPaymentEnum modeOfPayment, PaymentStatusEnum paymentStatus, TransactionEntity transaction) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.modeOfPayment = modeOfPayment;
        this.paymentStatus = paymentStatus;
        this.transaction = transaction;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public CreditCardEntity getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardEntity creditCard) {
        this.creditCard = creditCard;
    }

    public TransactionEntity getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionEntity transaction) {
        this.transaction = transaction;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public ModeOfPaymentEnum getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPaymentEnum modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paymentId != null ? paymentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the paymentId fields are not set
        if (!(object instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity other = (PaymentEntity) object;
        if ((this.paymentId == null && other.paymentId != null) || (this.paymentId != null && !this.paymentId.equals(other.paymentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Payment[ id=" + paymentId + " ]";
    }
}

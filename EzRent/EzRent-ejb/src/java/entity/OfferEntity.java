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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.OfferStatusEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class OfferEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOffered;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date rentalStartDate;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date rentalEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private OfferStatusEnum offerStatus;

    public OfferEntity() {
    }

    public OfferEntity(Date dateOffered, Date rentalStartDate, Date rentalEndDate, OfferStatusEnum offerStatus) {
        this();

        this.dateOffered = dateOffered;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.offerStatus = offerStatus;
    }

    public Long getOfferId() {
        return offerId;
    }

    public Date getDateOffered() {
        return dateOffered;
    }

    public void setDateOffered(Date dateOffered) {
        this.dateOffered = dateOffered;
    }

    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public Date getRentalEndDate() {
        return rentalEndDate;
    }

    public void setRentalEndDate(Date rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    public OfferStatusEnum getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatusEnum offerStatus) {
        this.offerStatus = offerStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerId != null ? offerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the offerId fields are not set
        if (!(object instanceof OfferEntity)) {
            return false;
        }
        OfferEntity other = (OfferEntity) object;
        if ((this.offerId == null && other.offerId != null) || (this.offerId != null && !this.offerId.equals(other.offerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Offer[ id=" + offerId + " ]";
    }
}
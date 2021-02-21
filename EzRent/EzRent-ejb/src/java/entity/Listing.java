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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import util.enumeration.AvailabilityEnum;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.ModeOfPaymentEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class Listing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;
    
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String listingName;
    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.00")
    private Double price;
    @Column(nullable = false)
    @NotNull
    private String description;
    @Column(nullable = false)
    @NotNull
    private String location;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOfPost;
    @Column(nullable = false)
    @NotNull
    private Integer minRentalDuration;
    @Column(nullable = false)
    @NotNull
    private Integer maxRentalDuration;
    @Column(nullable = false)
    @NotNull
    @Positive
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double itemCondition;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DeliveryOptionEnum deliveryOption;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AvailabilityEnum availability;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ModeOfPaymentEnum modeOfPayment;

    public Listing() {
    }

    public Listing(String listingName, Double price, String description, String location, Date dateOfPost, Integer minRentalDuration, Integer maxRentalDuration, Double itemCondition, DeliveryOptionEnum deliveryOption, AvailabilityEnum availability, ModeOfPaymentEnum modeOfPayment) {
        this();
        this.listingName = listingName;
        this.price = price;
        this.description = description;
        this.location = location;
        this.dateOfPost = dateOfPost;
        this.minRentalDuration = minRentalDuration;
        this.maxRentalDuration = maxRentalDuration;
        this.itemCondition = itemCondition;
        this.deliveryOption = deliveryOption;
        this.availability = availability;
        this.modeOfPayment = modeOfPayment;
    }
    
    
    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listingId != null ? listingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the listingId fields are not set
        if (!(object instanceof Listing)) {
            return false;
        }
        Listing other = (Listing) object;
        if ((this.listingId == null && other.listingId != null) || (this.listingId != null && !this.listingId.equals(other.listingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Listing[ id=" + listingId + " ]";
    }

    public String getListingName() {
        return listingName;
    }

    public void setListingName(String listingName) {
        this.listingName = listingName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(Date dateOfPost) {
        this.dateOfPost = dateOfPost;
    }

    public Integer getMinRentalDuration() {
        return minRentalDuration;
    }

    public void setMinRentalDuration(Integer minRentalDuration) {
        this.minRentalDuration = minRentalDuration;
    }

    public Integer getMaxRentalDuration() {
        return maxRentalDuration;
    }

    public void setMaxRentalDuration(Integer maxRentalDuration) {
        this.maxRentalDuration = maxRentalDuration;
    }

    public Double getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(Double itemCondition) {
        this.itemCondition = itemCondition;
    }

    public DeliveryOptionEnum getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOptionEnum deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public AvailabilityEnum getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityEnum availability) {
        this.availability = availability;
    }

    public ModeOfPaymentEnum getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPaymentEnum modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }
    
}

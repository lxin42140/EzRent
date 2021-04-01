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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
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
public class ListingEntity implements Serializable, Comparable<ListingEntity> {

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
    @Positive
    private Double price;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DeliveryOptionEnum deliveryOption;

    @NotNull
    @Column(nullable = false)
    private String filePathName;

    private String location;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateOfPost;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer minRentalDuration;

    @Column(nullable = false)
    @NotNull
    @Positive
    private Integer maxRentalDuration;

    @Column(nullable = false)
    @NotNull
    @Positive
    @Max(10)
    private Integer itemCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private ModeOfPaymentEnum modeOfPayment;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "categoryId")
    @NotNull
    private CategoryEntity category;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<TagEntity> tags;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private AvailabilityEnum availability;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<OfferEntity> offers;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<CommentEntity> comments;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "customerId")
    @NotNull
    private CustomerEntity listingOwner;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<CustomerEntity> likedCustomers;

    @Column(nullable = false)
    @NotNull
    private boolean isDeleted;

    public ListingEntity() {
        this.tags = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.offers = new ArrayList<>();
        this.likedCustomers = new ArrayList<>();
        this.availability = AvailabilityEnum.AVAILABLE;
        this.isDeleted = false;
    }

    public ListingEntity(String listingName, Double price, String description, DeliveryOptionEnum deliveryOption, String filePathName, String location, Date dateOfPost, Integer minRentalDuration, Integer maxRentalDuration, Integer itemCondition, ModeOfPaymentEnum modeOfPayment, CategoryEntity category, List<TagEntity> tags, CustomerEntity listingOwner) {
        this();
        this.listingName = listingName;
        this.price = price;
        this.description = description;
        this.deliveryOption = deliveryOption;
        this.filePathName = filePathName;
        this.location = location;
        this.dateOfPost = dateOfPost;
        this.minRentalDuration = minRentalDuration;
        this.maxRentalDuration = maxRentalDuration;
        this.itemCondition = itemCondition;
        this.modeOfPayment = modeOfPayment;
        this.category = category;
        this.tags = tags;
        this.listingOwner = listingOwner;
    }

    public CustomerEntity getListingOwner() {
        return listingOwner;
    }

    public void setListingOwner(CustomerEntity listingOwner) {
        this.listingOwner = listingOwner;
    }

    public Long getListingId() {
        return listingId;
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
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

    public Integer getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(Integer itemCondition) {
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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<CustomerEntity> getLikedCustomers() {
        return likedCustomers;
    }

    public void setLikedCustomers(List<CustomerEntity> likedCustomers) {
        this.likedCustomers = likedCustomers;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
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
        if (!(object instanceof ListingEntity)) {
            return false;
        }
        ListingEntity other = (ListingEntity) object;
        if ((this.listingId == null && other.listingId != null) || (this.listingId != null && !this.listingId.equals(other.listingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Listing[ id=" + listingId + " ]";
    }

    @Override
    //listing with more offers will be recommedned
    public int compareTo(ListingEntity o) {
        return this.getOffers().size() - o.getOffers().size();
    }
}

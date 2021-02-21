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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class CustomerEntity extends UserEntity implements Serializable {

    @Column(nullable = false, length = 128)
    @NotNull
    @Size(max = 128)
    private String streetName;

    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String postalCode;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateJoined;

    @Column(nullable = false)
    @NotNull
    private String bio;

    private Double averageRating;

    @OneToMany(mappedBy = "customer")
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "customer")
    private List<CreditCardEntity> creditCards;

    public CustomerEntity() {
    }

    public CustomerEntity(String streetName, String postalCode, Date dateJoined, String bio, Double averageRating, String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.dateJoined = dateJoined;
        this.bio = bio;
        this.averageRating = averageRating;
        this.reviews = new ArrayList<>();
        this.creditCards = new ArrayList<>();
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "CustomerEntity{id=" + this.userId + '}';
    }

}

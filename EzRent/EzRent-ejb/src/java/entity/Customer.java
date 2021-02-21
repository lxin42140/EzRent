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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class Customer extends UserEntity implements Serializable {
    
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
    @Column(nullable = false)
    @NotNull
    @DecimalMin("0.0")
    private Double averageRating;

    public Customer() {
    }
    
    
    public Customer(String streetName, String postalCode, Date dateJoined, String bio, Double averageRating, String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.dateJoined = dateJoined;
        this.bio = bio;
        this.averageRating = averageRating;
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
    
}

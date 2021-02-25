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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.UserAccessRightEnum;
import util.security.CryptographicHelper;

/**
 *
 * @author Li Xin
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
    @NamedQuery(name = "retrieveAllUndeletedUsers", query = "select u from UserEntity u where u.isDeleted = FALSE"),
    @NamedQuery(name = "retrieveAllDisabledUsers", query = "SELECT u from UserEntity u where u.isDisable = TRUE")
})
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ensure ID generation follows identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;

    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    protected String userName;

    @Column(length = 32, nullable = false)
    @NotNull
    @Size(min = 4, max = 32)
    protected String password;

    @Column(length = 32, nullable = false)
    @NotNull
    protected String salt;

    @Column(nullable = false, length = 128)
    @NotNull
    @Email
    protected String email;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    protected String firstName;

    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    protected String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    protected UserAccessRightEnum accessRight;

    protected boolean isDisable;

    protected boolean isDeleted;

    // Ensure to include default constructor
    // Init collections here
    public UserEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public UserEntity(String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        this();

        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRight = accessRight;
        this.isDisable = isDisable;
        this.isDeleted = isDeleted;
        this.setPassword(password);
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
    }

    public String getSalt() {
        return salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserAccessRightEnum getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(UserAccessRightEnum accessRight) {
        this.accessRight = accessRight;
    }

    public boolean isIsDisable() {
        return isDisable;
    }

    public void setIsDisable(boolean isDisable) {
        this.isDisable = isDisable;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the userId fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UserEntity[ id=" + userId + " ]";
    }

}

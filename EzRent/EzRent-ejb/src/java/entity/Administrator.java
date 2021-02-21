/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Yuxin
 */
@Entity
public class Administrator extends UserEntity implements Serializable {

    public Administrator() {
    }

    
    public Administrator(String userName, String email, String firstName, String lastName, UserAccessRightEnum accessRight, boolean isDisable, boolean isDeleted, String password) {
        super(userName, email, firstName, lastName, accessRight, isDisable, isDeleted, password);
    }

    
}

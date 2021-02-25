/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.UserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteUserException;
import util.exception.UpdateUserFailedException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Li Xin
 */
@Local
public interface UserEntitySessionBeanLocal {

    public Long updateUserUsername(Long userId, String newUsername) throws UserNotFoundException, UpdateUserFailedException;

    public Long updateUserPassword(Long userId, String newPassword) throws UserNotFoundException, UpdateUserFailedException;

    public Long deleteUserById(Long userID) throws UserNotFoundException, DeleteUserException;

    public Long toggleUserAccountDisable(Long userID, Boolean disableUser) throws UserNotFoundException;

    public List<UserEntity> retrieveAllUsers();

    public List<UserEntity> retrieveAllDisabledUsers();

}

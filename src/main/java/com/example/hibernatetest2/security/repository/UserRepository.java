package com.example.hibernatetest2.security.repository;

import org.springframework.web.multipart.MultipartFile;

import com.example.hibernatetest2.security.dto.UpdateForm;
import com.example.hibernatetest2.security.dto.UserDTO;
import com.example.hibernatetest2.security.entities.User;

import java.util.Collection;

/**
 * It is like a manager that gets missions from UserServiceImpl
 * and then delegate them to UserRepositoryImpl
 */
public interface UserRepository<T extends User> {



    /**
     * Creates a new user:
     * <ol>
     * <li>Checks whether the row with given email is not already exist in the database
     * <li>Creates a new row in the [users] table in the database by passing generated id and other variables using JSQL-query
     * <li>Changes created row by adding user's roles into it
     * <li>Generates a random verification code for user's registration
     * <li>Creates a new row in [accountverifications] table in the database
     * <li>Sends verification URL to user's email address
     * <li>Makes user's account an enabled one
     */
    T create(T data);



    Collection<T> list(int page, int pageSize);



    /**
     * Gets an existing row from [users] table in the database using given user's id
     */
    T get(Long id);



    T update(T data);



    Boolean delete(Long id);



    /**
     * Gets User from [users] table in the database
     */
    User getUserByEmail(String email);



    void sendVerificationCode(UserDTO user);



    /**
     * Verifies given code for 2-factor authentication:
     * <ol>
     * <li>Checks whether verification code for given user is no expired yet
     * <li>Checks whether verification code is legit
     * <li>Deletes verification code from [code] column in [TwoFactorVerifications] table for given user
     * <li>Returns User
     */
    User verifyCode(String email, String code);



    /**
     * Asks UserRepositoryImpl to change a row in the [users] table based on the given email
     * <ol>
     * <li>Set expiration time for URL for changing a user's password
     * <li>Generate a URL for changing the password
     * <li>Deletes previous row that represents URL for changing a password from [resetpasswordverifications] table for given user
     * <li>Create a new row that represents URL for changing a password in [resetpasswordverifications] table for given user
     * <li>Send an email with a URL to reset user's password to user's email
     */
    void resetPassword(String email);



    /**
     * Asks UserReopsitoryImpl to:
     * <ol>
     * <li>Checks whether given URL for changing a password was expired
     * <li>Return User
     */
    T verifyPasswordKey(String key);



    void renewPassword(String key, String password, String confirmPassword);



    /**
     * Asks UserRepositoryImol to:
     * <ol>
     * <li>Check that confirm password equals to original one
     * <li>Change a row in [users] table by changing [password] column value
     */
    void renewPassword(Long userId, String password, String confirmPassword);



    /**
     * Asks UserRepositoryImpl to:
     * <ol>
     * <li>Change row in [user] table by set [enabled] column to true
     * <li>Return updated User
     */
    T verifyAccountKey(String key);



    T updateUserDetails(UpdateForm user);



    /**
     * Asks UserRepositoryImpl to:
     * <ol>
     * <li>Check that new password and duplicate of it is equal
     * <li>Check that old password, provided by user, is equal to password received from database
     * <li>Change row in the [users] table by saving new password to database
     */
    void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);



    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);



    User toggleMfa(String email);



    void updateImage(UserDTO user, MultipartFile image);

}
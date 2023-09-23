package com.example.hibernatetest2.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.hibernatetest2.security.dto.UpdateForm;
import com.example.hibernatetest2.security.dto.UserDTO;
import com.example.hibernatetest2.security.entities.Role;
import com.example.hibernatetest2.security.entities.User;
import com.example.hibernatetest2.security.mapper.UserMapper;
import com.example.hibernatetest2.security.repository.RoleRepository;
import com.example.hibernatetest2.security.repository.UserRepository;

/**
 * <p>Implementation of UserService.
 * <p>It is like a manager that gets missions from Userservice java class instance
 * and then tells others what they need to do to accomplish them
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    private final RoleRepository<Role> roleRoleRepository;



    /**
     * <ol>
     * <li>Asks UserRepostiroty to:
     * <ol>
     * <li>add a new row into [users] table using given User java class instance,
     * <li>return created User back
     * <li>send verification URL to user's email
     * </li>
     * </ol>
     * </li>
     * <li>Asks UserDTOMapper to create a UserDTO java class instance from returned User java class isntance
     * <li>Returns UserDTO java class instance
     */
    @Override
    public UserDTO createUser(User user) {
        return mapToUserDTO(userRepository.create(user));
    }

    /**
     * <ol>
     * <li>Asks UserRepostiroty to get an existed row from [users] table using given user's email and return created User back
     * <li>Asks UserDTOMapper to create a UserDTO java class instance from returned User java class isntance
     * <li>Returns UserDTO java class instance

     */
    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    /**
     * Asks UserRepoisitory to:
     * <ol>
     * <li>Set expiration date for verification code to 24 hours
     * <li>Generate random 8-letters verification code
     * <li>Delete old verification code if it exists from [TwoFactorVerifications] table in the database
     * <li>Inserts new verification code into [TwoFactorVerifications] table in the database
     * <li>Send verification code to user's phone as an SMS
     */
    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode(user);
    }

    /**
     * <ol>
     * <li>
     * Asks UserRepostiroty to add a new verifying code into [code] column in [twofactorverifictaions] table in the database,
     * delete an old one
     * and return User java class instance
     * <li>Calls this.mapToUserDTO () function to create a UserDTO java class instance from returned User java class instance
     * <li>Returns UserDTO java class instance
     */
    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    /**
     * Asks UserRepository to change a row in the [users] table based on the given email
     * <ol>
     * <li>Set expiration time for URL for changing a user's password
     * <li>Generate a URL for changing the password
     * <li>Deletes previous row that represents URL for changing a password from [resetpasswordverifications] table for given user
     * <li>Create a new row that represents URL for changing a password in [resetpasswordverifications] table for given user
     * <li>Send an email with a URL to reset user's password to user's email
     */
    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    /**
     * <ol>
     * <li>Asks UserRepository to:
     * <ol>
     * <li>Checks whether URL for changing a password was expired and
     * <li>Return User
     * </ol>
     * <li>Asks MapUserDTO to return UserDTO based on given User
     */
    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    /**
     * Asks UserRepository to:
     * <ol>
     * <li>Check that confirm password equals to original one
     * <li>Change a row in [users] table by changing [password] column value
     */
    @Override
    public void updatePassword(Long userId, String password, String confirmPassword) {
        userRepository.renewPassword(userId, password, confirmPassword);
    }

    /**
     * <ol>
     * <li>Asks UserRepository to:
     * <ol>
     * <li>Change row in [user] table by set [enabled] column to true
     * <li>Return updated User
     * </li>
     * </ol>
     * <li>Calls this.mapToUserDTO () to return UserDTO java class instance with user's roles
     */
    @Override
    public UserDTO verifyAccountKey(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }

    @Override
    public UserDTO updateUserDetails(UpdateForm user) {
        return mapToUserDTO(userRepository.updateUserDetails(user));
    }

    /**
     * <ol>
     * <li>
     * Asks UserRepostiroty to get an existed row from [users] table using given user's id and return created User back
     * <li>
     * Asks UserDTOMapper to create a UserDTO java class instance from returned User java class isntance
     * <li>
     * Returns UserDTO java class instance
     */
    @Override
    public UserDTO getUserById(Long userId) {
        return mapToUserDTO(userRepository.get(userId));
    }

    /**
     * Asks UserRepository to:
     * <ol>
     * <li>Check that new password and duplicate of it is equal
     * <li>Check that old password, provided by user, is equal to password received from database
     * <li>Change row in the [users] table by saving new password to database
     * </ol>
     */
    @Override
    public void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        userRepository.updatePassword(id, currentPassword, newPassword, confirmNewPassword);
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        roleRoleRepository.updateUserRole(userId, roleName);
    }

    @Override
    public void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked) {
        userRepository.updateAccountSettings(userId, enabled, notLocked);
    }

    @Override
    public UserDTO toggleMfa(String email) {
        return mapToUserDTO(userRepository.toggleMfa(email));
    }

    @Override
    public void updateImage(UserDTO user, MultipartFile image) {
        userRepository.updateImage(user, image);
    }

    /**
     * <ol>
     * <li>Asks RoleRepository to get user's roles from [roles] table in the database using id of given User java class instance
     * <li>Asks UserDtoMapper to create a UserDTO java class instance from returned User java class instance
     * <li>Returns UserDTO java class instance
     */
    private UserDTO mapToUserDTO(User user) {
        return UserMapper.fromUserWithRole(user, roleRoleRepository.getRoleByUserId(user.getId()));
    }
}
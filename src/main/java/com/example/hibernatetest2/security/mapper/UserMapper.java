package com.example.hibernatetest2.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;

import com.example.hibernatetest2.security.dto.UserDTO;
import com.example.hibernatetest2.security.entities.Role;
import com.example.hibernatetest2.security.entities.User;

@Mapper(componentModel = "spring" //? For Spring's Dependency Injection to work
//uses = Table2Mapper.class) //? Allows you to use another mapper inside this one
)
public abstract class UserMapper {
    
    /**
     * Creates a UserDTO based on given User
     */
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO); // Automatically maps variables and their values from [User] to [UserDTO]
        return userDTO;
    }

    /**
     * Creates a UserDTO based on given User and its given roles
     */
    public static UserDTO fromUserWithRole(User user, Role role) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName(role.getName());
        userDTO.setPermissions(role.getPermission());
        return userDTO;
    }

    /**
     * Creates a User based on given UserDTO
     */
    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user); // Automatically maps variables and their values from [UserDTO] to [User]
        return user;
    }

}

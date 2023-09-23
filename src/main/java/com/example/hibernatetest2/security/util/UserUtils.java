package com.example.hibernatetest2.security.util;


import org.springframework.security.core.Authentication;

import com.example.hibernatetest2.security.dto.UserDTO;

/**
 * Java class that takes UserDTO from given Authentication
 */
public class UserUtils {

/**
 * Gets UserDTO from given authentication:
 * <ol>
 * <li>Asks given authentication to get UserPrincipal
 * <li>Asks UserPrincipal to get UserDTO
 */
public static UserDTO getAuthenticatedUser(Authentication authentication) {
    return ((UserDTO)authentication.getPrincipal());
}

/**
 * Gets UserDTO from given authentication:
 * <ol>
 * <li>Asks given authentication to get UserPrincipal
 * <li>Asks UserPrincipal to get UserDTO
 */
/*
public static UserDTO getLoggedInUser(Authentication authentication) {
    return ((UserPlusItsRoles)authentication.getPrincipal()).getUser();
}
*/
}

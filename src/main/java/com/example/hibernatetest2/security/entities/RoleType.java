package com.example.hibernatetest2.security.entities;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum that represents existing user's roles
 */
public enum RoleType { //? With that implementing, you can use values of this Enumerations in security checks

    ROLE_USER,

    ROLE_MANAGER,

    ROLE_ADMIN,

    ROLE_SYSADMIN;
}

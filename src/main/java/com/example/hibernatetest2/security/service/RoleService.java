package com.example.hibernatetest2.security.service;

import java.util.Collection;

import com.example.hibernatetest2.security.entities.Role;

public interface RoleService {

    Role getRoleByUserId(Long id);

    Collection<Role> getRoles();
}


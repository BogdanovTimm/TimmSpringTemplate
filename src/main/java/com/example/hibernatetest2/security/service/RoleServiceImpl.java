package com.example.hibernatetest2.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.hibernatetest2.security.entities.Role;
import com.example.hibernatetest2.security.repository.RoleRepository;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository<Role> roleRoleRepository;

    @Override
    public Role getRoleByUserId(Long id) {
        return roleRoleRepository.getRoleByUserId(id);
    }

    @Override
    public Collection<Role> getRoles() {
        return roleRoleRepository.list();
    }
}
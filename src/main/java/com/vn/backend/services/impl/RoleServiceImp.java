package com.vn.backend.services.impl;

import com.vn.backend.entities.Role;
import com.vn.backend.repositories.RoleRepository;
import com.vn.backend.services.RoleService;
import com.vn.backend.utils.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    @Override
    public Optional<Role> findByRoleName(ERole rolename) {
        return roleRepository.findByRolename(rolename);
    }

    @Autowired
    private RoleRepository roleRepository;

}

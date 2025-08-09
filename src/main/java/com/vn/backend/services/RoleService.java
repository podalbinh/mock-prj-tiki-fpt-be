package com.vn.backend.services;


import com.vn.backend.entities.Role;
import com.vn.backend.utils.enums.ERole;

import java.util.Optional;

public interface RoleService {

    public Optional<Role> findByRoleName(ERole rolename);

}

package com.vn.backend.repositories;

import com.vn.backend.entities.Role;
import com.vn.backend.utils.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRolename(ERole roleName);
}
package com.asistlab.imagemaker.repository;

import com.asistlab.imagemaker.model.enums.Permission;
import com.asistlab.imagemaker.model.enums.Role;
import com.asistlab.imagemaker.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleAndPermission(Role role, Permission permission);
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    Stream<RolePermission> findByPermission(Permission permission);
    Stream<RolePermission> findByRole(Role role);
}

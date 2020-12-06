package by.gstu.springsecurity.repository;

import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleAndPermission(Role role, Permission permission);
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
    Stream<RolePermission> findByPermission(Permission permission);
    Stream<RolePermission> findByRole(Role role);
}

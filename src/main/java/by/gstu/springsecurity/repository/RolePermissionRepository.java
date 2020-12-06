package by.gstu.springsecurity.repository;

import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.RoleType;
import by.gstu.springsecurity.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    Stream<RolePermission> findByPermission(Permission permission);
    Stream<RolePermission> findByRole(RoleType role);
}

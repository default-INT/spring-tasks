package by.gstu.springsecurity.service;

import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.RoleType;
import by.gstu.springsecurity.model.RolePermission;
import by.gstu.springsecurity.repository.RolePermissionRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Set<RolePermission> getPermissions(RoleType role) {
        return rolePermissionRepository.findByRole(role)
                .collect(Collectors.toSet());
    }

    public void addPermission(RoleType role, Permission permission) {
        rolePermissionRepository.save(new RolePermission(role, permission));

    }

    public Set<RolePermission> getRoles(Permission permission) {
        return rolePermissionRepository.findByPermission(permission)
                .collect(Collectors.toSet());
    }
}

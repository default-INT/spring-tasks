package by.gstu.springsecurity.service;

import by.gstu.springsecurity.exception.IllegalInsertEntityExistException;
import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.model.RolePermission;
import by.gstu.springsecurity.repository.RolePermissionRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Set<RolePermission> getPermissions(Role role) {
        return rolePermissionRepository.findByRole(role)
                .collect(Collectors.toSet());
    }
    public Set<GrantedAuthority> getAuthorities(Role role) {
        return rolePermissionRepository.findByRole(role)
                .map(RolePermission::getPermission)
                .map(permission ->  new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    public void addPermission(Role role, Permission permission) {
        if (rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
            throw new IllegalInsertEntityExistException(String.format("%s permission exist by %s", permission.name(), role.name()));
        }
        rolePermissionRepository.save(new RolePermission(role, permission));

    }

    public void deletePermission(Role role, Permission permission) {
        RolePermission rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission)
                .orElseThrow(EntityNotFoundException::new);
        rolePermissionRepository.delete(rolePermission);
    }

    public Set<RolePermission> getRoles(Permission permission) {
        return rolePermissionRepository.findByPermission(permission)
                .collect(Collectors.toSet());
    }
}

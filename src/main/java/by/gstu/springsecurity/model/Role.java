package by.gstu.springsecurity.model;

import by.gstu.springsecurity.service.RolePermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class Role {
    private RoleType roleType;
    private Set<Permission> permissions;


    private final RolePermissionService rolePermissionService;

    public Role(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}

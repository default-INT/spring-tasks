package by.gstu.springsecurity.model;

import by.gstu.springsecurity.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


public enum RoleType {
    ADMIN,
    USER,
    GUEST;

    private Set<Permission> permissions;

    @Autowired
    private RolePermissionService rolePermissionService;



}

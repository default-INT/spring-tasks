package by.gstu.springsecurity.model;

import by.gstu.springsecurity.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public enum Permission {
    DEFAULT, READ_ONLY, WRITE, COMMENT;

    private Set<Permission> permissions;

    private RolePermissionService rolePermissionService;

}
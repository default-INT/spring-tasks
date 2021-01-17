package com.asistlab.imagemaker.model;

import com.asistlab.imagemaker.model.enums.Permission;
import com.asistlab.imagemaker.model.enums.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles_permissions")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Permission permission;
    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public RolePermission() {
    }

    public RolePermission(Role role, Permission permission) {
        this.role = role;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePermission that = (RolePermission) o;
        return Objects.equals(id, that.id) &&
                permission == that.permission &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permission, role);
    }
}

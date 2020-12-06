package by.gstu.springsecurity.model;

import javax.persistence.*;

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
    private RoleType role;

    public RolePermission() {
    }

    public RolePermission(RoleType role, Permission permission) {
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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}

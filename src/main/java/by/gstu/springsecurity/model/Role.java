package by.gstu.springsecurity.model;

import java.util.Set;

public enum Role {
    ADMIN(Set.of()),
    USER(Set.of(Permission.values())),
    GUEST(Set.of());

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}

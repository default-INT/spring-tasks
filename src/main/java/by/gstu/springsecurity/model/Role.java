package by.gstu.springsecurity.model;

import java.util.Set;

public enum Role {
    ADMIN(Set.of(Permission.values())),
    USER(Set.of(Permission.READ_ONLY)),
    GUEST(Set.of());
    
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}

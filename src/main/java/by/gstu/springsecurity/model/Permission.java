package by.gstu.springsecurity.model;

import org.springframework.security.core.GrantedAuthority;

public enum  Permission implements GrantedAuthority {
    READ_ONLY,
    WRITE,
    COMMENTS,
    DEFAULT;

    @Override
    public String getAuthority() {
        return name();
    }
}
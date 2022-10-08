package ghorned.timecapture.utility;

import org.springframework.security.core.GrantedAuthority;

public enum AccessAuthority implements GrantedAuthority {

    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
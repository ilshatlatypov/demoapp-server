package ru.jvdev.demoapp.server.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.09.2016
 */
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private int id;

    public CustomUserDetails(int id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

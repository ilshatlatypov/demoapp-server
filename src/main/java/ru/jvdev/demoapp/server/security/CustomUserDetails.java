package ru.jvdev.demoapp.server.security;

import ru.jvdev.demoapp.server.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 27.07.2016
 */
public class CustomUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    public CustomUserDetails(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //String roles = getRoles().stream().map(Role::getName).collect(Collectors.joining(","));
        String roles = getRole().name();
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

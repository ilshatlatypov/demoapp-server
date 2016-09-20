package ru.jvdev.demoapp.server.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Role;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.09.2016
 */
@Component
public class AccessRules {

    public boolean ifManagerOrUserSearchesHimself(Authentication authentication, HttpServletRequest request) {
        String authenticatedUsername = authentication.getName();
        String paramUsername = request.getParameter(USERNAME);
        return isManager(authentication) || authenticatedUsername.equals(paramUsername);
    }

    public boolean ifManagerOrUserSearchesHisTasks(Authentication authentication, HttpServletRequest request) {
        String authenticatedUsername = authentication.getName();
        String paramUsername = request.getParameter(USERNAME);
        return isManager(authentication) || authenticatedUsername.equals(paramUsername);
    }

    private static boolean isManager(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> Role.MANAGER.name().equals(authority));
    }
}

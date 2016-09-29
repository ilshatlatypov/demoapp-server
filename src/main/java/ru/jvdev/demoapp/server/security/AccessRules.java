package ru.jvdev.demoapp.server.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.repository.TaskRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;
import ru.jvdev.demoapp.server.utils.URIUtils;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.09.2016
 */
@Component
public class AccessRules {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

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

    public boolean ifManagerOrTaskOwner(Authentication authentication, HttpServletRequest request) {
        if (isManager(authentication)) {
            return true;
        }

        String authenticatedUsername = authentication.getName();
        int taskId = URIUtils.getIdFromURI(request.getRequestURI());
        Task task = taskRepository.findOne(taskId);
        return task != null && task.getUser() != null && task.getUser().getUsername().equals(authenticatedUsername);
    }

    public boolean ifUserPatchesHimself(Authentication authentication, HttpServletRequest request) {
        int authenticatedUserId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        int userId = URIUtils.getIdFromURI(request.getRequestURI());
        return authenticatedUserId == userId;
    }

    private static boolean isManager(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> Role.MANAGER.name().equals(authority));
    }
}

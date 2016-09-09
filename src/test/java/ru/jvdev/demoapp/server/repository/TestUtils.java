package ru.jvdev.demoapp.server.repository;

import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.User;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 09.09.2016
 */
public class TestUtils {

    private TestUtils() {}

    public static User buildUser(String username, String password, Role role) {
        return new User("Any firstname", "Any lastname", username, password, role);
    }
}

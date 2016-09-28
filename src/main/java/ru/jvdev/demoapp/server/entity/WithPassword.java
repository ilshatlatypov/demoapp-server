package ru.jvdev.demoapp.server.entity;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 28.09.2016
 */
@Projection(name = "withPassword", types = { User.class })
public interface WithPassword {
    int getId();
    String getFirstname();
    String getLastname();
    String getUsername();
    Role getRole();
    String getPassword();
}

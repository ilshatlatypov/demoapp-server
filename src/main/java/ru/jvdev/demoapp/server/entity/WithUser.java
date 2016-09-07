package ru.jvdev.demoapp.server.entity;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 07.09.2016
 */
@Projection(name = "withUser", types = { Task.class })
public interface WithUser {
    String getTitle();
    Date getDate();
    User getUser();
}

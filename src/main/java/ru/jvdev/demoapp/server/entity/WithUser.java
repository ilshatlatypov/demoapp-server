package ru.jvdev.demoapp.server.entity;

import java.time.LocalDate;

import org.springframework.data.rest.core.config.Projection;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 07.09.2016
 */
@Projection(name = "withUser", types = { Task.class })
public interface WithUser {
    int getId();
    String getTitle();
    LocalDate getDate();
    boolean getDone();
    User getUser();
}

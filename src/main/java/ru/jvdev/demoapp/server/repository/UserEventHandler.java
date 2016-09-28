package ru.jvdev.demoapp.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.stereotype.Component;

import ru.jvdev.demoapp.server.entity.User;

/**
 * Special logic for user password. Performed before validation.
 *
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 28.09.2016
 */
@Component
class UserEventHandler implements ApplicationListener<BeforeCreateEvent>, Ordered {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(BeforeCreateEvent event) {
        User user = (User) event.getSource();
        user.setPassword(user.getUsername());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

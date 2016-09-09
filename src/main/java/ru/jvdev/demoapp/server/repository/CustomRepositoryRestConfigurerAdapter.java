package ru.jvdev.demoapp.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

import ru.jvdev.demoapp.server.validation.TaskValidator;
import ru.jvdev.demoapp.server.validation.UserValidator;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.08.2016
 */
@Configuration
public class CustomRepositoryRestConfigurerAdapter extends RepositoryRestConfigurerAdapter {

    @Autowired
    @Qualifier("mvcValidator")
    Validator validator; // for annotation based validation
    @Autowired
    UserValidator userValidator;
    @Autowired
    TaskValidator taskValidator;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(User.class, Task.class);
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);

        validatingListener.addValidator("beforeCreate", userValidator);
        validatingListener.addValidator("beforeSave", userValidator);

        validatingListener.addValidator("beforeCreate", taskValidator);
        validatingListener.addValidator("beforeSave", taskValidator);
    }
}

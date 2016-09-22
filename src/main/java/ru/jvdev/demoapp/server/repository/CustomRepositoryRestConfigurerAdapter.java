package ru.jvdev.demoapp.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.validation.TaskValidator;
import ru.jvdev.demoapp.server.validation.UserValidator;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.08.2016
 */
@Configuration
public class CustomRepositoryRestConfigurerAdapter extends RepositoryRestConfigurerAdapter {

    private static final String BEFORE_CREATE = "beforeCreate";
    private static final String BEFORE_SAVE = "beforeSave";

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
        validatingListener.addValidator(BEFORE_CREATE, validator);
        validatingListener.addValidator(BEFORE_SAVE, validator);

        validatingListener.addValidator(BEFORE_CREATE, userValidator);
        validatingListener.addValidator(BEFORE_SAVE, userValidator);

        validatingListener.addValidator(BEFORE_CREATE, taskValidator);
        validatingListener.addValidator(BEFORE_SAVE, taskValidator);
    }
}

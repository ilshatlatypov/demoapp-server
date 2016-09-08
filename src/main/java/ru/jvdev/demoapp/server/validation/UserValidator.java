package ru.jvdev.demoapp.server.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.repository.UserRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.09.2016
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getId() != user.getId()) {
            errors.rejectValue("username", "unique");
        }
    }
}

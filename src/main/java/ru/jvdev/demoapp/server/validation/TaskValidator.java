package ru.jvdev.demoapp.server.validation;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.repository.TaskRepository;
import ru.jvdev.demoapp.server.utils.PropertyNames;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 09.09.2016
 */
@Component
public class TaskValidator implements Validator {

    @Autowired
    EntityManager entityManager;
    @Autowired
    TaskRepository taskRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task t = (Task) target;
        int id = t.getId();
        LocalDate date = t.getDate();
        if (date == null) {
            return;
        }

        LocalDate today = LocalDate.now();

        boolean rejectDate;
        if (id == 0) {
            rejectDate = date.isBefore(today);
        } else {
            entityManager.detach(t);
            LocalDate existingDate = taskRepository.findOne(id).getDate();
            rejectDate = !date.equals(existingDate) && date.isBefore(today);
        }

        if (rejectDate) {
            errors.rejectValue(PropertyNames.DATE, "notpast");
        }
    }
}

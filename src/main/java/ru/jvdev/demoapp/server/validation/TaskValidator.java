package ru.jvdev.demoapp.server.validation;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.repository.TaskRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 09.09.2016
 */
@Component
public class TaskValidator implements Validator {

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

        LocalDate today = LocalDate.now();

        if (id == 0) {
            if (date.isBefore(today)) {
                errors.rejectValue("date", "notpast");
            }
        } else {
            LocalDate existingDate = taskRepository.findOne(id).getDate();
            if (!date.equals(existingDate) && date.isBefore(today)) {
                errors.rejectValue("date", "notpast");
            }
        }
    }
}

package ru.jvdev.demoapp.server.repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 31.08.2016
 */
@RepositoryRestController
public class UserController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(method = PUT, path = "/users/{id}/tasks", consumes = "text/uri-list")
    public @ResponseBody ResponseEntity<?> assignTasksToUser(@PathVariable("id") int userId, @RequestBody ResourceSupport taskURIs) {
        Set<Integer> taskIds = taskURIs.getLinks().stream()
            .map(Link::getHref)
            .map(UserController::getIdFromURI)
            .collect(Collectors.toSet());

        User user = new User();
        user.setId(userId);

        List<Task> tasks = taskRepository.findAll(taskIds);
        tasks.forEach(t -> t.setUser(user));
        taskRepository.save(tasks);

        // taskRepository.setUserIdForTasks(user, taskIds);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = DELETE, path = "/users/{userId}/tasks/{taskId}")
    public @ResponseBody ResponseEntity<?> unassignTaskFromUser(@PathVariable("userId") int userId, @PathVariable("taskId") int taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task != null && task.getUser() != null && task.getUser().getId() == taskId) {
            task.setUser(null);
            taskRepository.save(task);
        }
        return ResponseEntity.noContent().build();
    }

    private static int getIdFromURI(String uri) {
        int lastSlashIndex = uri.lastIndexOf('/');
        return Integer.parseInt(uri.substring(lastSlashIndex + 1));
    }

}

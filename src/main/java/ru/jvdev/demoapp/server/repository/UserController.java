package ru.jvdev.demoapp.server.repository;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.utils.MediaTypes;
import ru.jvdev.demoapp.server.utils.URIUtils;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 31.08.2016
 */
@RepositoryRestController
public class UserController {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = PUT, path = "/users/{userId}/tasks", consumes = MediaTypes.URI_LIST)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> assignTasksToUser(@PathVariable int userId,
                                               @RequestBody ResourceSupport taskURIs) {
        Set<Integer> taskIds = taskURIs.getLinks().stream()
            .map(Link::getHref)
            .map(URIUtils::getIdFromURI)
            .collect(Collectors.toSet());
        taskRepository.setUserIdForTasks(userId, taskIds);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = DELETE, path = "/users/{userId}/tasks/{taskId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> unassignTaskFromUser(@PathVariable int userId,
                                                  @PathVariable int taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task != null && task.getUser() != null && task.getUser().getId() == userId) {
            task.setUser(null);
            taskRepository.save(task);
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/users/{userId}/setPassword", method = POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> setPassword(@PathVariable int userId, @RequestBody String newPassword) {
        // TODO validate new password: not blank, min length 3

        User user = userRepository.findOne(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(newPassword)) {
            userRepository.setPassword(userId, newPassword);
        }
        return ResponseEntity.noContent().build();
    }
}

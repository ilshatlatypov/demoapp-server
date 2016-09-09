package ru.jvdev.demoapp.server.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.jvdev.demoapp.server.Application;
import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.entity.User;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 07.09.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AssignTasksToUserTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    private List<User> users = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MINUTE, 5);

        taskRepository.deleteAllInBatch();
        tasks.add(taskRepository.save(new Task("Launch rocket", cal.getTime())));

        userRepository.deleteAllInBatch();
        users.add(userRepository.save(new User("Elon", "Musk", "emusk", "1234", Role.MANAGER)));
        users.add(userRepository.save(new User("Stephen", "Hawking", "shawking", "1234", Role.EMPLOYEE)));
    }

    @Test
    public void testManagerCanAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/" + users.get(0).getId() + "/tasks")
            .contentType("text/uri-list")
            .content("http://localhost:8080/tasks/" + tasks.get(0).getId())
            .with(httpBasic("emusk", "1234")))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testManagerCanUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/1/tasks/1")
            .with(httpBasic("emusk", "1234")))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testEmployeeCannotAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/" + users.get(0).getId() + "/tasks")
            .contentType("text/uri-list")
            .content("http://localhost:8080/tasks/" + tasks.get(0).getId())
            .with(httpBasic("shawking", "1234")))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testEmployeeCannotUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/" + users.get(0).getId() + "/tasks/1")
            .with(httpBasic("shawking", "1234")))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testManagerCanAssignUserToTask() throws Exception {
        mockMvc.perform(put("/tasks/" + tasks.get(0).getId() + "/user")
            .contentType("text/uri-list")
            .content("http://localhost:8080/users/" + users.get(0).getId())
            .with(httpBasic("emusk", "1234")))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testEmployeeCanNotAssignUserToTask() throws Exception {
        mockMvc.perform(put("/tasks/" + tasks.get(0).getId() + "/user")
            .contentType("text/uri-list")
            .content("http://localhost:8080/users/" + users.get(0).getId())
            .with(httpBasic("shawking", "1234")))
            .andExpect(status().isForbidden());
    }
}

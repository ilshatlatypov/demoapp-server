package ru.jvdev.demoapp.server.repository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

import static ru.jvdev.demoapp.server.repository.TestUtils.buildUser;
import static ru.jvdev.demoapp.server.utils.URIUtils.buildURIFromId;
import ru.jvdev.demoapp.server.Application;
import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.entity.Task;
import ru.jvdev.demoapp.server.utils.MediaTypes;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 07.09.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AssignTasksToUserTest {

    private static final String MANAGER_USERNAME = "managerUser";
    private static final String EMPLOYEE_USERNAME = "employeeUser";
    private static final String PASSWORD = "anypassword";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    private int taskId;
    private int userId;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MINUTE, 5);

        taskRepository.deleteAllInBatch();
        taskId = taskRepository.save(new Task("Launch rocket", LocalDate.now())).getId();

        userRepository.deleteAllInBatch();
        userRepository.save(buildUser(MANAGER_USERNAME, PASSWORD, Role.MANAGER));
        userId = userRepository.save(buildUser(EMPLOYEE_USERNAME, PASSWORD, Role.EMPLOYEE)).getId();
    }

    @Test
    public void testManagerCanAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/" + userId + "/tasks")
            .contentType(MediaTypes.URI_LIST)
            .content(buildURIFromId(taskId))
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testManagerCanUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/" + userId + "/tasks/" + taskId)
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testEmployeeCannotAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/" + userId + "/tasks")
            .contentType(MediaTypes.URI_LIST)
            .content(buildURIFromId(taskId))
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testEmployeeCannotUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/" + userId + "/tasks/" + taskId)
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testManagerCanAssignUserToTask() throws Exception {
        mockMvc.perform(put("/tasks/" + taskId + "/user")
            .contentType(MediaTypes.URI_LIST)
            .content(buildURIFromId(taskId))
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testEmployeeCannotAssignUserToTask() throws Exception {
        mockMvc.perform(put("/tasks/" + taskId + "/user")
            .contentType(MediaTypes.URI_LIST)
            .content(buildURIFromId(userId))
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isForbidden());
    }
}

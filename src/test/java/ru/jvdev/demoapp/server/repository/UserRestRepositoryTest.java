package ru.jvdev.demoapp.server.repository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ru.jvdev.demoapp.server.repository.TestUtils.buildUser;
import ru.jvdev.demoapp.server.entity.Role;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 01.09.2016
 */
public class UserRestRepositoryTest extends AbstractSpringTest {

    private static final String MANAGER_USERNAME = "managerUser";
    private static final String EMPLOYEE_USERNAME = "employeeUser";
    private static final String PASSWORD = "anypassword";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        initMockMvcWithSpringSecurity();

        this.userRepository.deleteAllInBatch();
        this.userRepository.save(buildUser(MANAGER_USERNAME, PASSWORD, Role.MANAGER)).getId();
        this.userRepository.save(buildUser(EMPLOYEE_USERNAME, PASSWORD, Role.EMPLOYEE)).getId();
    }

    @Test
    public void testRootPathDoesNotRequireAuthentication() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk());
    }

    @Test
    public void testGettingTasksRequiresAuthorization() throws Exception {
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    // @WithMockUser(authorities = "MANAGER")
    public void testManagerCanGetTasks() throws Exception {
        mockMvc.perform(get("/tasks")
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isOk());
    }

    @Test
    // @WithMockUser(authorities = "EMPLOYEE")
    public void testEmployeeCannotGetTasks() throws Exception {
        mockMvc.perform(get("/tasks")
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isForbidden());
    }
}

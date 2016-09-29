package ru.jvdev.demoapp.server.repository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ru.jvdev.demoapp.server.repository.TestUtils.buildUser;
import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.utils.Paths;

/**
 * Created by ilshat on 21.09.16.
 */
public class FindTasksByUsernameTest extends AbstractSpringTest {

    private static final String MANAGER_USERNAME = "managerUser";
    private static final String EMPLOYEE_USERNAME = "employeeUser";
    private static final String PASSWORD = "anypassword";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        initMockMvcWithSpringSecurity();

        this.userRepository.deleteAllInBatch();
        this.userRepository.save(buildUser(MANAGER_USERNAME, PASSWORD, Role.MANAGER));
        this.userRepository.save(buildUser(EMPLOYEE_USERNAME, PASSWORD, Role.EMPLOYEE));
    }

    @Test
    public void testManagerCanGetHisDataByUsername() throws Exception {
        mockMvc.perform(get(Paths.FIND_TASKS_BY_USERNAME)
            .param(USERNAME, MANAGER_USERNAME)
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isOk());
    }

    @Test
    public void testManagerCanGetOtherUserDataByUsername() throws Exception {
        mockMvc.perform(get(Paths.FIND_TASKS_BY_USERNAME)
            .param(USERNAME, EMPLOYEE_USERNAME)
            .with(httpBasic(MANAGER_USERNAME, PASSWORD)))
            .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeCanGetHisDataByUsername() throws Exception {
        mockMvc.perform(get(Paths.FIND_TASKS_BY_USERNAME)
            .param(USERNAME, EMPLOYEE_USERNAME)
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeCannotGetOtherUserDataByUsername() throws Exception {
        mockMvc.perform(get(Paths.FIND_TASKS_BY_USERNAME)
            .param(USERNAME, MANAGER_USERNAME)
            .with(httpBasic(EMPLOYEE_USERNAME, PASSWORD)))
            .andExpect(status().isForbidden());
    }
}

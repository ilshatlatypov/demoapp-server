package ru.jvdev.demoapp.server.repository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Ignore;
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
import static ru.jvdev.demoapp.server.utils.RequestParamNames.USERNAME;
import ru.jvdev.demoapp.server.Application;
import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.utils.Paths;

/**
 * Created by ilshat on 21.09.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
// TODO disable tests since they show unreliable results, check later
@Ignore
public class FindTasksByUsernameTest {

    private static final String MANAGER_USERNAME = "managerUser";
    private static final String EMPLOYEE_USERNAME = "employeeUser";
    private static final String PASSWORD = "anypassword";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();

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

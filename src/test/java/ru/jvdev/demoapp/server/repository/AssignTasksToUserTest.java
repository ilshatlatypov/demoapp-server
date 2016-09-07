package ru.jvdev.demoapp.server.repository;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.jvdev.demoapp.server.Application;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 07.09.2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AssignTasksToUserTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void testManagerCanAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/1/tasks")
            .contentType("text/uri-list")
            .content("http://localhost:8080/tasks/1")
            .with(httpBasic("mscott", "1234")))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testManagerCanUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/1/tasks/1")
            .with(httpBasic("mscott", "1234")))
            .andExpect(status().isNoContent());
    }

    @Test
    public void testEmployeeCannotAssignTaskToUser() throws Exception {
        mockMvc.perform(put("/users/1/tasks")
            .contentType("text/uri-list")
            .content("http://localhost:8080/tasks/1")
            .with(httpBasic("jbauer", "1234")))
            .andExpect(status().isForbidden());
    }

    @Test
    public void testEmployeeCannotUnassignTaskFromUser() throws Exception {
        mockMvc.perform(delete("/users/1/tasks/1")
            .with(httpBasic("jbauer", "1234")))
            .andExpect(status().isForbidden());
    }
}

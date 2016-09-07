package ru.jvdev.demoapp.server.repository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
@SpringApplicationConfiguration(classes = { Application.class, UserRestRepository.class } )
@WebAppConfiguration
public class GetUserByUsernameTest {

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
    public void testManagerCanGetHisDataByUsername() throws Exception {
        mockMvc.perform(get("/users/search/findByUsername")
            .param("username", "mscott")
            .with(httpBasic("mscott", "1234")))
            .andExpect(status().isOk());
    }

    @Test
    public void testManagerCanGetOtherUserDataByUsername() throws Exception {
        mockMvc.perform(get("/users/search/findByUsername")
            .param("username", "jbauer")
            .with(httpBasic("mscott", "1234")))
            .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeCanGetHisDataByUsername() throws Exception {
        mockMvc.perform(get("/users/search/findByUsername")
            .param("username", "jbauer")
            .with(httpBasic("jbauer", "1234")))
            .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeCanNotGetOtherUserDataByUsername() throws Exception {
        mockMvc.perform(get("/users/search/findByUsername")
            .param("username", "jconnor")
            .with(httpBasic("jbauer", "1234")))
            .andExpect(status().isForbidden());
    }
}

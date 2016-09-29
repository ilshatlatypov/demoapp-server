package ru.jvdev.demoapp.server.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static ru.jvdev.demoapp.server.repository.TestUtils.buildUser;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.09.2016
 */
public class SetUserPasswordTest extends AbstractSpringTest {

    @Autowired
    private UserRepository userRepository;

    private int aliceId;
    private int bobId;

    @Before
    public void setup() throws Exception {
        initMockMvcWithSpringSecurity();

        this.userRepository.deleteAllInBatch();
        aliceId = this.userRepository.save(buildUser("alice", "alice.password")).getId();
        bobId = this.userRepository.save(buildUser("bob", "bob.password")).getId();
    }

    @Test
    public void testUserCanChangeHisPassword() throws Exception {
        mockMvc.perform(post("/users/" + aliceId + "/setPassword")
            .contentType(MediaType.TEXT_PLAIN)
            .content("new.alice.password")
            .with(httpBasic("alice", "alice.password")))
            .andExpect(status().is2xxSuccessful());

        String actualPassword = userRepository.findOne(aliceId).getPassword();
        assertEquals("new.alice.password", actualPassword);
    }

    @Test
    public void testUserCanNotChangeOtherUsersPassword() throws Exception {
        mockMvc.perform(post("/users/" + bobId + "/setPassword")
            .contentType(MediaType.TEXT_PLAIN)
            .content("new.bob.password")
            .with(httpBasic("alice", "alice.password")))
            .andExpect(status().isForbidden());

        String actualPassword = userRepository.findOne(bobId).getPassword();
        assertEquals("bob.password", actualPassword);
    }
}

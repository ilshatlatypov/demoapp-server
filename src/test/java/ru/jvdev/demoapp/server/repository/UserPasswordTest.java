package ru.jvdev.demoapp.server.repository;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static ru.jvdev.demoapp.server.repository.TestUtils.buildUser;
import static ru.jvdev.demoapp.server.repository.TestUtils.buildUserWithNoPassword;
import ru.jvdev.demoapp.server.entity.User;
import ru.jvdev.demoapp.server.utils.URIUtils;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.09.2016
 */
public class UserPasswordTest extends AbstractSpringTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        initMockMvc();
    }

    @Test
    public void testPasswordTakesUsernameValueOnUserCreation() throws Exception {
        final String username = "mscott";

        User user = buildUserWithNoPassword(username);

        String userJson = json(user);
        MvcResult result = mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        int userId = getCreatedEntityId(result);
        User createdUser = userRepository.findOne(userId);
        assertEquals(username, createdUser.getPassword());
    }

    @Test
    public void testPasswordIsNotUpdatable() throws Exception {
        User user = buildUser();
        String oldPassword = user.getPassword();
        int userId = userRepository.save(user).getId();

        String newPassword = oldPassword + "modification";
        user.setPassword(newPassword);

        String userJson = json(user);
        mockMvc.perform(put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().is2xxSuccessful());

        User updatedUser = userRepository.findOne(userId);
        assertEquals(oldPassword, updatedUser.getPassword());
    }

    @Test
    public void testPasswordIsNotReturnedOnGetUser() throws Exception {
        int userId = userRepository.save(buildUser()).getId();

        mockMvc.perform(get("/users/" + userId))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    private static int getCreatedEntityId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        return URIUtils.getIdFromURI(location);
    }
}

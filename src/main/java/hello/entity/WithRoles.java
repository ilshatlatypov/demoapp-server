package hello.entity;

import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 29.07.2016
 */
@Projection(name = "withRoles", types = { User.class })
public interface WithRoles {
    String getFirstname();
    String getLastname();
    String getUsername();
    String getPassword();
    Set<Role> getRoles();
}

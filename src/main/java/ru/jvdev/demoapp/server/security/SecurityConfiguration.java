package ru.jvdev.demoapp.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import ru.jvdev.demoapp.server.entity.Role;
import ru.jvdev.demoapp.server.utils.Paths;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 27.07.2016
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/tasks/search/*")
                    .access("@accessRules.ifManagerOrUserSearchesHisTasks(authentication,request)")
                .antMatchers("/tasks/*")
                    .access("@accessRules.ifManagerOrTaskOwner(authentication,request)")
                .antMatchers("/tasks/**").hasAuthority(Role.MANAGER.name())
                .antMatchers(Paths.FIND_USER_BY_USERNAME)
                    .access("@accessRules.ifManagerOrUserSearchesHimself(authentication,request)")
                .antMatchers(HttpMethod.PATCH, "/users/*") // patch for changing password
                    .access("@accessRules.ifUserPatchesHimself(authentication,request)")
                .antMatchers("/users/**").hasAuthority(Role.MANAGER.name())
                .anyRequest().authenticated()
                .and()
            .httpBasic()
                .and()
            .csrf().disable();
    }
}

package ru.jvdev.demoapp.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 27.07.2016
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.httpBasic();
//        http.csrf().disable();
//        http
//            .exceptionHandling()
//            .authenticationEntryPoint(digestEntryPoint())
//            .and()
//            .addFilterAfter(digestAuthenticationFilter(), BasicAuthenticationFilter.class)
//            .csrf().disable()
//            .authorizeRequests().antMatchers("/**").hasRole("ADMIN");
    }

    @Bean
    public DigestAuthenticationEntryPoint digestEntryPoint() {
        DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
        digestAuthenticationEntryPoint.setKey("acegi");
        digestAuthenticationEntryPoint.setRealmName("Digest Realm");
        digestAuthenticationEntryPoint.setNonceValiditySeconds(30);
        return digestAuthenticationEntryPoint;
    }

    @Bean
    public DigestAuthenticationFilter digestAuthenticationFilter() {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        digestAuthenticationFilter.setUserDetailsService(userDetailsService);
        digestAuthenticationFilter.setPasswordAlreadyEncoded(true);
        return digestAuthenticationFilter;
    }
}

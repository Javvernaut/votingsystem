package javvernaut.votingsystem.config;

import javvernaut.votingsystem.model.Role;
import javvernaut.votingsystem.repository.UserRepository;
import javvernaut.votingsystem.util.security.AuthorizedUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static javvernaut.votingsystem.util.UserUtil.PASSWORD_ENCODER;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/api/profile/register").anonymous()
                .antMatchers("/api/restaurants/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(
                        email -> {
                            log.info("Authenticating {} ", email);
                            return new AuthorizedUser(userRepository.findByEmail(email)
                                    .orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found")));
                        }
                )
                .passwordEncoder(PASSWORD_ENCODER);
    }
}

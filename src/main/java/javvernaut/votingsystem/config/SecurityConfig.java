package javvernaut.votingsystem.config;

import javvernaut.votingsystem.repository.jpa.UserRepository;
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
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/profile/register").anonymous()
                .antMatchers("/menus").hasRole("USER")
                .antMatchers("/menus/dish").hasRole("ADMIN")
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
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

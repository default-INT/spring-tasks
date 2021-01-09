package by.gstu.springsecurity.config;

import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.security.JwtConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()

                .authorizeRequests()
                .antMatchers("/temp/**", "/profile/**").hasAuthority("DEFAULT")
                .antMatchers("/test/**").hasRole(Role.USER.name())
                .antMatchers(
                        "/api/auth/login",
                        "/api/auth/guest",
                        "/uploads/**",
                        "/static/**",
                        "/auth/**"
                ).permitAll()
                .antMatchers("/test").permitAll()
                .antMatchers("/api/auth/add-user-permission").hasAuthority(Permission.ADMIN_PERMISSION.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/api/auth/guest")
                    .failureForwardUrl("/api/auth/not-fount")
                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}

package com.asistlab.imagemaker.config;

import com.asistlab.imagemaker.model.enums.Permission;
import com.asistlab.imagemaker.repository.UserRepository;
import com.asistlab.imagemaker.security.AuthenticationFilter;
import com.asistlab.imagemaker.security.AuthorizationFilter;
import com.asistlab.imagemaker.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/auth/login",
                        "/api/auth/guest",
                        "/static/**",
                        "/auth/**",
                        "/uploads/**"
                ).permitAll()
                .antMatchers("/uploads/load-img").hasAuthority(Permission.ADD_IMAGE.name())
                .antMatchers("/api/auth/add-user-permission", "/user-manage", "/api/auth/change-status")
                    .hasAuthority(Permission.ADMIN_PERMISSION.name())
                .anyRequest().authenticated()
                .and()
                    .addFilter(new AuthenticationFilter(authenticationManager()))
                    .addFilter(new AuthorizationFilter(authenticationManager(), userRepository))
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .formLogin()
                    .loginPage("/auth/login") // todo guest auth /api/auth/guest
                    .failureForwardUrl("/api/auth/not-fount")
                .and()
                .logout();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
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

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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/temp/**", "/profile/**").hasAuthority("DEFAULT")
                .antMatchers(
                        "/api/auth/login",
                        "/api/auth/guest",
                        "/uploads/**",
                        "/static/**",
                        "/auth/**"
                ).permitAll()
                .antMatchers("/api/auth/add-user-permission").hasAuthority(Permission.ADMIN_PERMISSION.name())
                .antMatchers("/**").hasAuthority(Permission.DEFAULT.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/auth/login") // if todo guest auth /api/auth/guest
                    .failureForwardUrl("/api/auth/not-fount")
                .and()
                .apply(jwtConfigurer);
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

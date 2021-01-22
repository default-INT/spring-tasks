package com.asistlab.imagemaker.security;

import com.asistlab.imagemaker.exception.JwtAuthenticationException;
import com.asistlab.imagemaker.model.User;
import com.asistlab.imagemaker.model.enums.Status;
import com.asistlab.imagemaker.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                chain.doFilter(request, response);
                return;
            }
            User user = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new JwtAuthenticationException("Not found user on token"));
            if (user.getStatus() == Status.BANNED) {
                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                securityContextLogoutHandler.logout(request, response, null);
                SecurityContextHolder.clearContext();
                throw new IllegalArgumentException("User banned");
            }
            chain.doFilter(request, response);
        } catch (JwtAuthenticationException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            response.sendError(404, e.getMessage());
        }
    }
}

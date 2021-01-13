package by.gstu.springsecurity.security;

import by.gstu.springsecurity.exception.JwtAuthenticationException;
import by.gstu.springsecurity.model.User;
import by.gstu.springsecurity.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Optional<String> token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token.isPresent() && jwtTokenProvider.validateToken(token.get())) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token.get());
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            logger.warn(e.getMessage());
//            ((HttpServletResponse) servletResponse).sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
//            throw e;
        } catch (ExpiredJwtException e) {
            String urlPath = ((HttpServletRequest) servletRequest).getServletPath();
            SecurityContextHolder.clearContext();
            if (urlPath.contains("/temp")) {
                String uuid = urlPath.split("/")[2];
                User user = userRepository.findByUsername(uuid)
                        .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));;
                userRepository.delete(user);
            }
            ((HttpServletResponse) servletResponse).sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            throw e;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

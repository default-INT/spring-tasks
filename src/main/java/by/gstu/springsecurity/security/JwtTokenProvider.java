package by.gstu.springsecurity.security;

import by.gstu.springsecurity.exception.JwtAuthenticationException;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.model.User;
import by.gstu.springsecurity.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.expiration}")
    private long validityMilliseconds;
    @Value("${jwt.guest.expiration}")
    private long validityGuestMilliseconds;

    private long getMilliseconds(String role) {
        return role.equals(Role.GUEST.name()) ? validityGuestMilliseconds:  validityMilliseconds;
    }

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date nowDate = new Date();
        Date validityDate = new Date(nowDate.getTime() + getMilliseconds(role)  * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(validityDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            boolean oldToken = !claimsJws.getBody().getExpiration().before(new Date());
            if (!oldToken && claimsJws.getBody().get("role").equals(Role.GUEST.name())) {
                User user = userRepository.findByUsername(getUsername(token))
                        .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
                userRepository.delete(user);
            }
            return oldToken;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Jwt token is expired or invalid", e);
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}

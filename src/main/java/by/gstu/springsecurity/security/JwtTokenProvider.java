package by.gstu.springsecurity.security;

import by.gstu.springsecurity.exception.JwtAuthenticationException;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${cookie.key}")
    private String tokenCookieKey;
    @Value("${jwt.expiration}")
    private long validityMilliseconds;
    @Value("${jwt.guest.expiration}")
    private long validityGuestMilliseconds;

    private long getMilliseconds(String role) {
        return role.equals(Role.GUEST.name()) ? validityGuestMilliseconds:  validityMilliseconds;
    }

    public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
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
            return  !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw e;
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

    public Optional<String> resolveToken(HttpServletRequest request) {
        Optional<String> tokenOpt = Optional.ofNullable(request.getHeader(authorizationHeader));
        if (tokenOpt.isEmpty() && request.getCookies() != null) {
            Optional<Cookie> cookieOptional = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(tokenCookieKey))
                    .findFirst();
            return cookieOptional.map(Cookie::getValue);
        }
        return tokenOpt;
    }
}

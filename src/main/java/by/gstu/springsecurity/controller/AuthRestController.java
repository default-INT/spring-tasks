package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.dto.UserRequestDto;
import by.gstu.springsecurity.repository.UserRepository;
import by.gstu.springsecurity.security.JwtTokenProvider;
import by.gstu.springsecurity.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    // TODO: temporary solution
    private static final String GUEST_PASS = "$2y$12$l0xLhkorx8v2/BDymHBpzekg1WxClJ1v6lzxd6zLzpxCwHxWZOiey";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public AuthRestController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @GetMapping("/guest")
    public ResponseEntity<?> guestAuth() {
        try {
            return authService.guestAuth();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/not-found")
    public ResponseEntity<?> notFound() {
        return new ResponseEntity<>("Not-found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserRequestDto request) {
        try {
            return authService.login(request);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}

package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.dto.UserRequestDto;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.model.Status;
import by.gstu.springsecurity.model.User;
import by.gstu.springsecurity.repository.UserRepository;
import by.gstu.springsecurity.security.JwtTokenProvider;
import org.apache.coyote.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    // TODO: temporary solution
    private static final String GUEST_PASS = "$2y$12$l0xLhkorx8v2/BDymHBpzekg1WxClJ1v6lzxd6zLzpxCwHxWZOiey";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthRestController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/guest")
    public ResponseEntity<?> guestAuth() {
        try {
            String uuidUsername = UUID.randomUUID().toString();
            User user = new User();
            user.setUsername(uuidUsername);
            user.setPassword(GUEST_PASS);
            user.setRole(Role.GUEST);
            user.setStatus(Status.ACTIVE);

            userRepository.save(user);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), Role.GUEST.name())
            );
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
            return ResponseEntity.ok(Map.of(
                    "uuid", user.getUsername(),
                    "role", user.getRole().name(),
                    "token", token
            ));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());

            return ResponseEntity.ok(Map.of("username", request.getUsername(), "token", token));
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

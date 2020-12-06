package by.gstu.springsecurity.service;

import by.gstu.springsecurity.dto.UserRequestDto;
import by.gstu.springsecurity.model.RoleType;
import by.gstu.springsecurity.model.Status;
import by.gstu.springsecurity.model.User;
import by.gstu.springsecurity.repository.UserRepository;
import by.gstu.springsecurity.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    // TODO: temporary solution
    private static final String GUEST_PASS = "$2y$12$l0xLhkorx8v2/BDymHBpzekg1WxClJ1v6lzxd6zLzpxCwHxWZOiey";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> guestAuth() throws AuthenticationException {
        String uuidUsername = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername(uuidUsername);
        user.setPassword(GUEST_PASS);
        user.setRole(RoleType.GUEST);
        user.setStatus(Status.ACTIVE);

        userRepository.save(user);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), RoleType.GUEST.name())
        );
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
                "uuid", user.getUsername(),
                "role", user.getRole().name(),
                "token", token
        ));
    }

    public ResponseEntity<?> login(UserRequestDto userRequestDto) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword())
        );
        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(Map.of("username", user.getUsername(), "token", token));
    }

    public boolean validUuid(String uuid) {
        return userRepository.existsByUsername(uuid);
    }
}

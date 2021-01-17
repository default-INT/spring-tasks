package com.asistlab.imagemaker.service;

import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.exception.IllegalInsertEntityExistException;
import com.asistlab.imagemaker.exception.JwtAuthenticationException;
import com.asistlab.imagemaker.model.enums.Role;
import com.asistlab.imagemaker.model.enums.Status;
import com.asistlab.imagemaker.model.User;
import com.asistlab.imagemaker.repository.UserRepository;
import com.asistlab.imagemaker.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private static final PasswordEncoder PASSWORD_ENCODER = passwordEncoder();

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> guestAuth() throws AuthenticationException {
        String uuidUsername = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername(uuidUsername);
        user.setPassword(PASSWORD_ENCODER.encode("GUEST"));
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
    }

    public UserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UserDto.of(userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new JwtAuthenticationException("Not found user on token")));
    }

    public UserDto login(UserDto userRequestDto) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword())
        );
        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        return UserDto.of(user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getRole().name().toLowerCase(), token);
    }

    public UserDto registration(UserDto userDto) {
        User user = User.of(userDto);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalInsertEntityExistException("User with username='" + user.getUsername() + "' exists");
        }
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        userRepository.save(user);

        return login(userDto);
    }

    public boolean validUuid(String uuid) {
        return userRepository.existsByUsername(uuid);
    }
}

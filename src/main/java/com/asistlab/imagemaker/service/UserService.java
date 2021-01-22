package com.asistlab.imagemaker.service;

import com.asistlab.imagemaker.dto.ImageDto;
import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.exception.IllegalInsertEntityExistException;
import com.asistlab.imagemaker.exception.JwtAuthenticationException;
import com.asistlab.imagemaker.model.User;
import com.asistlab.imagemaker.model.enums.Role;
import com.asistlab.imagemaker.model.enums.Status;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final PasswordEncoder PASSWORD_ENCODER = passwordEncoder();

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    public UserDto findByUsername(String username) {
        return UserDto.of(userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new));
    }

    public UserDto changeStatus(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User with username: '" + userDto.getStatus() + "' not found."));
        user.setStatus(user.getStatus().equals(Status.ACTIVE) ? Status.BANNED : Status.ACTIVE);
        userRepository.save(user);
        emailService.sendMessage(user.getEmail(), "Changed user status", "Set new status: " + user.getStatus().name());
        return UserDto.of(user);
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

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .sorted((u1, u2) -> u1.getUsername().compareToIgnoreCase(u2.getUsername()))
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    public UserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UserDto.of(userRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new JwtAuthenticationException("Not found user on username")));
    }

    public UserDto editUserInfo(UserDto userDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new JwtAuthenticationException("Not found user on token"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(PASSWORD_ENCODER.encode(userDto.getPassword()));
        userRepository.save(user);

        return UserDto.of(user);
    }

    public UserDto login(UserDto userRequestDto) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword())
        );
        User user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
        return UserDto.of(user, token);
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

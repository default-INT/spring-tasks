package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.dto.PermissionRequestDto;
import by.gstu.springsecurity.dto.UserDto;
import by.gstu.springsecurity.model.Permission;
import by.gstu.springsecurity.model.Role;
import by.gstu.springsecurity.service.RolePermissionService;
import by.gstu.springsecurity.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;
    private final RolePermissionService rolePermissionService;

    public AuthRestController(UserService userService, RolePermissionService rolePermissionService) {
        this.userService = userService;
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping("/guest")
    public ResponseEntity<?> guestAuth() {
        try {
            return userService.guestAuth();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/add-user-permission")
    public ResponseEntity<?> addPermission(@RequestBody PermissionRequestDto request) {
        try {
            rolePermissionService.addPermission(Role.USER, Permission.valueOf(request.getPermission()));
            return ResponseEntity.ok("User role up permission");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid permission", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/delete-user-permission")
    public ResponseEntity<?> deletePermission(@RequestBody PermissionRequestDto request) {
        try {
            rolePermissionService.deletePermission(Role.USER, Permission.valueOf(request.getPermission()));
            return ResponseEntity.ok("User role delete permission: " + request.getPermission());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid permission", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/not-found")
    public ResponseEntity<?> notFound() {
        return new ResponseEntity<>("Not-found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDto request) {
        try {
            return ResponseEntity.ok(userService.login(request));
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

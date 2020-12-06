package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TempController {
    private final UserService userService;

    public TempController(UserService userService) {
        this.userService = userService;
    }

    //TODO: guest authorization
    @GetMapping("/{uuid}")
    public ResponseEntity<?> guestPage(@PathVariable String uuid) {
        try {
            if (!userService.validUuid(uuid)) {
                return new ResponseEntity<>("Uuid not found", HttpStatus.FORBIDDEN);
            }
            return ResponseEntity.ok(uuid);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("JWT token die", HttpStatus.FORBIDDEN);
        }
    }
}

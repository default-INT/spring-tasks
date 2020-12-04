package by.gstu.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/temp")
public class TempController {
    //TODO: guest authorization
    @GetMapping("{uuid}")
    public void guestPage(@RequestParam String uuid) {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }
}

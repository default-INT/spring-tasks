package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/")
public class ContentMvcController {

    private final UserService userService;

    public ContentMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getMainPage(Map<String, Object> model) {
        model.put("user", userService.getCurrentUser());
        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Map<String, Object> model) {
        model.put("user", userService.getCurrentUser());
        return "profile";
    }
}

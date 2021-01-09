package by.gstu.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ContentController {
    @GetMapping("/test")
    public String getMainPage() {
        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Map<String, Object> model, HttpServletRequest httpRequest) {
        return "profile";
    }
}

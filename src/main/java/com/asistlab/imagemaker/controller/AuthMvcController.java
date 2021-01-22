package com.asistlab.imagemaker.controller;

import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
public class AuthMvcController {

    private final static Logger logger = LogManager.getLogger();

    private final UserService userService;

    public AuthMvcController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return "redirect:/auth/login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(UserDto requestDto, HttpServletResponse response) {
        UserDto userDto = userService.registration(requestDto);
        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/log")
    public String loginAuth() {
        return "redirect:/profile";
    }
}

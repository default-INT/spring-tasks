package by.gstu.springsecurity.controller;

import by.gstu.springsecurity.dto.UserRequestDto;
import by.gstu.springsecurity.dto.UserResponseDto;
import by.gstu.springsecurity.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthMvcController {

    private final static Logger logger = LogManager.getLogger();

    private final UserService userService;

    public AuthMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
            securityContextLogoutHandler.logout(request, response, null);
            response.sendRedirect("/login");
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(UserRequestDto requestDto, Map<String, Object> model, HttpServletRequest httpRequest, HttpServletResponse response) {
        UserResponseDto responseDto = userService.login(requestDto);
        httpRequest.getSession().setAttribute("token", responseDto.getToken());
        model.put("user", responseDto);
        return "index";
    }
}

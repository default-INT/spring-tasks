package com.asistlab.imagemaker.controller;

import com.asistlab.imagemaker.dto.UserDto;
import com.asistlab.imagemaker.service.ImageService;
import com.asistlab.imagemaker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ContentMvcController {

    private final UserService userService;
    private final ImageService imageService;

    public ContentMvcController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping
    public String getMainPage(Map<String, Object> model, HttpServletRequest request) {
        model.put("user", userService.getCurrentUser());
        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Map<String, Object> model) {
        model.put("user", userService.getCurrentUser());
        return "profile";
    }

    @GetMapping("/profile-edit")
    public String getProfileEdit(Map<String, Object> model) {
        model.put("user", userService.getCurrentUser());

        return "profileEdit";
    }

    @GetMapping("/user-manage")
    public String getUserManagePage(Map<String, Object> model) {
        model.put("user", userService.getCurrentUser());
        model.put("users", userService.findAll());
        return "userManage";
    }

    @GetMapping("/my-images")
    public String getUserImages(Map<String, Object> model) {
        UserDto userDto = userService.getCurrentUser();
        model.put("user", userDto);
        model.put("images", imageService.findAllByUsername(userDto.getUsername()));
        return "userImages";
    }

    @PostMapping("/profile-edit" )
    public String editUserInfo(UserDto userDto, String confirmPassword,  Map<String, Object> model) {
        try {
            if (!confirmPassword.equals(userDto.getPassword())) {
                throw new IllegalArgumentException("Not confirm password!");
            }
            userService.editUserInfo(userDto);
        } catch (Exception e) {
            model.put("errorMsg", e.getMessage());
            model.put("user", userService.getCurrentUser());
            return "profileEdit";
        }
        return "redirect:/profile";
    }
}

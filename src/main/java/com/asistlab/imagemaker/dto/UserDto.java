package com.asistlab.imagemaker.dto;

import com.asistlab.imagemaker.model.User;
import com.asistlab.imagemaker.model.enums.Role;

import javax.persistence.Transient;

public class UserDto {

    public static UserDto of(String username, String token) {
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setToken(token);
        return user;
    }

    public static UserDto of(User rawUser) {
        UserDto user = new UserDto();

        user.setId(rawUser.getId());
        user.setUsername(rawUser.getUsername());
        user.setFirstName(rawUser.getFirstName());
        user.setLastName(rawUser.getLastName());
        user.setRole(rawUser.getRole().name().toLowerCase());

        return user;
    }

    public static UserDto of(UserDto rawUser) {
        UserDto user = new UserDto();

        user.setUsername(rawUser.getUsername());
        user.setFirstName(rawUser.getFirstName());
        user.setLastName(rawUser.getLastName());
        user.setRole(rawUser.getRole());
        user.setToken(rawUser.getToken());

        return user;
    }

    public static UserDto of(String username, String firstName, String lastName, String role, String token) {
        UserDto user = new UserDto();

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setToken(token);
        return user;
    }

    private Long id;
    private String username;
    @Transient
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String token;

    public UserDto() {
    }

    public UserDto(String username, String firstName, String lastName, String role, String token) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.token = token;
    }

    public boolean isAdmin() {
        return role.equalsIgnoreCase(Role.ADMIN.name());
    }

    public boolean isUser() {
        return role.equalsIgnoreCase(Role.USER.name());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

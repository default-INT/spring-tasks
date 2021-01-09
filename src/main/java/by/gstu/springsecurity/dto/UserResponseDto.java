package by.gstu.springsecurity.dto;

public class UserResponseDto {

    public static UserResponseDto of(String username, String token) {
        UserResponseDto user = new UserResponseDto();
        user.setUsername(username);
        user.setToken(token);
        return user;
    }

    public static UserResponseDto of(String username, String firstName, String lastName, String role, String token) {
        UserResponseDto user = new UserResponseDto();

        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setToken(token);
        return user;
    }

    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String token;

    public UserResponseDto() {
    }

    public UserResponseDto(String username, String firstName, String lastName, String role, String token) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.token = token;
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

package by.gstu.springsecurity.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public static User getGuest() {
        User guest = new User();
        guest.setFirstName("guest");
        guest.setLastName("guest");
        guest.setUsername("guest");
        guest.setRole(RoleType.GUEST);
        return guest;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(length = 30)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(length = 30)
    @Enumerated(value = EnumType.STRING)
    private RoleType role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

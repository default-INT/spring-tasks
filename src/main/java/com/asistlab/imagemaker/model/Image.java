package com.asistlab.imagemaker.model;

import com.asistlab.imagemaker.dto.UserDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "images")
public class Image {
    
    public static Image of(String name, String filePath, String contentType, Long userId) {
        Image image = new Image();
        image.setName(name);
        image.setFilePath(filePath);
        image.setContentType(contentType);
        image.setUserId(userId);
        return image;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_path", unique = true)
    private String filePath;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id) &&
                Objects.equals(filePath, image.filePath) &&
                Objects.equals(name, image.name) &&
                Objects.equals(contentType, image.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, name, contentType);
    }
}

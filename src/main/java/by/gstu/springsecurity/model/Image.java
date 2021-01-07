package by.gstu.springsecurity.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_path", unique = true)
    private String filePath;
    @Column
    private int width;
    @Column
    private int height;

    @ManyToMany
    private Set<ImageEffect> imageEffects;

    public Collection<ImageEffect> getImageEffects() {
        return imageEffects;
    }

    public void setImageEffects(Set<ImageEffect> imageEffects) {
        this.imageEffects = imageEffects;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

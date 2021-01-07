package by.gstu.springsecurity.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "image_effects")
public class ImageEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String effect;

    @ManyToMany(mappedBy = "imageEffects", fetch = FetchType.EAGER)
    private Set<Image> images;

    public Set<Image> getImage() {
        return images;
    }

    public void setImage(Set<Image> images) {
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}

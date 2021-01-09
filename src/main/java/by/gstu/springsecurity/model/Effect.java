package by.gstu.springsecurity.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "effects")
public class Effect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String effect;

    @OneToMany(mappedBy = "effect")
    private Set<ImageEffect> imageEffects;

    public Set<ImageEffect> getImageEffects() {
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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}

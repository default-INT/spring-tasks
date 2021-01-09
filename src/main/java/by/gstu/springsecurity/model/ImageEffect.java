package by.gstu.springsecurity.model;


import javax.persistence.*;

@Entity
@Table(name ="image_effect")
public class ImageEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "effect_id")
    private Long effectId;
    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name="image_id", updatable = false, insertable = false)
    private Image image;
    @ManyToOne
    @JoinColumn(name="effect_id", updatable = false, insertable = false)
    private Effect effect;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getEffectId() {
        return effectId;
    }

    public void setEffectId(Long effectId) {
        this.effectId = effectId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}

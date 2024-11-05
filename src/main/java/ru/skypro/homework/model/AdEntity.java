package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.User;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class AdEntity {

    public AdEntity(Integer pk, String image, Integer price, String title, String description, UserEntity author) {
        this.pk = pk;
        this.image = image;
        this.price = price;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public AdEntity(Integer pk, String image, String title, Integer price, UserEntity author) {
        this.pk = pk;
        this.image = image;
        this.title = title;
        this.price = price;
        this.author = author;
    }

    public AdEntity(String title, Integer price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private String image;
    private Integer price;
    private String title;
    private String description;

    @OneToMany(mappedBy = "ad")
    private List<CommentEntity> comments;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
}

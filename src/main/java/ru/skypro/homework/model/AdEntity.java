package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.User;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class AdEntity {

//    private Integer author;
//    private String image;
//    private Integer pk;
//    private Integer price;
//    private String title;

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

    public AdEntity(UserEntity author, List<CommentEntity> comments, String description, String title, Integer price, String image) {
        this.author = author;
        this.comments = comments;
        this.description = description;
        this.title = title;
        this.price = price;
        this.image = image;
    }
}

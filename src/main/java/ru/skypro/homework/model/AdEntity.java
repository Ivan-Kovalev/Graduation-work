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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private String image;
    private Integer price;
    private String title;

    @OneToMany(mappedBy = "ad")
    private List<CommentEntity> comments;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;
}

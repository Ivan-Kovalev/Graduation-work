package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Builder
public class AdEntity {

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

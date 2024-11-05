package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class CommentEntity {

    public CommentEntity(UserEntity author, String authorImage, String authorFirstName, Integer createdAt, Integer pk, String text) {
        this.author = author;
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }

    public CommentEntity(String text) {
        this.text = text;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private String authorImage;
    private String authorFirstName;
    private Integer createdAt;
    private String text;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private AdEntity ad;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity author;

}

package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class CommentEntity {

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

    public CommentEntity(String authorImage, String authorFirstName, Integer createdAt, String text, AdEntity ad, UserEntity author) {
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.text = text;
        this.ad = ad;
        this.author = author;
    }
}

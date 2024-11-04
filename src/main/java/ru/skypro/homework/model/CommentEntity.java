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

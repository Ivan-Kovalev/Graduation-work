package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

    @OneToMany(mappedBy = "author")
    private List<AdEntity> ads;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<CommentEntity> comments;

    public UserEntity(String email, String password, String firstName, String lastName, String phone, Role role, String image, List<AdEntity> ads, List<CommentEntity> comments) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.ads = ads;
        this.comments = comments;
    }
}

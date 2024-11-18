package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private Integer pk;
    private String authorImage;
    private String authorFirstName;
    private Integer createdAt;
    private String text;

    private Integer author;
}

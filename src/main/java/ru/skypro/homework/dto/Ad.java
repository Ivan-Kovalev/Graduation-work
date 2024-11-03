package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ad {
    private Integer author;
    private String image;
    private Integer pk;
    private Integer price;
    private String title;
}

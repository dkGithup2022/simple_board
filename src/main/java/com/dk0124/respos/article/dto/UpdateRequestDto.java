package com.dk0124.respos.article.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateRequestDto {
    private UUID article_id;
    private String title;
    private String content;
    private String category;
}

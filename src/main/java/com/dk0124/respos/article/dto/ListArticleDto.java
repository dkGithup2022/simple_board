package com.dk0124.respos.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ListArticleDto {
    private List<DetailedArticleDto> list;
    private Long cursorId;
}

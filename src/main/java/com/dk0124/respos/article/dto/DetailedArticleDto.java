package com.dk0124.respos.article.dto;

import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.domain.ECategory;
import com.dk0124.respos.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DetailedArticleDto {
    private UUID id;

    private Member member;

    private String title;

    private String content;

    private ECategory category;

    private Long views;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public DetailedArticleDto(Article article){

    }

}

package com.dk0124.respos.article.dto;

import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.domain.ECategory;
import com.dk0124.respos.member.Member;
import lombok.Data;

import java.util.UUID;

@Data
public class DetailedArticleDto {
    private UUID id;

    private Member member;

    private String title;

    private String content;

    private ECategory category;

    private Long views;


    public DetailedArticleDto(Article article){

    }

}

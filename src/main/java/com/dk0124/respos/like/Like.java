package com.dk0124.respos.like;

import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Like(Member member, Article article) {
        this.article = article;
        this.member = member;
    }
}

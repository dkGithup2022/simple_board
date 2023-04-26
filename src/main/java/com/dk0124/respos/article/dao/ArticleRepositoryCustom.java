package com.dk0124.respos.article.dao;

import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dk0124.respos.article.domain.QArticle.article;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Slice<DetailedArticleDto> list(Pageable pageable) {
        List<DetailedArticleDto> articles = queryFactory
                .select(
                        Projections.constructor(DetailedArticleDto.class,
                                article.id,
                                article.title,
                                article.content,
                                article.category,
                                article.views
                        )
                )
                .from(article)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return new SliceImpl<>(articles, pageable, sliceHasNext(pageable, articles));
    }

    private boolean sliceHasNext(Pageable pageable, List contents) {
        return pageable.getPageSize() + 1 == contents.size();
    }
}

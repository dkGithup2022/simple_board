package com.dk0124.respos.article.dao;

import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.dk0124.respos.article.domain.QArticle.article;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 리스트 요청. 기본적으로 date 기준 정렬, date가 null 이라면 현재시각 이후로 넘김 .
     * @param cursorId
     * @param pageable
     * @return
     */
    public Slice<DetailedArticleDto> list(LocalDateTime cursorId, Pageable pageable) {

        if (cursorId == null)
            cursorId = LocalDateTime.now();

        List<DetailedArticleDto> articles = queryFactory
                .select(
                        Projections.constructor(DetailedArticleDto.class,
                                article.id,
                                article.member.id,
                                article.title,
                                article.content,
                                article.category,
                                article.views,
                                article.createdAt,
                                article.updatedAt
                        )
                )
                .from(article)
                .where(article.createdAt.before(cursorId))
                .orderBy(article.createdAt.desc())
                .fetch();

        return new SliceImpl<>(articles, pageable, sliceHasNext(pageable, articles));
    }


    private boolean sliceHasNext(Pageable pageable, List contents) {
        return pageable.getPageSize() + 1 == contents.size();
    }
}

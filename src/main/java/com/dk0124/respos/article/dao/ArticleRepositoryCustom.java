package com.dk0124.respos.article.dao;

import static com.dk0124.respos.article.domain.QArticle.*;
import static com.dk0124.respos.like.QLike.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.dk0124.respos.article.domain.QArticle;
import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.dk0124.respos.article.service.ArticleListRequest;
import com.dk0124.respos.common.ESort;
import com.dk0124.respos.like.QLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public Slice<DetailedArticleDto> list(Pageable pageable, ArticleListRequest listRequest) {

		var condition = buildCondition(listRequest);
		var orderCondition = buildOrderCondition(listRequest);

		List<DetailedArticleDto> articles =
			queryFactory.select(detailedArticleExpression(article, like))
				.from(article)
				.leftJoin(like).on(article.id.eq(like.article.id))
				.groupBy(article.id)
				.having(condition)
				.orderBy(orderCondition)
				.fetch();

		return new SliceImpl<>(articles, pageable, sliceHasNext(pageable, articles));
	}

	private OrderSpecifier[] buildOrderCondition(ArticleListRequest listRequest) {
		List<OrderSpecifier> orders = new ArrayList<>();
		if (listRequest.getLikeSort() != null)
			orders.add(listRequest.getLikeSort() == ESort.DESC ?
				like.count().desc() : like.count().asc());

		if (listRequest.getTimeSort() != null)
			orders.add(listRequest.getTimeSort() == ESort.DESC ?
				article.createdAt.desc() : article.createdAt.asc());

		return orders.toArray(new OrderSpecifier[orders.size()]);
	}

	private BooleanBuilder buildCondition(ArticleListRequest listRequest) {
		BooleanBuilder booleanBuilder = new BooleanBuilder();

		if (listRequest.getOwnerNickName() != null && !listRequest.getOwnerNickName().isBlank()) {
			booleanBuilder.and(article.member.nickName.eq(listRequest.getOwnerNickName()));
		}

		if (listRequest.getTimeBefore() != null) {
			booleanBuilder.and(article.createdAt.lt(listRequest.getTimeBefore()));
		}

		if (listRequest.getLikeBefore() != null) {
			booleanBuilder.and(like.count().lt(listRequest.getLikeBefore()));
		}

		return booleanBuilder;
	}

	private boolean sliceHasNext(Pageable pageable, List contents) {
		return pageable.getPageSize() + 1 == contents.size();
	}

	private ConstructorExpression<DetailedArticleDto> detailedArticleExpression(QArticle article, QLike like) {
		return Projections.constructor(
			DetailedArticleDto.class,
			article.id,
			article.member.id,
			article.title,
			article.content,
			article.category,
			article.views,
			article.createdAt,
			article.updatedAt,
			like.count()
		);
	}

}

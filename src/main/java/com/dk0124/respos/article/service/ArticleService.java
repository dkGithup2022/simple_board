package com.dk0124.respos.article.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.dao.ArticleRepositoryCustom;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.domain.Category;
import com.dk0124.respos.article.domain.ECategory;
import com.dk0124.respos.article.dto.ArticleListCursor;
import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.dk0124.respos.article.dto.UpdateRequestDto;
import com.dk0124.respos.common.utils.TimeUtil;
import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

	private final ArticleRepositoryCustom articleRepositoryCustom;
	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;

	public Slice<DetailedArticleDto> list(ArticleListRequest listRequest) {
		return articleRepositoryCustom.list(Pageable.ofSize(100), listRequest);
	}

	public void update(Article article, UpdateRequestDto updateRequest) {
		validateUpdateRequest(article, updateRequest);

		if (!updateRequest.getTitle().isBlank())
			article.setTitle(updateRequest.getTitle());

		if (!updateRequest.getContent().isBlank())
			article.setContent(updateRequest.getContent());

		if (ECategory.contains(updateRequest.getCategory()))
			article.setCategory(new Category(ECategory.getCategory(updateRequest.getCategory())));
	}

	public boolean ownedByCurrentUser(UUID articleID) {
		UserDetailsImpl userDetails = (UserDetailsImpl)AuthUtils.getCurrentUserDetails();
		Member member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new RuntimeException("ArticleApi update : 요청자 유저를 찾을 수 없음 "));

		Article article = articleRepository.findById(articleID)
			.orElseThrow(() -> new RuntimeException("ArticleApi update : 아티클 아이디가 잘못됨"));

		if (article.getMember() != member)
			throw new RuntimeException("ArticleApi update: 요청자 불일치 ");

		return true;
	}

	private void validateUpdateRequest(Article article, UpdateRequestDto updateRequest) {
		if (article.getId() != updateRequest.getArticle_id())
			throw new RuntimeException("article id not matched");

		if (updateRequest.getTitle().isBlank() && updateRequest.getContent().isBlank()) {
			if (updateRequest.getCategory().isBlank())
				throw new RuntimeException("Empty update Request");
			else if (!ECategory.contains(updateRequest.getCategory()))
				throw new RuntimeException("Invalid category");
		}
	}

	public ArticleListCursor nextListCursor(Slice<DetailedArticleDto> sliced, ArticleListRequest listRequest) {
		if (sliced.getContent().size() == 0)
			return new ArticleListCursor(null, null);

		var res = new ArticleListCursor(
			TimeUtil.localDateTimeToLong(sliced.getContent().get(sliced.getContent().size() - 1).getCreatedAt()),
			sliced.getContent().get(sliced.getContent().size() - 1).getLikeCnt()
		);

		return res;
	}
}

package com.dk0124.respos.article.service;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.dao.ArticleRepositoryCustom;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.domain.Category;
import com.dk0124.respos.article.domain.ECategory;
import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.dk0124.respos.article.dto.UpdateRequestDto;
import com.dk0124.respos.common.utils.TimeUtil;
import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepositoryCustom articleRepositoryCustom;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Slice<DetailedArticleDto> listWithMillis(Long millis, Pageable pageable) {
        LocalDateTime cursorId = millis == null ? null : TimeUtil.longToLocalDateTime(millis);
        return articleRepositoryCustom.list(cursorId, pageable);
    }

    public Long nextCursorId(Slice<DetailedArticleDto> sliced, Long cursorId) {
        if (sliced.getContent().size() == 0)
            return cursorId;
        else
            return TimeUtil.localDateTimeToLong(sliced.getContent().get(sliced.getContent().size() - 1).getCreatedAt());
    }

    public boolean ownedByCurrentUser(UUID articleID) {
        UserDetailsImpl userDetails = (UserDetailsImpl) AuthUtils.getCurrentUserDetails();
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("ArticleApi update : 요청자 유저를 찾을 수 없음 "));

        Article article = articleRepository.findById(articleID)
                .orElseThrow(() -> new RuntimeException("ArticleApi update : 아티클 아이디가 잘못됨"));

        if (article.getMember() != member)
            throw new RuntimeException("ArticleApi update: 요청자 불일치 ");

        return true;
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
}

package com.dk0124.respos.like.controller;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.like.Like;
import com.dk0124.respos.like.dao.LikeRepository;
import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Transactional
public class LikeController {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    @GetMapping("/register/{articleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> registerLikeArticle(@PathVariable @Valid String articleId) {
        likeRepository.save(new Like(getCurrentMember(), getArticle(articleId)));
        return ResponseEntity.ok().body("like");
    }

    @GetMapping("/undo/{articleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> undoLikeArticle(@PathVariable @Valid String articleId) {
        Like like = likeRepository.findByMemberAndArticle(getCurrentMember(), getArticle(articleId)).orElseThrow(
                () -> new RuntimeException("Invalid input : No matching Like "));

        likeRepository.deleteById(like.getId());
        return ResponseEntity.ok().body("deleted");
    }


    private Member getCurrentMember() {
        UserDetailsImpl userDetails = (UserDetailsImpl) AuthUtils.getCurrentUserDetails();
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("ArticleApi create : 유저를 찾을 수 없음 "));
        return member;
    }

    private Article getArticle(String articleId) {
        return articleRepository.findById(UUID.fromString(articleId)).orElseThrow(
                () -> new RuntimeException("ArticleApi get: 아티클을 찾을 수 없음 "));

    }
}

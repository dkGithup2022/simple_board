package com.dk0124.respos.like.dao;

import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.like.Like;
import com.dk0124.respos.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByMemberAndArticle(Member member, Article article);
}

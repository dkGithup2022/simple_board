package com.dk0124.respos.article.controller;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.dto.CreateArticleRequestDto;
import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.dk0124.respos.article.service.ArticleService;
import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/article")
@Transactional
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity create(@Valid @RequestBody CreateArticleRequestDto createRequestDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) AuthUtils.getCurrentUserDetails();

        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("ArticleApi create : 유저를 찾을 수 없음 "));

        articleRepository.save(new Article(member, createRequestDto.getTitle(), createRequestDto.getContent(), createRequestDto.getCategory()));

        return ResponseEntity.ok().body("created");
    }


    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<DetailedArticleDto> get(@RequestParam String id) {
        Article article = articleRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new RuntimeException("ArticleApi get: 아티클을 찾을 수 없음 "));

        DetailedArticleDto articleDto = modelMapper.map(article, DetailedArticleDto.class);

        return ResponseEntity.ok().body(articleDto);
    }


}

package com.dk0124.respos.article.controller;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.dto.*;
import com.dk0124.respos.article.service.ArticleService;
import com.dk0124.respos.member.Member;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<String> create(@Valid @RequestBody CreateArticleRequestDto createRequestDto) {
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

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ListArticleDto> listWithTimeStamp(@RequestParam(required = false) Long millis) {
        Slice<DetailedArticleDto> sliced = articleService.listWithMillis(millis, PageRequest.of(0, 100));
        Long cursorId = articleService.nextCursorId(sliced, millis);
        return ResponseEntity.ok().body(new ListArticleDto(sliced.getContent(), cursorId));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> update(@RequestBody UpdateRequestDto updateRequest) {
        articleService.ownedByCurrentUser(updateRequest.getArticle_id());
        Article article = articleRepository.findById(updateRequest.getArticle_id()).orElseThrow(
                () -> new RuntimeException("No matching article")
        );

        articleService.update(article, updateRequest);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> delete(@RequestBody DeleteArticleRequestDto deleteRequestDto) {
        articleService.ownedByCurrentUser(deleteRequestDto.getUUID());
        articleRepository.deleteById(deleteRequestDto.getUUID());
        return ResponseEntity.ok().body("delted");
    }


}

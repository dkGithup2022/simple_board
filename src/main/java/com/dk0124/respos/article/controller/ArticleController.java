package com.dk0124.respos.article.controller;

import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dk0124.respos.article.dao.ArticleRepository;
import com.dk0124.respos.article.domain.Article;
import com.dk0124.respos.article.dto.CreateArticleRequestDto;
import com.dk0124.respos.article.dto.DeleteArticleRequestDto;
import com.dk0124.respos.article.dto.DetailedArticleDto;
import com.dk0124.respos.article.dto.ListArticleDto;
import com.dk0124.respos.article.dto.UpdateRequestDto;
import com.dk0124.respos.article.service.ArticleListRequest;
import com.dk0124.respos.article.service.ArticleService;
import com.dk0124.respos.common.utils.TimeUtil;
import com.dk0124.respos.member.dao.MemberRepository;
import com.dk0124.respos.security.AuthUtils;
import com.dk0124.respos.security.userDetails.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

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

		var userDetails = (UserDetailsImpl)AuthUtils.getCurrentUserDetails();
		var member = memberRepository.findById(userDetails.getId())
			.orElseThrow(() -> new RuntimeException("ArticleApi create : 유저를 찾을 수 없음 "));

		var article = articleRepository.save(
			new Article(member, createRequestDto.getTitle(), createRequestDto.getContent(),
				createRequestDto.getCategory()));

		return ResponseEntity.ok().body("created : " + article.getId());
	}

	@GetMapping("/get/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<DetailedArticleDto> get(@PathVariable String id) {

		var article = articleRepository.findById(UUID.fromString(id))
			.orElseThrow(() -> new RuntimeException("ArticleApi get: 아티클을 찾을 수 없음 "));

		var articleDto = modelMapper.map(article, DetailedArticleDto.class);
		return ResponseEntity.ok().body(articleDto);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<ListArticleDto> list(
		@RequestParam(required = false, name = "timeBefore") Long timeBefore,
		@RequestParam(required = false, name = "likeBefore") Integer likeBefore,
		@RequestParam(required = false) String nickName
	) {

		var listReuest = new ArticleListRequest(nickName, timeBefore, likeBefore);

		var sliced = articleService.list(listReuest);

		return ResponseEntity.ok()
			.body(new ListArticleDto(sliced.getContent(), articleService.nextListCursor(sliced, listReuest)));
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<String> update(@RequestBody UpdateRequestDto updateRequest) {

		articleService.ownedByCurrentUser(updateRequest.getArticle_id());
		var article = articleRepository.findById(updateRequest.getArticle_id()).orElseThrow(
			() -> new RuntimeException("No matching article"));

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

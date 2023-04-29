package com.dk0124.respos.article.service;

import java.time.LocalDateTime;

import com.dk0124.respos.common.ESort;
import com.dk0124.respos.common.utils.TimeUtil;

import lombok.Builder;
import lombok.Data;

@Data
public class ArticleListRequest {
	private String ownerNickName;

	private LocalDateTime timeBefore;
	private ESort timeSort = ESort.DESC;

	private Integer likeBefore;
	private ESort likeSort = ESort.DESC;

	public ArticleListRequest(String ownerNickName, Long millis, Integer likeBefore) {
		this.ownerNickName = ownerNickName;
		this.timeBefore = millis == null ? LocalDateTime.now() : TimeUtil.longToLocalDateTime(millis);
		this.likeBefore = likeBefore == null ? Integer.MAX_VALUE : likeBefore;
	}
}

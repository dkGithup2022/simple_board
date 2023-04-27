package com.dk0124.respos.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteArticleRequestDto {
    private String id;

    public UUID getUUID(){
        return UUID.fromString(this.id);
    }
}

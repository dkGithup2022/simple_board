package com.dk0124.respos.article.domain;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
public class Category {

    @Id
    @Enumerated(EnumType.STRING)
    private ECategory category;

}

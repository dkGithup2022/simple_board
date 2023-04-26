package com.dk0124.respos.article.domain;

import com.dk0124.respos.common.BaseEntity;
import com.dk0124.respos.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "article_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @Getter
    private UUID id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Getter
    private Member member;

    @Size(max = 255)
    @Column(nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    @Getter
    @Setter
    private String content;


    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)", nullable = false)
    @Getter
    @Setter
    private ECategory category;

    @Size(min = 0)
    @Getter
    @Setter
    private Long views;

    public Article(Member member, String title, String content, ECategory category) {
        this(null, member, title, content, category, 0L);
    }
    public Article(Member member, String title, String content, String category) {
        this(null, member, title, content, ECategory.getCategory(category), 0L);
    }


}

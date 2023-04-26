package com.dk0124.respos.article.dao;

import com.dk0124.respos.article.domain.Category;
import com.dk0124.respos.article.domain.ECategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, ECategory> {
}

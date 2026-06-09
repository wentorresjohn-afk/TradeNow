package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {
}

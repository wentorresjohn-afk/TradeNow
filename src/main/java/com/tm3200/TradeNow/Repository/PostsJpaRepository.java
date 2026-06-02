package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsJpaRepository extends JpaRepository<Posts, Integer >
{
    List<Posts> findByCategoryIdAndZoneIdAndType(Integer categoryId, Integer zoneId, PublicationType type);
}

package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReputationJpaRepository extends JpaRepository<Rating, Integer>
{
    List<Rating> findByRatedIdOrderByCreatedAtDesc(Integer ratedId);

    Optional<Rating> findByTradeIdAndRaterId(Integer tradeId, Integer raterId);
}


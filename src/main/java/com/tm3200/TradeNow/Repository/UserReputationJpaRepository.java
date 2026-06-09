package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Rating;
import com.tm3200.TradeNow.Model.UserReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReputationJpaRepository extends JpaRepository<UserReputation, Integer>
{
    Optional<UserReputation> findByUserId(Integer userId);
}


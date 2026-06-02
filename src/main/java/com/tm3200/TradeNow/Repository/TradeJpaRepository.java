package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeJpaRepository extends JpaRepository<Trade, Integer> {

    List<Trade> findByUser1OrUser2Id(Integer user1Id, Integer user2Id);




}

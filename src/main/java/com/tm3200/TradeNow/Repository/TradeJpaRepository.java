package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeJpaRepository extends JpaRepository<Trade, Integer> {

    List<Trade> findByUser1_IdOrUser2_Id(Integer user1Id, Integer user2Id);

    List<Trade> findByUser1_IdAndStatus(Integer user1Id, TradeStatus status);

    List<Trade> findByUser2_IdAndStatus(Integer user2Id, TradeStatus status);




}


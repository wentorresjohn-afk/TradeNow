package com.tm3200.TradeNow.Service;


import com.tm3200.TradeNow.Model.DTO.TradeConfirmDTO;
import com.tm3200.TradeNow.Model.DTO.TradeCreateDTO;
import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeJpaRepository tradeRepository;

    @Autowired
    private UserJpaRepository userRepository;

    public Trade createTrade(TradeCreateDTO dto) {
        Optional<User> optionalUser1 = userRepository.findById(dto.getUser1Id());
        if (!optionalUser1.isPresent()) {
            throw new RuntimeException("User1 not found");
        }

        Optional<User> optionalUser2 = userRepository.findById(dto.getUser2Id());
        if (!optionalUser2.isPresent()) {
            throw new RuntimeException("User2 not found");
        }

        Trade trade = new Trade();
        trade.setConditions(dto.getConditions());
        trade.setExchangeDate(dto.getExchangeDate());
        trade.setDeliveryMode(dto.getDeliveryMode());
        trade.setAgreement(dto.getAgreement());
        trade.setUser1(optionalUser1.get());
        trade.setUser2(optionalUser2.get());
        trade.setConfirmedByUser1(false);
        trade.setConfirmedByUser2(false);
        trade.setStatus(TradeStatus.ACTIVE);

        return tradeRepository.save(trade);
    }

    public Trade confirmDelivery(Integer tradeId, TradeConfirmDTO dto) {
        Optional<Trade> optionalTrade = tradeRepository.findById(tradeId);
        if (!optionalTrade.isPresent()) {
            throw new RuntimeException("Trade not found");
        }

        Trade trade = optionalTrade.get();

        if (trade.getStatus().equals(TradeStatus.COMPLETED)) {
            throw new RuntimeException("Trade is already completed");
        }
        if (dto.getUserId().equals(trade.getUser1().getId())) {
            trade.setConfirmedByUser1(true);
        } else if (dto.getUserId().equals(trade.getUser2().getId())) {
            trade.setConfirmedByUser2(true);
        } else {
            throw new RuntimeException("User is not part of this trade");
        }
        if (trade.getConfirmedByUser1() && trade.getConfirmedByUser2()) {
            trade.setStatus(TradeStatus.COMPLETED);
        }

        return tradeRepository.save(trade);
    }

    public List<Trade> getActiveTrades(Integer userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        List<Trade> tradesAsUser1 = tradeRepository.findByUser1_IdAndStatus(userId, TradeStatus.ACTIVE);
        List<Trade> tradesAsUser2 = tradeRepository.findByUser2_IdAndStatus(userId, TradeStatus.ACTIVE);

        List<Trade> allTrades = new ArrayList<>();
        allTrades.addAll(tradesAsUser1);
        allTrades.addAll(tradesAsUser2);

        return allTrades;
    }

    public Trade getTradeById(Integer tradeId) {
        Optional<Trade> optional = tradeRepository.findById(tradeId);
        if (!optional.isPresent()) {
            throw new RuntimeException("Trade not found");
        }
        return optional.get();
    }

}

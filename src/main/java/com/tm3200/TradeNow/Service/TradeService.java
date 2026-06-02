package com.tm3200.TradeNow.Service;


import com.tm3200.TradeNow.Model.DTO.TradeCreateDTO;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        trade.setStatus("ACTIVE");

        return tradeRepository.save(trade);
    }

}

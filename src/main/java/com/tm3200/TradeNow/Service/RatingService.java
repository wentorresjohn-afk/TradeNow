package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.RatingDTO;
import com.tm3200.TradeNow.Model.ProposalStatus;
import com.tm3200.TradeNow.Model.Rating;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Repository.RatingJpaRepository;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserReputationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService
{
    @Autowired
    private RatingJpaRepository ratingRepo;

    @Autowired
    private UserReputationJpaRepository reputationRepo;

    @Autowired
    private TradeJpaRepository tradeRepo;

    public Rating addRating(RatingDTO dto)
    {
        // Validar que el trueque existe y está completado
        Trade trade = tradeRepo.findById(dto.getTradeId()).orElse(null);
        if (trade == null || trade.getStatus() != ProposalStatus.ACCEPTED) {
            return null;
        }
    }
}

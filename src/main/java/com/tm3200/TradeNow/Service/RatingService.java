package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.RatingDTO;
import com.tm3200.TradeNow.Model.Enum.RatingRole;
import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import com.tm3200.TradeNow.Model.Rating;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Model.UserReputation;
import com.tm3200.TradeNow.Repository.RatingJpaRepository;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserReputationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (trade == null || trade.getStatus() != TradeStatus.COMPLETED) {
            return null;
        }

        // Evitar calificación duplicada
        if (ratingRepo.findByTradeIdAndRaterId(dto.getTradeId(), dto.getRaterId()).isPresent()) {
            return null;
        }

        Rating rating = new Rating();
        rating.setTradeId(dto.getTradeId());
        rating.setRaterId(dto.getRaterId());
        rating.setRatedId(dto.getRatedId());
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());
        rating.setRaterRole(RatingRole.valueOf(dto.getRaterRole().toUpperCase()));

        Rating saved = ratingRepo.save(rating);

        recalculateReputation(dto.getRatedId());

        return saved;
    }//Fin del metodo


    private void recalculateReputation(Integer userId) {
        List<Rating> ratings = ratingRepo.findByRatedIdOrderByCreatedAtDesc(userId);

        if (ratings.isEmpty()) return;

        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (int i = 0; i < ratings.size(); i++) {
            double weight = Math.pow(0.9, i);
            weightedSum += ratings.get(i).getScore() * weight;
            totalWeight += weight;
        }

        double weightedScore = Math.round((weightedSum / totalWeight) * 100.0) / 100.0;

        UserReputation reputation = reputationRepo.findByUserId(userId)
                .orElse(new UserReputation());

        reputation.setUserId(userId);
        reputation.setWeightedScore(weightedScore);
        reputation.setTotalRatings(ratings.size());

        reputationRepo.save(reputation);
    }

    public UserReputation getReputation(Integer userId) {
        return reputationRepo.findByUserId(userId).orElse(null);
    }
}

package com.tm3200.TradeNow.Repository;

import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalJpaRepository extends JpaRepository<Proposal,Integer> {

    List<Proposal> findByTargetPostsId(Posts targetPublicationId);
    List<Proposal> findBySenderId(User senderId);

}

package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.*;
import com.tm3200.TradeNow.Model.DTO.ProposalCreateDTO;
import com.tm3200.TradeNow.Model.DTO.ProposalHistoryDTO;
import com.tm3200.TradeNow.Model.DTO.ProposalResponseDTO;
import com.tm3200.TradeNow.Model.Enum.ProposalStatus;
import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import com.tm3200.TradeNow.Repository.PostsJpaRepository;
import com.tm3200.TradeNow.Repository.ProposalJpaRepository;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Model.Enum.ProposalStatus;
import com.tm3200.TradeNow.Model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProposalService {
    @Autowired
    private ProposalJpaRepository proposalRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PostsJpaRepository postsRepository;

    @Autowired
    private TradeJpaRepository tradeRepository;

    //enviar propuestas de trueque
    public Proposal sendProposal(ProposalCreateDTO dto){
        Optional<User> optionalSender = userRepository.findById(dto.getSenderId());
        if (optionalSender.isEmpty()) {
            throw new RuntimeException("Sender not found");
        }

        Optional<Posts> optionalTarget = postsRepository.findById(dto.getTargetPublicationId());
        if (optionalTarget.isEmpty()) {
            throw new RuntimeException("Target publication not found");
        }

        Optional<Posts> optionalOffered = postsRepository.findById(dto.getOfferedPublicationId());
        if (optionalOffered.isEmpty()) {
            throw new RuntimeException("Offered publication not found");
        }

        Proposal proposal = new Proposal();
        proposal.setSenderId(optionalSender.get());
        proposal.setTargetPublicationId(optionalTarget.get());
        proposal.setOfferedPublicationId(optionalOffered.get());
        proposal.setStatus(ProposalStatus.PENDING);
        proposal.setCreatedAt(LocalDate.now());
        return proposalRepository.save(proposal);
    }

    public Proposal respondProposal(Integer proposalId, ProposalResponseDTO dto) {
        Optional<Proposal> optional = proposalRepository.findById(proposalId);
        if (optional.isEmpty()) {
            throw new RuntimeException("Proposal not found");
        }

        Proposal proposal = optional.get();

        if (!proposal.getStatus().equals(ProposalStatus.PENDING)) {
            throw new RuntimeException("Proposal is no longer pending");
        }

        if (dto.getStatus().equals(ProposalStatus.COUNTERED)) {
            if (dto.getCounterOfferedPublicationId() == null) {
                throw new RuntimeException("Counter offer publication is required");
            }
            Optional<Posts> optionalCounter = postsRepository.findById(dto.getCounterOfferedPublicationId());
            if (optionalCounter.isEmpty()) {
                throw new RuntimeException("Counter offer publication not found");
            }

            Proposal counterProposal = new Proposal();
            counterProposal.setSenderId(proposal.getTargetPublicationId().getUser());
            counterProposal.setTargetPublicationId(proposal.getOfferedPublicationId());
            counterProposal.setOfferedPublicationId(optionalCounter.get());
            counterProposal.setStatus(ProposalStatus.PENDING);
            counterProposal.setCreatedAt(LocalDate.now());
            proposalRepository.save(counterProposal);

            proposal.setCounterProposalId(counterProposal);
        }
        if (dto.getStatus().equals(ProposalStatus.ACCEPTED)) {
            Trade trade = new Trade();
            trade.setUser1(proposal.getSenderId());
            trade.setUser2(proposal.getTargetPublicationId().getUser());
            trade.setConditions("Proposal #" + proposal.getId() + " accepted");
            trade.setExchangeDate(LocalDate.now());
            trade.setDeliveryMode("To be defined");
            trade.setAgreement("Both parties agreed via proposal");
            trade.setConfirmedByUser1(false);
            trade.setConfirmedByUser2(false);
            trade.setStatus(TradeStatus.ACTIVE);
            tradeRepository.save(trade);
        }
        proposal.setStatus(dto.getStatus());
        return proposalRepository.save(proposal);
    }

    public List<ProposalHistoryDTO> getSentProposals(Integer userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        List<Proposal> proposals = proposalRepository.findBySenderId(optional.get());
        List<ProposalHistoryDTO> result = new ArrayList<>();

        for (Proposal p : proposals) {
            ProposalHistoryDTO dto = new ProposalHistoryDTO(
                    p.getId(),
                    p.getSenderId().getName(),
                    p.getTargetPublicationId().getTitle(),
                    p.getOfferedPublicationId().getTitle(),
                    p.getStatus(),
                    p.getCreatedAt()
            );
            result.add(dto);
        }

        return result;
    }

    // RF11 - Ver propuestas recibidas
    public List<ProposalHistoryDTO> getReceivedProposals(Integer userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        List<Posts> posts = postsRepository.findByUser(optional.get());
        List<ProposalHistoryDTO> result = new ArrayList<>();

        for (Posts post : posts) {
            List<Proposal> proposals = proposalRepository.findByTargetPublicationId(post);
            for (Proposal p : proposals) {
                ProposalHistoryDTO dto = new ProposalHistoryDTO(
                        p.getId(),
                        p.getSenderId().getName(),
                        p.getTargetPublicationId().getTitle(),
                        p.getOfferedPublicationId().getTitle(),
                        p.getStatus(),
                        p.getCreatedAt()
                );
                result.add(dto);
            }
        }
        return result;
    }
}

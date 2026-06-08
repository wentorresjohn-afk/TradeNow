package com.tm3200.TradeNow.Model;

import com.tm3200.TradeNow.Model.Enum.ProposalStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb-proposals")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User senderId;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Posts targetPublicationId;
    @ManyToOne
    @JoinColumn(name = "offered_id")
    //referencia a la publicación a la que le llega la propuesta
    private Posts offeredPublicationId;
    //lo que el emisor ofrece a cambio
    private ProposalStatus status;
    @ManyToOne
    @JoinColumn(name = "counter_id")
    private Proposal counterProposalId;
    private LocalDate createdAt;

    public Proposal() {
    }

    public Proposal(Integer id, User senderId, Posts targetPublicationId, Posts offeredPublicationId, ProposalStatus status, Proposal counterProposalId, LocalDate createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.targetPublicationId = targetPublicationId;
        this.offeredPublicationId = offeredPublicationId;
        this.status = status;
        this.counterProposalId = counterProposalId;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }

    public Posts getTargetPublicationId() {
        return targetPublicationId;
    }

    public void setTargetPublicationId(Posts targetPublicationId) {
        this.targetPublicationId = targetPublicationId;
    }

    public Posts getOfferedPublicationId() {
        return offeredPublicationId;
    }

    public void setOfferedPublicationId(Posts offeredPublicationId) {
        this.offeredPublicationId = offeredPublicationId;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public Proposal getCounterProposalId() {
        return counterProposalId;
    }

    public void setCounterProposalId(Proposal counterProposalId) {
        this.counterProposalId = counterProposalId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

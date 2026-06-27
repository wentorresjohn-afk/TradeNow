package com.tm3200.TradeNow.Model.DTO;

import com.tm3200.TradeNow.Model.Enum.ProposalStatus;

import java.time.LocalDate;

public class ProposalHistoryDTO {
    private Integer id;
    private String senderName;
    private String targetPublicationTitle;
    private String offeredPublicationTitle;
    private ProposalStatus status;
    private LocalDate createdAt;

    public ProposalHistoryDTO() {
    }

    public ProposalHistoryDTO(Integer id, String senderName, String targetPublicationTitle, String offeredPublicationTitle, ProposalStatus status, LocalDate createdAt) {
        this.id = id;
        this.senderName = senderName;
        this.targetPublicationTitle = targetPublicationTitle;
        this.offeredPublicationTitle = offeredPublicationTitle;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTargetPublicationTitle() {
        return targetPublicationTitle;
    }

    public void setTargetPublicationTitle(String targetPublicationTitle) {
        this.targetPublicationTitle = targetPublicationTitle;
    }

    public String getOfferedPublicationTitle() {
        return offeredPublicationTitle;
    }

    public void setOfferedPublicationTitle(String offeredPublicationTitle) {
        this.offeredPublicationTitle = offeredPublicationTitle;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

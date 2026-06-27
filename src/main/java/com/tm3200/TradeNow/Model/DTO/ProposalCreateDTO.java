package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotNull;

public class ProposalCreateDTO {
    @NotNull(message = "Sender is required/Se requiere el remitente")
    private Integer senderId;
    @NotNull(message = "Target publication is required/Se requiere publicación de destino")
    private Integer targetPublicationId;
    @NotNull(message = "Offered publication is required/Se requiere la publicación que ofrece")
    private Integer offeredPublicationId;

    public ProposalCreateDTO() {
    }

    public ProposalCreateDTO(Integer senderId, Integer targetPublicationId, Integer offeredPublicationId) {
        this.senderId = senderId;
        this.targetPublicationId = targetPublicationId;
        this.offeredPublicationId = offeredPublicationId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getTargetPublicationId() {
        return targetPublicationId;
    }

    public void setTargetPublicationId(Integer targetPublicationId) {
        this.targetPublicationId = targetPublicationId;
    }

    public Integer getOfferedPublicationId() {
        return offeredPublicationId;
    }

    public void setOfferedPublicationId(Integer offeredPublicationId) {
        this.offeredPublicationId = offeredPublicationId;
    }
}

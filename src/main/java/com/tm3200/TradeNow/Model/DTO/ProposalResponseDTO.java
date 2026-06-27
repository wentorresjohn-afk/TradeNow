package com.tm3200.TradeNow.Model.DTO;

import com.tm3200.TradeNow.Model.Enum.ProposalStatus;
import jakarta.validation.constraints.NotNull;

public class ProposalResponseDTO {
    @NotNull(message = "Status is required/Se requiere estado")
    private ProposalStatus status;
    //este no lleva notnull porque es opcional, solo se necesita si hay una contra oferta
    private Integer counterOfferedPublicationId;

    public ProposalResponseDTO() {
    }

    public ProposalResponseDTO(ProposalStatus status, Integer counterOfferedPublicationId) {
        this.status = status;
        this.counterOfferedPublicationId = counterOfferedPublicationId;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public Integer getCounterOfferedPublicationId() {
        return counterOfferedPublicationId;
    }

    public void setCounterOfferedPublicationId(Integer counterOfferedPublicationId) {
        this.counterOfferedPublicationId = counterOfferedPublicationId;
    }
}

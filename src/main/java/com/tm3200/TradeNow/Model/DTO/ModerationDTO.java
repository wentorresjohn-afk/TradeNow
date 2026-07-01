package com.tm3200.TradeNow.Model.DTO;

import com.tm3200.TradeNow.Model.Enum.PublicationStatus;
import jakarta.validation.constraints.NotNull;

public class ModerationDTO
{
    @NotNull(message = "El status es obligatorio")
    private PublicationStatus status;

    @NotNull(message = "El moderatorId es obligatorio")
    private Integer moderatorId;

    public ModerationDTO() {
    }

    public ModerationDTO(PublicationStatus status, Integer moderatorId) {
        this.status = status;
        this.moderatorId = moderatorId;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        this.status = status;
    }

    public Integer getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(Integer moderatorId) {
        this.moderatorId = moderatorId;
    }
}

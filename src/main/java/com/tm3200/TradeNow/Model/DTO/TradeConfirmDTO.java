package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotNull;

public class TradeConfirmDTO {

    @NotNull(message = "User id is required")
    private Integer userId;

    public TradeConfirmDTO() {
    }

    public TradeConfirmDTO(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

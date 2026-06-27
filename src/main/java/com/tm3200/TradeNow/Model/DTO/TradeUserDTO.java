package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotNull;

public class TradeUserDTO {

    @NotNull(message = "User id is required")
    private Integer userId;

    public TradeUserDTO() {
    }

    public TradeUserDTO(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

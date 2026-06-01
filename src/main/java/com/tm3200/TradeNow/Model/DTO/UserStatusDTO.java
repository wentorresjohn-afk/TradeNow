package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserStatusDTO {

    @NotNull(message = "Status is required")
    private Boolean active;


    public UserStatusDTO() {
    }

    public UserStatusDTO(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

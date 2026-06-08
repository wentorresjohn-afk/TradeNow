package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TradeCreateDTO {

    @NotBlank(message = "Conditions are required")
    private String conditions;

    @NotBlank(message = "Exchange date is required")
    private String exchangeDate;

    @NotBlank(message = "Delivery mode is required")
    private String deliveryMode;

    @NotBlank(message = "Agreement is required")
    private String agreement;

    @NotNull(message = "User1 is required")
    private Integer user1Id;

    @NotNull(message = "User2 is required")
    private Integer user2Id;

    public TradeCreateDTO() {
    }

    public TradeCreateDTO(String conditions, String exchangeDate, String deliveryMode,
                          String agreement, Integer user1Id, Integer user2Id) {
        this.conditions = conditions;
        this.exchangeDate = exchangeDate;
        this.deliveryMode = deliveryMode;
        this.agreement = agreement;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public Integer getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Integer user1Id) {
        this.user1Id = user1Id;
    }

    public Integer getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Integer user2Id) {
        this.user2Id = user2Id;
    }
}

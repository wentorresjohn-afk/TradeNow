package com.tm3200.TradeNow.Model.DTO;

import com.tm3200.TradeNow.Model.Enum.PublicationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PostsDTO
{
    @NotNull(message = "El tipo es obligatorio")
    private PublicationType type;
    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private BigDecimal estimatedValue;
    private String exchangeFor;

    public PostsDTO()
    {
    }

    public PostsDTO(PublicationType type, String title, String description, BigDecimal estimatedValue, String exchangeFor) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.estimatedValue = estimatedValue;
        this.exchangeFor = exchangeFor;
    }

    public PublicationType getType() {
        return type;
    }

    public void setType(PublicationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getExchangeFor() {
        return exchangeFor;
    }

    public void setExchangeFor(String exchangeFor) {
        this.exchangeFor = exchangeFor;
    }
}

package com.tm3200.TradeNow.Model.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PostsDTO
{
    @NotNull(message = "El tipo es obligatorio")
    @JsonProperty("type")
    private PublicationType type;
    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    private Double estimatedValue;
    private String exchangeFor;

    private Integer userId;
    private Integer categoryId;
    private Integer zoneId;
    private String imageUrl;

    public PostsDTO()
    {
    }

    public PostsDTO(PublicationType type, String title, String description, Double estimatedValue, String exchangeFor, Integer userId, Integer categoryId, Integer zoneId, String imageUrl) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.estimatedValue = estimatedValue;
        this.exchangeFor = exchangeFor;
        this.userId = userId;
        this.categoryId = categoryId;
        this.zoneId = zoneId;
        this.imageUrl = imageUrl;
    }
    // ESTO ES LO QUE SOLUCIONA EL ERROR 400:
    @JsonCreator
    public static PublicationType fromString(String value) {
        return PublicationType.valueOf(value.toUpperCase());
    }

    // Mantenemos este setter único y funcional
    public void setType(Object value) {
        if (value instanceof String) {
            // Si llega como String (JSON), lo convertimos al Enum
            this.type = PublicationType.valueOf(((String) value).toUpperCase());
        } else if (value instanceof PublicationType) {
            // Si ya llega como Enum, lo asignamos directo
            this.type = (PublicationType) value;
        }
    }

    public PublicationType getType() {
        return type;
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


    public Double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public String getExchangeFor() {
        return exchangeFor;
    }

    public void setExchangeFor(String exchangeFor) {
        this.exchangeFor = exchangeFor;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

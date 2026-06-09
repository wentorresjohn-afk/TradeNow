package com.tm3200.TradeNow.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RatingDTO
{
    @NotNull(message = "El ID del trueque es obligatorio")
    private Integer tradeId;

    @NotNull(message = "El ID del calificador es obligatorio")
    private Integer raterId;

    @NotNull(message = "El ID del calificado es obligatorio")
    private Integer ratedId;

    @NotNull(message = "El puntaje es obligatorio")
    @Min(value = 1, message = "El puntaje mínimo es 1")
    @Max(value = 5, message = "El puntaje máximo es 5")
    private Integer score;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comment;

    @NotNull(message = "El rol del calificador es obligatorio")
    private String raterRole;

    public RatingDTO() {
    }

    public RatingDTO(Integer tradeId, Integer raterId, Integer ratedId, Integer score, String comment, String raterRole) {
        this.tradeId = tradeId;
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.score = score;
        this.comment = comment;
        this.raterRole = raterRole;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getRaterId() {
        return raterId;
    }

    public void setRaterId(Integer raterId) {
        this.raterId = raterId;
    }

    public Integer getRatedId() {
        return ratedId;
    }

    public void setRatedId(Integer ratedId) {
        this.ratedId = ratedId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRaterRole() {
        return raterRole;
    }

    public void setRaterRole(String raterRole) {
        this.raterRole = raterRole;
    }
}

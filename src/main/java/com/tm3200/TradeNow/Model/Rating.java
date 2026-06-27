package com.tm3200.TradeNow.Model;

import com.tm3200.TradeNow.Model.Enum.RatingRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ratings")
public class Rating
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "trade_id", nullable = false)//Mapea el campo exacto y no permite que quede vacío
    private Integer tradeId;//Guarda el id del trueque al que pertenece la calificación
    //Se conecta con módulo 4

    @Column(name = "rater_id", nullable = false)//Mapea el campo exacto y no permite que quede vacío
    private Integer raterId;//Id del user que está calificando

    @Column(name = "rated_id", nullable = false)//Mapea el campo exacto y no permite que quede vacío
    private Integer ratedId;//Id del user que está siendo calificado

    @Column(nullable = false)//Hace que el valor solo se pueda registrar y no modificar despues
    private Integer score;//Puntaje de la calificacióm

    @Column(length = 500)//Define el campo maximo del texto
    private String comment;//Comentario opcional de la calificación

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RatingRole raterRole;//Indica si quien califica fue el que ofreció (OFFERER) o el que solicitó (REQUESTER) en el trueque.

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)//garantiza que ese valor se escribe una sola vez al crear el registro y nunca se puede modificar después.
    private LocalDateTime createdAt;//Fecha y hora exacta en que se registró la calificación. La usa el servicio para ordenar de más reciente a más antigua

    public Rating() {
    }

    public Rating(Integer id, Integer tradeId, Integer raterId, Integer ratedId, Integer score, String comment, RatingRole raterRole, LocalDateTime createdAt) {
        this.id = id;
        this.tradeId = tradeId;
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.score = score;
        this.comment = comment;
        this.raterRole = raterRole;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public RatingRole getRaterRole() {
        return raterRole;
    }

    public void setRaterRole(RatingRole raterRole) {
        this.raterRole = raterRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

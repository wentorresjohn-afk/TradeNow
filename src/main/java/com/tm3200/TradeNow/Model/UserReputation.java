package com.tm3200.TradeNow.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reputations")
public class UserReputation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true)//No puede ser nulo ni haber dos filas con el mismo id
    private Integer userId;//Id del usuario al que pertenece la reputacióny

    @Column(name = "weighted_score", nullable = false)//no puede ser nul
    private Double weightedScore = 0.0;//Puntaje ya calculado, crece cada que el user recibe una nueva calificación

    @Column(name = "total_ratings", nullable = false)//no puede ser nulo
    private Integer totalRatings = 0;//Cuantas calificaciones ha recibido el user

    @UpdateTimestamp// Hibernate actualiza automáticamente este campo con la fecha y hora actual cada vez que se modifica el registro.
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;//Fecha y hora de la última vez que se recalculó la reputación del usuario.

    public UserReputation() {
    }


    public UserReputation(Integer userId, Double weightedScore, Integer totalRatings) {
        this.userId = userId;
        this.weightedScore = weightedScore;
        this.totalRatings = totalRatings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getWeightedScore() {
        return weightedScore;
    }

    public void setWeightedScore(Double weightedScore) {
        this.weightedScore = weightedScore;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

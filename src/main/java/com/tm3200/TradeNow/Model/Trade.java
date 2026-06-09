package com.tm3200.TradeNow.Model;

import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String conditions;
    private LocalDate exchangeDate;
    private String deliveryMode;
    private String agreement;

    private Boolean confirmedByUser1;
    private Boolean confirmedByUser2;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    public Trade() {
    }

    public Trade(Integer id, String conditions, LocalDate exchangeDate,
                 String deliveryMode, String agreement, Boolean confirmedByUser1,
                 Boolean confirmedByUser2, TradeStatus status, User user1, User user2) {
        this.id = id;
        this.conditions = conditions;
        this.exchangeDate = exchangeDate;
        this.deliveryMode = deliveryMode;
        this.agreement = agreement;
        this.confirmedByUser1 = confirmedByUser1;
        this.confirmedByUser2 = confirmedByUser2;
        this.status = status;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public LocalDate getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDate exchangeDate) {
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

    public Boolean getConfirmedByUser1() {
        return confirmedByUser1;
    }

    public void setConfirmedByUser1(Boolean confirmedByUser1) {
        this.confirmedByUser1 = confirmedByUser1;
    }

    public Boolean getConfirmedByUser2() {
        return confirmedByUser2;
    }

    public void setConfirmedByUser2(Boolean confirmedByUser2) {
        this.confirmedByUser2 = confirmedByUser2;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}

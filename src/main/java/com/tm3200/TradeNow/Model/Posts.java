package com.tm3200.TradeNow.Model;

import com.tm3200.TradeNow.Model.Enum.PublicationStatus;
import com.tm3200.TradeNow.Model.Enum.PublicationType;
import com.tm3200.TradeNow.Model.PostsEntitys.Category;
import com.tm3200.TradeNow.Model.PostsEntitys.Zone;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_posts")
public class Posts
{
    @Id//Hace que no pueda repetirse el id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Va incrementando el id
    private Integer id;
    private PublicationType type;
    private String title;
    private String description;
    private BigDecimal estimatedValue;
    private String exchangeFor;
    private PublicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;


    public Posts()
    {
    }

    public Posts(Integer id, PublicationType type, String title, String description, BigDecimal estimatedValue, String exchangeFor, PublicationStatus status, Category category, Zone zone) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.estimatedValue = estimatedValue;
        this.exchangeFor = exchangeFor;
        this.status = status;
        this.category = category;
        this.zone = zone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}

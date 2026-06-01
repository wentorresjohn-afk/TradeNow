package com.tm3200.TradeNow.Model;

import jakarta.persistence.*;

@Entity
@Table(name="app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  name;
    private String email;
    private String password;
    private String geographicZone;

    private String photo;
    private String description;

    private Double averageRating;
    private Integer completedTrades;

    private UserType userType;
    private Boolean active;

    public User() {
    }

    public User(Integer id, String name, String email, String password,
                String geographicZone, String photo, String description,
                Double averageRating, Integer completedTrades, UserType userType,
                Boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.geographicZone = geographicZone;
        this.photo = photo;
        this.description = description;
        this.averageRating = averageRating;
        this.completedTrades = completedTrades;
        this.userType = userType;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGeographicZone() {
        return geographicZone;
    }

    public void setGeographicZone(String geographicZone) {
        this.geographicZone = geographicZone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getCompletedTrades() {
        return completedTrades;
    }

    public void setCompletedTrades(Integer completedTrades) {
        this.completedTrades = completedTrades;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

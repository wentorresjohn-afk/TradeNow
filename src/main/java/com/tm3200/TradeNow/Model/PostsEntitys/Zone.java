package com.tm3200.TradeNow.Model.PostsEntitys;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_zone")
public class Zone
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cityName;
    private String state;
    private String country;

    public Zone()
    {
    }

    public Zone(Integer id, String cityName, String state, String country) {
        this.id = id;
        this.cityName = cityName;
        this.state = state;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

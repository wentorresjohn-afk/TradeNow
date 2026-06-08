package com.tm3200.TradeNow.Model.DTO;

public class ZoneReportDTO {
    private String cityName;
    private Integer count;

    public ZoneReportDTO() {
    }

    public ZoneReportDTO(String cityName, Integer count) {
        this.cityName = cityName;
        this.count = count;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

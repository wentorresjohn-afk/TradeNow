package com.tm3200.TradeNow.Model.DTO;

public class CategoryReportDTO {
    private String categoryName;
    private Integer count;

    public CategoryReportDTO() {
    }

    public CategoryReportDTO(String categoryName, Integer count) {
        this.categoryName = categoryName;
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

package com.tm3200.TradeNow.Model.DTO;

import java.math.BigDecimal;

public class TotalValueReportDTO {
    private BigDecimal totalValue;

    public TotalValueReportDTO() {
    }

    public TotalValueReportDTO(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
}

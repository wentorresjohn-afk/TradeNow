package com.tm3200.TradeNow.Model.DTO;

public class ActiveUserReportDTO {
    private String userName;
    private Integer proposalCount;

    public ActiveUserReportDTO() {
    }

    public ActiveUserReportDTO(String userName, Integer proposalCount) {
        this.userName = userName;
        this.proposalCount = proposalCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getProposalCount() {
        return proposalCount;
    }

    public void setProposalCount(Integer proposalCount) {
        this.proposalCount = proposalCount;
    }
}

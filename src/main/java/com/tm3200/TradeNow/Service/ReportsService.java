package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.ActiveUserReportDTO;
import com.tm3200.TradeNow.Model.DTO.CategoryReportDTO;
import com.tm3200.TradeNow.Model.DTO.TotalValueReportDTO;
import com.tm3200.TradeNow.Model.DTO.ZoneReportDTO;
import com.tm3200.TradeNow.Model.Posts;
import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Repository.ProposalJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsService {
    @Autowired
    private ProposalJpaRepository proposalRepository;

    // RF17 - Categorías más intercambiadas
    public List<CategoryReportDTO> getCategoryReport(LocalDate startDate, LocalDate endDate) {
        List<Proposal> proposals = proposalRepository.findByCreatedAtBetween(startDate, endDate);
        List<CategoryReportDTO> result = new ArrayList<>();

        for (Proposal p : proposals) {
            String targetCategory = p.getTargetPublicationId().getCategory().getName();
            String offeredCategory = p.getOfferedPublicationId().getCategory().getName();

            addOrIncrementCategory(result, targetCategory);
            addOrIncrementCategory(result, offeredCategory);
        }

        return result;
    }

    private void addOrIncrementCategory(List<CategoryReportDTO> list, String categoryName) {
        for (CategoryReportDTO dto : list) {
            if (dto.getCategoryName().equals(categoryName)) {
                dto.setCount(dto.getCount() + 1);
                return;
            }
        }
        list.add(new CategoryReportDTO(categoryName, 1));
    }

    // RF18 - Usuarios más activos
    public List<ActiveUserReportDTO> getActiveUsersReport() {
        List<Proposal> proposals = proposalRepository.findAll();
        List<ActiveUserReportDTO> result = new ArrayList<>();

        for (Proposal p : proposals) {
            String userName = p.getSenderId().getName();
            addOrIncrementUser(result, userName);
        }

        return result;
    }

    private void addOrIncrementUser(List<ActiveUserReportDTO> list, String userName) {
        for (ActiveUserReportDTO dto : list) {
            if (dto.getUserName().equals(userName)) {
                dto.setProposalCount(dto.getProposalCount() + 1);
                return;
            }
        }
        list.add(new ActiveUserReportDTO(userName, 1));
    }

    // RF18 - Trueques por zona
    public List<ZoneReportDTO> getZoneReport() {
        List<Proposal> proposals = proposalRepository.findAll();
        List<ZoneReportDTO> result = new ArrayList<>();

        for (Proposal p : proposals) {
            String cityName = p.getTargetPublicationId().getZone().getCityName();
            addOrIncrementZone(result, cityName);
        }

        return result;
    }

    private void addOrIncrementZone(List<ZoneReportDTO> list, String cityName) {
        for (ZoneReportDTO dto : list) {
            if (dto.getCityName().equals(cityName)) {
                dto.setCount(dto.getCount() + 1);
                return;
            }
        }
        list.add(new ZoneReportDTO(cityName, 1));
    }

    // RF18 - Valor total intercambiado
    public TotalValueReportDTO getTotalValue() {
        List<Proposal> proposals = proposalRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;

        for (Proposal p : proposals) {
            Posts target = p.getTargetPublicationId();
            Posts offered = p.getOfferedPublicationId();

            if (target.getEstimatedValue() != null) {
                total = total.add(target.getEstimatedValue());
            }
            if (offered.getEstimatedValue() != null) {
                total = total.add(offered.getEstimatedValue());
            }
        }

        return new TotalValueReportDTO(total);
    }
}

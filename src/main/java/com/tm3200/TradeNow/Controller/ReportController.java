package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.DTO.ActiveUserReportDTO;
import com.tm3200.TradeNow.Model.DTO.CategoryReportDTO;
import com.tm3200.TradeNow.Model.DTO.TotalValueReportDTO;
import com.tm3200.TradeNow.Model.DTO.ZoneReportDTO;
import com.tm3200.TradeNow.Service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private ReportsService reportService;

    // RF17 - Categorías más intercambiadas
    @GetMapping("/reportes/categorias")
    public ResponseEntity<?> getCategoryReport(@RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<CategoryReportDTO> result = reportService.getCategoryReport(start, end);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // RF18 - Usuarios más activos
    @GetMapping("/reportes/usuarios-activos")
    public ResponseEntity<?> getActiveUsersReport() {
        try {
            List<ActiveUserReportDTO> result = reportService.getActiveUsersReport();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // RF18 - Trueques por zona
    @GetMapping("/reportes/por-zona")
    public ResponseEntity<?> getZoneReport() {
        try {
            List<ZoneReportDTO> result = reportService.getZoneReport();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // RF18 - Valor total intercambiado
    @GetMapping("/reportes/valor-total")
    public ResponseEntity<?> getTotalValue() {
        try {
            TotalValueReportDTO result = reportService.getTotalValue();
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

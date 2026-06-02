package com.tm3200.TradeNow.Controller;


import com.tm3200.TradeNow.Model.DTO.TradeCreateDTO;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Service.TradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/trades")
    public ResponseEntity<?> createTrade(@Valid @RequestBody TradeCreateDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            Trade trade = tradeService.createTrade(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(trade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}

package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.DTO.RatingDTO;
import com.tm3200.TradeNow.Model.Rating;
import com.tm3200.TradeNow.Model.UserReputation;
import com.tm3200.TradeNow.Service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RatingController
{
    @Autowired
    private RatingService ratingService;

    // RF15: Calificar a la contraparte tras un trueque
    @PostMapping("/calificaciones")
    public ResponseEntity<?> addRating(@Valid @RequestBody RatingDTO dto) {
        Rating rating = ratingService.addRating(dto);

        if (rating == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("No se puede calificar: el trueque no está completado, o ya fue calificado por este usuario.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    // RF16: Ver reputación acumulada de un usuario
    @GetMapping("/usuarios/{id}/reputacion")
    public ResponseEntity<?> getReputation(@PathVariable Integer id) {
        UserReputation reputation = ratingService.getReputation(id);

        if (reputation == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El usuario no tiene reputación registrada aún.");
        }

        return ResponseEntity.ok(reputation);
    }
}

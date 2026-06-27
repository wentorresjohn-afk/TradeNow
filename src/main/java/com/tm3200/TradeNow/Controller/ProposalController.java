package com.tm3200.TradeNow.Controller;

import com.tm3200.TradeNow.Model.DTO.ProposalCreateDTO;
import com.tm3200.TradeNow.Model.DTO.ProposalHistoryDTO;
import com.tm3200.TradeNow.Model.DTO.ProposalResponseDTO;
import com.tm3200.TradeNow.Model.Proposal;
import com.tm3200.TradeNow.Service.ProposalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    //RF 9: El sistema debe permitir al usuario enviar una propuesta de trueque a otra
    //publicación, indicando qué ofrece a cambio.
    @PostMapping("/propuestas")
    public ResponseEntity<?> sendProposal(@Valid @RequestBody ProposalCreateDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Proposal proposal = proposalService.sendProposal(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(proposal);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //RF 10: El sistema debe permitir al receptor de una propuesta aceptarla, rechazarla o
    //enviar una contraoferta.
    @PutMapping("/propuestas/{id}/responder")
    public ResponseEntity<?> respondProposal(@PathVariable("id") Integer id,
                                             @Valid @RequestBody ProposalResponseDTO dto,
                                             BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Proposal proposal = proposalService.respondProposal(id, dto);
            return ResponseEntity.ok(proposal);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //RF 11: El sistema debe registrar y mostrar el historial de PROPUESTAS ENVIADAS     y
    //recibidas con su estado actual.
    @GetMapping("/propuestas/enviadas")
    public ResponseEntity<?> getSentProposals(@RequestParam Integer userId) {
        try {
            List<ProposalHistoryDTO> proposals = proposalService.getSentProposals(userId);
            return ResponseEntity.ok(proposals);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //RF 11: El sistema debe registrar y mostrar el historial de propuestas enviadas y
    //RECIBIDAS con su estado actual.
    @GetMapping("/propuestas/recibidas")
    public ResponseEntity<?> getReceivedProposals(@RequestParam Integer userId) {
        try {
            List<ProposalHistoryDTO> proposals = proposalService.getReceivedProposals(userId);
            return ResponseEntity.ok(proposals);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

package it.unicam.hackhub.api.controller;

import it.unicam.hackhub.api.dto.CreaTeamRequest;
import it.unicam.hackhub.api.dto.TeamResponse;
import it.unicam.hackhub.application.team.TeamService;
import it.unicam.hackhub.domain.team.Team;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Punto di ingresso REST per i team. */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /** Caso d'uso "Crea team". */
    @PostMapping
    public ResponseEntity<TeamResponse> crea(@Valid @RequestBody CreaTeamRequest richiesta) {
        Team creato = teamService.crea(richiesta.nome(), richiesta.idCreatore());
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.da(creato));
    }

    /** Aggiunge un membro a un team esistente. */
    @PostMapping("/{id}/membri/{idUtente}")
    public TeamResponse aggiungiMembro(@PathVariable UUID id, @PathVariable UUID idUtente) {
        return TeamResponse.da(teamService.aggiungiMembro(id, idUtente));
    }
}

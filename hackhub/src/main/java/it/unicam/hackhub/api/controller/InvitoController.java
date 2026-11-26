package it.unicam.hackhub.api.controller;

import it.unicam.hackhub.api.dto.AccettaInvitoRequest;
import it.unicam.hackhub.api.dto.InvitaUtenteRequest;
import it.unicam.hackhub.api.dto.InvitoResponse;
import it.unicam.hackhub.api.dto.TeamResponse;
import it.unicam.hackhub.application.invito.InvitoService;
import it.unicam.hackhub.domain.invito.Invito;
import it.unicam.hackhub.domain.team.Team;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Punto di ingresso REST per gli inviti nei team.
 *
 * Come gli altri controller, traduce fra HTTP e livello applicativo e
 * lascia la gestione degli errori al gestore centralizzato.
 */
@RestController
public class InvitoController {

    private final InvitoService invitoService;

    public InvitoController(InvitoService invitoService) {
        this.invitoService = invitoService;
    }

    /** Caso d'uso "Invita utente nel team". */
    @PostMapping("/api/team/{idTeam}/inviti")
    public ResponseEntity<InvitoResponse> invitaUtente(
            @PathVariable UUID idTeam,
            @Valid @RequestBody InvitaUtenteRequest richiesta) {

        Invito invito = invitoService.invita(
                idTeam, richiesta.idMittente(), richiesta.emailDestinatario());

        return ResponseEntity.status(HttpStatus.CREATED).body(InvitoResponse.da(invito));
    }

    /** Caso d'uso "Accetta invito nel team". */
    @PostMapping("/api/inviti/{idInvito}/accetta")
    public TeamResponse accettaInvito(
            @PathVariable UUID idInvito,
            @Valid @RequestBody AccettaInvitoRequest richiesta) {

        Team team = invitoService.accetta(idInvito, richiesta.idUtente());
        return TeamResponse.da(team);
    }
}

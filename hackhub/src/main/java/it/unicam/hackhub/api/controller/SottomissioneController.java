package it.unicam.hackhub.api.controller;

import it.unicam.hackhub.api.dto.InviaSottomissioneRequest;
import it.unicam.hackhub.api.dto.SottomissioneResponse;
import it.unicam.hackhub.api.dto.ValutaSottomissioneRequest;
import it.unicam.hackhub.api.dto.ValutazioneResponse;
import it.unicam.hackhub.application.sottomissione.SottomissioneService;
import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Punto di ingresso REST per sottomissioni e valutazioni.
 *
 * Gli endpoint sono annidati sotto l'hackathon perche' una sottomissione
 * esiste solo nel contesto di un evento.
 */
@RestController
@RequestMapping("/api/hackathons/{idHackathon}")
public class SottomissioneController {

    private final SottomissioneService sottomissioneService;

    public SottomissioneController(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    /** Caso d'uso "Invia sottomissione". */
    @PostMapping("/sottomissioni")
    public ResponseEntity<SottomissioneResponse> invia(
            @PathVariable UUID idHackathon,
            @Valid @RequestBody InviaSottomissioneRequest richiesta) {

        Sottomissione inviata = sottomissioneService.invia(
                idHackathon, richiesta.idTeam(), richiesta.titolo(), richiesta.contenuto());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SottomissioneResponse.da(inviata));
    }

    /** Caso d'uso "Valuta sottomissione". */
    @PostMapping("/valutazioni")
    public ResponseEntity<ValutazioneResponse> valuta(
            @PathVariable UUID idHackathon,
            @Valid @RequestBody ValutaSottomissioneRequest richiesta) {

        Valutazione valutazione = sottomissioneService.valuta(
                idHackathon, richiesta.idTeam(), richiesta.idGiudice(),
                richiesta.giudizio(), richiesta.punteggio());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ValutazioneResponse.da(valutazione));
    }
}

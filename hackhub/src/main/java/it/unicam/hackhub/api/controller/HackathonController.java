package it.unicam.hackhub.api.controller;

import it.unicam.hackhub.api.dto.CreaHackathonRequest;
import it.unicam.hackhub.api.dto.HackathonResponse;
import it.unicam.hackhub.application.hackathon.DatiHackathon;
import it.unicam.hackhub.application.hackathon.HackathonService;
import it.unicam.hackhub.domain.hackathon.Hackathon;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Punto di ingresso REST per gli hackathon.
 *
 * Il controller traduce fra protocollo HTTP e livello applicativo: converte
 * il DTO in oggetto parametro, chiama il service e trasforma il risultato in
 * DTO di risposta. Non contiene regole di business, cosi' da evitare il
 * cosiddetto "fat controller".
 *
 * La gestione degli errori non e' qui: le eccezioni sono tradotte in codici
 * HTTP dal gestore centralizzato.
 */
@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    /** Caso d'uso "Crea hackathon". */
    @PostMapping
    public ResponseEntity<HackathonResponse> crea(@Valid @RequestBody CreaHackathonRequest richiesta) {
        DatiHackathon dati = new DatiHackathon(
                richiesta.nome(), richiesta.regolamento(), richiesta.scadenzaIscrizioni(),
                richiesta.dataInizio(), richiesta.dataFine(), richiesta.luogo(),
                richiesta.premio(), richiesta.dimensioneMaxTeam(),
                richiesta.idOrganizzatore(), richiesta.idGiudice(), richiesta.idMentori());

        Hackathon creato = hackathonService.crea(dati);
        return ResponseEntity.status(HttpStatus.CREATED).body(HackathonResponse.da(creato));
    }

    /** Elenco di tutti gli hackathon. */
    @GetMapping
    public List<HackathonResponse> elenco() {
        return hackathonService.trovaTutti().stream()
                .map(HackathonResponse::da)
                .toList();
    }

    /** Dettagli di un singolo hackathon. */
    @GetMapping("/{id}")
    public HackathonResponse dettagli(@PathVariable UUID id) {
        return HackathonResponse.da(hackathonService.trovaPerId(id));
    }

    /** Caso d'uso "Iscrivi team a hackathon". */
    @PostMapping("/{id}/team/{idTeam}")
    public HackathonResponse iscriviTeam(@PathVariable UUID id, @PathVariable UUID idTeam) {
        return HackathonResponse.da(hackathonService.iscriviTeam(id, idTeam));
    }

    /**
     * Fa avanzare l'hackathon alla fase successiva.
     * Non e' un caso d'uso del documento dei requisiti, ma serve a rendere
     * osservabile la macchina a stati e a poter provare gli altri endpoint.
     */
    @PostMapping("/{id}/avanza")
    public HackathonResponse avanzaFase(@PathVariable UUID id) {
        return HackathonResponse.da(hackathonService.avanzaFase(id));
    }
}

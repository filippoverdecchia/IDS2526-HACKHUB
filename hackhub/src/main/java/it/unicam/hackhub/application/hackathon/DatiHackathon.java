package it.unicam.hackhub.application.hackathon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Oggetto parametro con i dati necessari a creare un hackathon.
 *
 * Risolve due problemi in una volta:
 * - il costruttore di Hackathon ha undici parametri, che e' uno smell:
 *   raggrupparli in un unico oggetto rende le chiamate leggibili;
 * - il livello applicativo non deve conoscere i DTO del boundary REST:
 *   il controller traduce la propria richiesta in questo record, cosi'
 *   il service non dipende da HTTP.
 *
 * E' un record: immutabile, senza logica, con i soli dati.
 */
public record DatiHackathon(
        String nome,
        String regolamento,
        LocalDate scadenzaIscrizioni,
        LocalDate dataInizio,
        LocalDate dataFine,
        String luogo,
        BigDecimal premio,
        int dimensioneMaxTeam,
        UUID idOrganizzatore,
        UUID idGiudice,
        List<UUID> idMentori
) {
}

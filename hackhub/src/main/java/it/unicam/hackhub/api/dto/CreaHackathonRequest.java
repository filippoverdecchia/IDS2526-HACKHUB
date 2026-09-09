package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Dati in ingresso per la creazione di un hackathon.
 *
 * Qui si valida solo il FORMATO: campi obbligatori, numeri positivi, liste
 * non vuote. La coerenza fra i dati (le date in ordine, il nome non gia'
 * usato, i ruoli dello staff) e' una regola di dominio e viene verificata
 * dal service.
 */
public record CreaHackathonRequest(

        @NotBlank(message = "il nome e' obbligatorio")
        String nome,

        @NotBlank(message = "il regolamento e' obbligatorio")
        String regolamento,

        @NotNull(message = "la scadenza delle iscrizioni e' obbligatoria")
        LocalDate scadenzaIscrizioni,

        @NotNull(message = "la data di inizio e' obbligatoria")
        LocalDate dataInizio,

        @NotNull(message = "la data di fine e' obbligatoria")
        LocalDate dataFine,

        @NotBlank(message = "il luogo e' obbligatorio")
        String luogo,

        @NotNull @Positive(message = "il premio deve essere positivo")
        BigDecimal premio,

        @Min(value = 1, message = "la dimensione massima del team deve essere almeno 1")
        int dimensioneMaxTeam,

        @NotNull(message = "l'organizzatore e' obbligatorio")
        UUID idOrganizzatore,

        @NotNull(message = "il giudice e' obbligatorio")
        UUID idGiudice,

        @NotEmpty(message = "e' richiesto almeno un mentore")
        List<UUID> idMentori
) {
}

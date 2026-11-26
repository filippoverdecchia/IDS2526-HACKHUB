package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** Dati in ingresso per accettare un invito. */
public record AccettaInvitoRequest(

        @NotNull(message = "l'utente e' obbligatorio")
        UUID idUtente
) {
}

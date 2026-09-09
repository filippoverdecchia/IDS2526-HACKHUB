package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** Dati in ingresso per l'invio di una sottomissione. */
public record InviaSottomissioneRequest(

        @NotNull(message = "il team e' obbligatorio")
        UUID idTeam,

        @NotBlank(message = "il titolo e' obbligatorio")
        String titolo,

        @NotBlank(message = "il contenuto e' obbligatorio")
        String contenuto
) {
}

package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

/** Dati in ingresso per la valutazione di una sottomissione. */
public record ValutaSottomissioneRequest(

        @NotNull(message = "il team e' obbligatorio")
        UUID idTeam,

        @NotNull(message = "il giudice e' obbligatorio")
        UUID idGiudice,

        @NotBlank(message = "il giudizio e' obbligatorio")
        String giudizio,

        @Min(value = 0, message = "il punteggio minimo e' 0")
        @Max(value = 10, message = "il punteggio massimo e' 10")
        int punteggio
) {
}

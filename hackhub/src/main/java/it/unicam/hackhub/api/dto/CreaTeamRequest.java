package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** Dati in ingresso per la creazione di un team. */
public record CreaTeamRequest(

        @NotBlank(message = "il nome del team e' obbligatorio")
        String nome,

        @NotNull(message = "il creatore e' obbligatorio")
        UUID idCreatore
) {
}

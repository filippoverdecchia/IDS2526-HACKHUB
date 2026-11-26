package it.unicam.hackhub.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/** Dati in ingresso per invitare un utente in un team. */
public record InvitaUtenteRequest(

        @NotNull(message = "il mittente e' obbligatorio")
        UUID idMittente,

        @NotBlank(message = "l'email del destinatario e' obbligatoria")
        @Email(message = "l'email del destinatario non e' valida")
        String emailDestinatario
) {
}

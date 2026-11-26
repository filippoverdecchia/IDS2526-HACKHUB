package it.unicam.hackhub.api.dto;

import it.unicam.hackhub.domain.invito.Invito;

import java.time.LocalDateTime;
import java.util.UUID;

/** Dati di un invito esposti al client. */
public record InvitoResponse(
        UUID id,
        UUID idTeam,
        String nomeTeam,
        UUID idMittente,
        String mittente,
        UUID idDestinatario,
        String destinatario,
        String stato,
        LocalDateTime data
) {

    public static InvitoResponse da(Invito invito) {
        return new InvitoResponse(
                invito.getId(),
                invito.getTeam().getId(),
                invito.getTeam().getNome(),
                invito.getMittente().getId(),
                invito.getMittente().getNome(),
                invito.getDestinatario().getId(),
                invito.getDestinatario().getNome(),
                invito.getStato().name(),
                invito.getData());
    }
}

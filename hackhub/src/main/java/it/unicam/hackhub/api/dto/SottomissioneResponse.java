package it.unicam.hackhub.api.dto;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;

import java.time.LocalDateTime;
import java.util.UUID;

/** Dati di una sottomissione esposti al client. */
public record SottomissioneResponse(
        UUID id,
        String titolo,
        String contenuto,
        LocalDateTime dataUltimoAggiornamento,
        boolean valutata
) {

    public static SottomissioneResponse da(Sottomissione sottomissione) {
        return new SottomissioneResponse(
                sottomissione.getId(),
                sottomissione.getTitolo(),
                sottomissione.getContenuto(),
                sottomissione.getDataUltimoAggiornamento(),
                sottomissione.isValutata());
    }
}

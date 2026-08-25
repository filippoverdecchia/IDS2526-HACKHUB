package it.unicam.hackhub.api.dto;

import it.unicam.hackhub.domain.sottomissione.Valutazione;

import java.util.UUID;

/** Dati di una valutazione esposti al client. */
public record ValutazioneResponse(
        UUID id,
        String giudizio,
        int punteggio,
        String giudice
) {

    public static ValutazioneResponse da(Valutazione valutazione) {
        return new ValutazioneResponse(
                valutazione.getId(),
                valutazione.getGiudizio(),
                valutazione.getPunteggio(),
                valutazione.getAutore().getNome());
    }
}

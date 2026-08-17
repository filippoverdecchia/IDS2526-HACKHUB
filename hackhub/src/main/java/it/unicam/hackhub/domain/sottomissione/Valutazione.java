package it.unicam.hackhub.domain.sottomissione;

import it.unicam.hackhub.domain.staff.Giudice;

import java.util.UUID;

/**
 * Giudizio espresso da un Giudice su una sottomissione.
 *
 * E' una classe e non un attributo di Sottomissione perche' ha dati propri
 * (giudizio e punteggio) e un autore: quando un candidato attributo ha a sua
 * volta attributi e relazioni, e' un'entita'.
 */
public class Valutazione {

    /** Punteggio minimo ammesso. */
    public static final int PUNTEGGIO_MINIMO = 0;

    /** Punteggio massimo ammesso. */
    public static final int PUNTEGGIO_MASSIMO = 10;

    private final UUID id;
    private final String giudizio;
    private final int punteggio;
    private final Giudice autore;

    public Valutazione(String giudizio, int punteggio, Giudice autore) {
        if (giudizio == null || giudizio.isBlank()) {
            throw new IllegalArgumentException("Il giudizio e' obbligatorio");
        }
        if (punteggio < PUNTEGGIO_MINIMO || punteggio > PUNTEGGIO_MASSIMO) {
            throw new IllegalArgumentException(
                    "Il punteggio deve essere compreso tra " + PUNTEGGIO_MINIMO
                            + " e " + PUNTEGGIO_MASSIMO);
        }
        if (autore == null) {
            throw new IllegalArgumentException("La valutazione deve avere un giudice");
        }
        this.id = UUID.randomUUID();
        this.giudizio = giudizio;
        this.punteggio = punteggio;
        this.autore = autore;
    }

    public UUID getId() { return id; }

    public String getGiudizio() { return giudizio; }

    public int getPunteggio() { return punteggio; }

    public Giudice getAutore() { return autore; }
}

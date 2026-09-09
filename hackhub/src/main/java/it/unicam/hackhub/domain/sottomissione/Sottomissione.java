package it.unicam.hackhub.domain.sottomissione;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Elaborato prodotto da un team per un hackathon.
 *
 * Puo' essere aggiornata finche' l'hackathon e' in corso e riceve al massimo
 * una valutazione. E' la sottomissione a sapere se e' gia' stata valutata,
 * quindi e' lei a impedire una seconda valutazione (Information Expert).
 */
public class Sottomissione {

    private final UUID id;
    private String titolo;
    private String contenuto;
    private LocalDateTime dataUltimoAggiornamento;
    private Valutazione valutazione;

    public Sottomissione(String titolo, String contenuto) {
        validaContenuti(titolo, contenuto);
        this.id = UUID.randomUUID();
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    /**
     * Sostituisce titolo e contenuto e aggiorna la data di modifica.
     * Il controllo sulla fase dell'hackathon non avviene qui: e' lo stato
     * dell'hackathon a stabilire se l'aggiornamento e' ancora consentito.
     */
    public void aggiorna(String titolo, String contenuto) {
        validaContenuti(titolo, contenuto);
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.dataUltimoAggiornamento = LocalDateTime.now();
    }

    /**
     * Registra la valutazione ricevuta.
     *
     * @throws IllegalStateException se la sottomissione e' gia' stata valutata
     */
    public void assegnaValutazione(Valutazione valutazione) {
        if (valutazione == null) {
            throw new IllegalArgumentException("La valutazione non puo' essere nulla");
        }
        if (this.valutazione != null) {
            throw new IllegalStateException("La sottomissione e' gia' stata valutata");
        }
        this.valutazione = valutazione;
    }

    public boolean isValutata() {
        return valutazione != null;
    }

    private void validaContenuti(String titolo, String contenuto) {
        if (titolo == null || titolo.isBlank()) {
            throw new IllegalArgumentException("Il titolo e' obbligatorio");
        }
        if (contenuto == null || contenuto.isBlank()) {
            throw new IllegalArgumentException("Il contenuto e' obbligatorio");
        }
    }

    public UUID getId() { return id; }

    public String getTitolo() { return titolo; }

    public String getContenuto() { return contenuto; }

    public LocalDateTime getDataUltimoAggiornamento() { return dataUltimoAggiornamento; }

    /** Vuoto finche' la sottomissione non e' stata valutata. */
    public Optional<Valutazione> getValutazione() {
        return Optional.ofNullable(valutazione);
    }
}

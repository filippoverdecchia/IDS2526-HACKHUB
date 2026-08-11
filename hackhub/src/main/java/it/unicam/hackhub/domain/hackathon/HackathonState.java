package it.unicam.hackhub.domain.hackathon;

/**
 * Ruolo "State" del pattern State (GoF).
 * Ogni stato concreto incapsula CIO' CHE E' PERMESSO nella sua fase:
 * grazie a questa interfaccia, nel resto del progetto non serve
 * nessun if/switch sulla fase dell'hackathon.
 *
 * L'interfaccia CRESCE con i casi d'uso dell'Iterazione 1:
 *  - ora: sola transizione di fase;
 *  - con Team: iscriviTeam(...) permessa solo in IN_ISCRIZIONE;
 *  - con Sottomissione: aggiungiSottomissione(...) solo in IN_CORSO;
 *  - con Valutazione: valutaSottomissione(...) solo in IN_VALUTAZIONE.
 */
public interface HackathonState {

    /** Porta l'hackathon alla fase successiva, se la transizione e' consentita. */
    void passaAlProssimoStato();

    /** Tipo enumerato corrispondente a questo stato. */
    HackathonStateType tipo();
}

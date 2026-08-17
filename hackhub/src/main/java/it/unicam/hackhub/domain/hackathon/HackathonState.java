package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.team.Team;

/**
 * Ruolo "State" del pattern State (GoF).
 *
 * Ogni stato concreto incapsula CIO' CHE E' PERMESSO nella sua fase: grazie a
 * questa interfaccia, nel resto del progetto non serve nessun if o switch
 * sulla fase dell'hackathon.
 *
 * Corrispondenza tra fase e operazione consentita:
 *   IN_ISCRIZIONE  -> iscriviTeam
 *   IN_CORSO       -> aggiungiSottomissione
 *   IN_VALUTAZIONE -> valutaSottomissione
 *   CONCLUSO       -> nessuna
 */
public interface HackathonState {

    /** Porta l'hackathon alla fase successiva, se consentito. */
    void passaAlProssimoStato();

    /** Iscrive un team all'hackathon. Consentito solo in fase di iscrizione. */
    void iscriviTeam(Team team);

    /** Registra la sottomissione di un team. Consentito solo a hackathon in corso. */
    void aggiungiSottomissione(Team team, Sottomissione sottomissione);

    /** Assegna una valutazione alla sottomissione di un team. Consentito solo in valutazione. */
    void valutaSottomissione(Team team, Valutazione valutazione);

    /** Tipo enumerato corrispondente a questo stato. */
    HackathonStateType tipo();
}

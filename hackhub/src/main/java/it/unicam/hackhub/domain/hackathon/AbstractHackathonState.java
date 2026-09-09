package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.team.Team;

/**
 * Base comune degli stati concreti.
 *
 * Due scelte progettuali:
 *
 * 1) tiene un riferimento al Context (l'Hackathon), cosi' ogni stato puo'
 *    consultare date, scadenze e team iscritti senza riceverli come parametro;
 *
 * 2) OGNI OPERAZIONE E' VIETATA PER DEFAULT: uno stato concreto fa override
 *    solo di cio' che consente. Lo stato finale non fa alcun override e
 *    blocca tutto senza una riga di codice in piu'.
 *
 * Il divieto e' segnalato con IllegalStateException perche' i dati passati
 * sono corretti: e' l'hackathon a trovarsi nella fase sbagliata. Il gestore
 * degli errori la tradurra' in una risposta 409 Conflict.
 */
public abstract class AbstractHackathonState implements HackathonState {

    protected final Hackathon hackathon;

    protected AbstractHackathonState(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public void passaAlProssimoStato() {
        throw operazioneNonPermessa("avanzare di fase");
    }

    @Override
    public void iscriviTeam(Team team) {
        throw operazioneNonPermessa("iscrivere un team");
    }

    @Override
    public void aggiungiSottomissione(Team team, Sottomissione sottomissione) {
        throw operazioneNonPermessa("inviare una sottomissione");
    }

    @Override
    public void valutaSottomissione(Team team, Valutazione valutazione) {
        throw operazioneNonPermessa("valutare una sottomissione");
    }

    /** Messaggio uniforme per tutte le operazioni vietate nella fase corrente. */
    private IllegalStateException operazioneNonPermessa(String operazione) {
        return new IllegalStateException(
                "Non e' possibile " + operazione
                        + ": l'hackathon si trova nella fase " + tipo());
    }
}

package it.unicam.hackhub.domain.hackathon;

/**
 * Base comune degli stati concreti.
 *
 * Due scelte progettuali:
 * 1) tiene un riferimento al Context (l'Hackathon), cosi' ogni stato puo'
 *    consultare date, scadenze e team senza riceverli come parametro;
 * 2) ogni operazione e' VIETATA per default: uno stato concreto fa override
 *    solo di cio' che consente. Lo stato finale non fa alcun override
 *    e blocca tutto senza una riga in piu'.
 */
public abstract class AbstractHackathonState implements HackathonState {

    protected final Hackathon hackathon;

    protected AbstractHackathonState(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public void passaAlProssimoStato() {
        throw new IllegalStateException(
                "Operazione non permessa nello stato " + tipo());
    }
}

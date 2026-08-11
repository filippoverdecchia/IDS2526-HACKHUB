package it.unicam.hackhub.domain.hackathon;

/**
 * Fase di valutazione: il giudice esamina le sottomissioni.
 * Avanza a CONCLUSO.
 */
public class EvaluationState extends AbstractHackathonState {

    public EvaluationState(Hackathon hackathon) { super(hackathon); }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new EndedState(hackathon));
    }

    @Override
    public HackathonStateType tipo() { return HackathonStateType.IN_VALUTAZIONE; }
}

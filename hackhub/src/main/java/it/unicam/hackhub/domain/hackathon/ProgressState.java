package it.unicam.hackhub.domain.hackathon;

/**
 * Hackathon in corso: i team lavorano e inviano le sottomissioni.
 * Avanza a IN_VALUTAZIONE.
 */
public class ProgressState extends AbstractHackathonState {

    public ProgressState(Hackathon hackathon) { super(hackathon); }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new EvaluationState(hackathon));
    }

    @Override
    public HackathonStateType tipo() { return HackathonStateType.IN_CORSO; }
}

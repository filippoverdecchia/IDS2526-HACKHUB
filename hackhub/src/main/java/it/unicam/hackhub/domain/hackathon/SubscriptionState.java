package it.unicam.hackhub.domain.hackathon;

/**
 * Fase di iscrizione: i team possono registrarsi all'hackathon.
 * Avanza a IN_CORSO.
 */
public class SubscriptionState extends AbstractHackathonState {

    public SubscriptionState(Hackathon hackathon) { super(hackathon); }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new ProgressState(hackathon));
    }

    @Override
    public HackathonStateType tipo() { return HackathonStateType.IN_ISCRIZIONE; }
}

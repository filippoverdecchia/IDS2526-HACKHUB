package it.unicam.hackhub.domain.hackathon;

/**
 * Fase finale: nessuna operazione consentita.
 * Non fa override di nulla: eredita da AbstractHackathonState
 * il divieto su tutte le operazioni.
 */
public class EndedState extends AbstractHackathonState {

    public EndedState(Hackathon hackathon) { super(hackathon); }

    @Override
    public HackathonStateType tipo() { return HackathonStateType.CONCLUSO; }
}

package it.unicam.hackhub.domain.hackathon;

/**
 * Fase finale: nessuna operazione consentita.
 *
 * Non fa override di nulla: eredita da AbstractHackathonState il divieto su
 * tutte le operazioni, incluso l'avanzamento di fase. E' il punto in cui il
 * pattern State ripaga l'investimento, perche' il comportamento corretto
 * si ottiene senza scrivere codice.
 */
public class EndedState extends AbstractHackathonState {

    public EndedState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public HackathonStateType tipo() {
        return HackathonStateType.CONCLUSO;
    }
}

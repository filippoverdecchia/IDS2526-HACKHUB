package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.team.Team;

/**
 * Hackathon in corso: i team lavorano e inviano le sottomissioni.
 * Unica operazione consentita oltre alla transizione: aggiungiSottomissione.
 */
public class ProgressState extends AbstractHackathonState {

    public ProgressState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new EvaluationState(hackathon));
    }

    /**
     * Registra la sottomissione di un team iscritto.
     * Se il team ha gia' sottomesso, la sottomissione esistente viene
     * aggiornata: il requisito prevede la modifica fino al termine dei lavori.
     */
    @Override
    public void aggiungiSottomissione(Team team, Sottomissione sottomissione) {
        if (team == null || sottomissione == null) {
            throw new IllegalArgumentException("Team e sottomissione sono obbligatori");
        }
        if (!hackathon.isTeamIscritto(team)) {
            throw new IllegalArgumentException(
                    "Il team " + team.getNome() + " non e' iscritto a questo hackathon");
        }
        hackathon.sottomissioneDi(team).ifPresentOrElse(
                esistente -> esistente.aggiorna(sottomissione.getTitolo(), sottomissione.getContenuto()),
                () -> hackathon.associaSottomissione(team, sottomissione));
    }

    @Override
    public HackathonStateType tipo() {
        return HackathonStateType.IN_CORSO;
    }
}

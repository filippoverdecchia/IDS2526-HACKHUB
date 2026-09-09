package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.team.Team;

/**
 * Fase di valutazione: il giudice esamina le sottomissioni ricevute.
 * Unica operazione consentita oltre alla transizione: valutaSottomissione.
 */
public class EvaluationState extends AbstractHackathonState {

    public EvaluationState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new EndedState(hackathon));
    }

    /**
     * Assegna una valutazione alla sottomissione di un team, verificando che
     * il giudizio provenga dal giudice assegnato a questo hackathon.
     */
    @Override
    public void valutaSottomissione(Team team, Valutazione valutazione) {
        if (team == null || valutazione == null) {
            throw new IllegalArgumentException("Team e valutazione sono obbligatori");
        }
        if (!hackathon.isTeamIscritto(team)) {
            throw new IllegalArgumentException(
                    "Il team " + team.getNome() + " non e' iscritto a questo hackathon");
        }
        if (!valutazione.getAutore().getId().equals(hackathon.getGiudice().getId())) {
            throw new IllegalArgumentException(
                    "Solo il giudice assegnato all'hackathon puo' valutare le sottomissioni");
        }
        hackathon.associaValutazione(team, valutazione);
    }

    @Override
    public HackathonStateType tipo() {
        return HackathonStateType.IN_VALUTAZIONE;
    }
}

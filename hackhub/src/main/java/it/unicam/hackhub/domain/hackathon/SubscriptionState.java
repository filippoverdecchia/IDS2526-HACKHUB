package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.team.Team;

import java.time.LocalDate;

/**
 * Fase di iscrizione: i team possono registrarsi all'hackathon.
 * Unica operazione consentita oltre alla transizione: iscriviTeam.
 */
public class SubscriptionState extends AbstractHackathonState {

    public SubscriptionState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void passaAlProssimoStato() {
        hackathon.cambiaStato(new ProgressState(hackathon));
    }

    /**
     * Iscrive un team, dopo aver verificato le regole della fase:
     * scadenza non superata, team non gia' iscritto, dimensione consentita.
     */
    @Override
    public void iscriviTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Il team da iscrivere non puo' essere nullo");
        }
        if (LocalDate.now().isAfter(hackathon.getScadenzaIscrizioni())) {
            throw new IllegalStateException(
                    "Le iscrizioni si sono chiuse il " + hackathon.getScadenzaIscrizioni());
        }
        if (hackathon.isTeamIscritto(team)) {
            throw new IllegalArgumentException(
                    "Il team " + team.getNome() + " e' gia' iscritto a questo hackathon");
        }
        if (team.numeroMembri() > hackathon.getDimensioneMaxTeam()) {
            throw new IllegalArgumentException(
                    "Il team ha " + team.numeroMembri() + " membri, il massimo consentito e' "
                            + hackathon.getDimensioneMaxTeam());
        }
        hackathon.associaTeam(team);
    }

    @Override
    public HackathonStateType tipo() {
        return HackathonStateType.IN_ISCRIZIONE;
    }
}

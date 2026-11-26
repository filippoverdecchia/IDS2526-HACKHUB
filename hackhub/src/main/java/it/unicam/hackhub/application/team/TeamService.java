package it.unicam.hackhub.application.team;

import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.application.port.UtenteRepository;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;

import java.util.UUID;

/**
 * Logica del caso d'uso "Crea team".
 *
 * GRASP Controller: riceve la richiesta, coordina persistenza e dominio,
 * e non contiene regole di business proprie. Le regole sul team (un membro
 * non puo' essere aggiunto due volte, il nome e' obbligatorio) sono nella
 * classe Team, che possiede le informazioni per applicarle.
 *
 * I nuovi membri entrano nel team solo accettando un invito
 * (vedi InvitoService): non esiste un'operazione per aggiungerli
 * direttamente.
 *
 * Nessuna annotazione di framework: la classe e' Java puro e viene
 * registrata come componente Spring da una configurazione esterna.
 */
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public TeamService(TeamRepository teamRepository, UtenteRepository utenteRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un team con il nome indicato, di cui l'utente creatore diventa
     * il primo membro.
     *
     * @throws IllegalArgumentException se il creatore non esiste,
     *                                  se appartiene gia' a un team
     *                                  o se il nome e' gia' in uso
     */
    public Team crea(String nome, UUID idCreatore) {
        Utente creatore = utenteRepository.trovaPerId(idCreatore)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato: " + idCreatore));

        if (teamRepository.trovaPerMembro(creatore).isPresent()) {
            throw new IllegalArgumentException(
                    "L'utente " + creatore.getNome() + " appartiene gia' a un team");
        }

        if (teamRepository.esisteConNome(nome)) {
            throw new IllegalArgumentException(
                    "Esiste gia' un team con il nome: " + nome);
        }

        Team team = new Team(nome, creatore);
        return teamRepository.salva(team);
    }
}

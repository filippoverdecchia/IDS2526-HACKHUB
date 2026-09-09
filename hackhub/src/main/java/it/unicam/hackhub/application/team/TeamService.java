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
     * @throws IllegalArgumentException se il creatore non esiste
     *                                  o se il nome e' gia' in uso
     */
    public Team crea(String nome, UUID idCreatore) {
        Utente creatore = utenteRepository.trovaPerId(idCreatore)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato: " + idCreatore));

        if (teamRepository.esisteConNome(nome)) {
            throw new IllegalArgumentException(
                    "Esiste gia' un team con il nome: " + nome);
        }

        Team team = new Team(nome, creatore);
        return teamRepository.salva(team);
    }

    /**
     * Aggiunge un utente a un team esistente.
     * Il controllo sulla duplicazione dei membri e' delegato al Team.
     */
    public Team aggiungiMembro(UUID idTeam, UUID idUtente) {
        Team team = teamRepository.trovaPerId(idTeam)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Team non trovato: " + idTeam));

        Utente utente = utenteRepository.trovaPerId(idUtente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato: " + idUtente));

        team.aggiungiMembro(utente);
        return teamRepository.salva(team);
    }
}

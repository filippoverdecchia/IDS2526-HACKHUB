package it.unicam.hackhub.application.invito;

import it.unicam.hackhub.application.port.InvitoRepository;
import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.application.port.UtenteRepository;
import it.unicam.hackhub.domain.invito.Invito;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;

import java.util.UUID;

/**
 * Logica dei casi d'uso "Invita utente nel team" e "Accetta invito nel team".
 *
 * GRASP Controller: coordina repository e dominio senza contenere regole
 * proprie dell'invito. Le regole che dipendono dall'invito (stato,
 * destinatario) sono nella classe Invito; quelle che richiedono di
 * consultare la persistenza (un utente appartiene a un solo team, nessun
 * invito duplicato) sono qui, perche' solo il service vede i repository.
 *
 * Creator: il service crea l'Invito perche' possiede tutti i dati
 * necessari per inizializzarlo (team, mittente, destinatario).
 *
 * Il flusso dei metodi segue i sequence diagram SD_InvitaUtenteNelTeam
 * e SD_AccettaInvito.
 */
public class InvitoService {

    private final InvitoRepository invitoRepository;
    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public InvitoService(InvitoRepository invitoRepository,
                         TeamRepository teamRepository,
                         UtenteRepository utenteRepository) {
        this.invitoRepository = invitoRepository;
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Un membro del team invita un altro utente, indicato per email.
     *
     * @throws IllegalArgumentException se team, mittente o destinatario non
     *         esistono, se il mittente non fa parte del team, se il
     *         destinatario appartiene gia' a un team o ha gia' un invito
     *         pendente per questo team
     */
    public Invito invita(UUID idTeam, UUID idMittente, String emailDestinatario) {
        Team team = teamRepository.trovaPerId(idTeam)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Team non trovato: " + idTeam));

        Utente mittente = utenteRepository.trovaPerId(idMittente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato: " + idMittente));

        if (!team.contiene(mittente)) {
            throw new IllegalArgumentException(
                    "Solo un membro del team puo' invitare altri utenti");
        }

        Utente destinatario = utenteRepository.trovaPerEmail(emailDestinatario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nessun utente registrato con email: " + emailDestinatario));

        if (teamRepository.trovaPerMembro(destinatario).isPresent()) {
            throw new IllegalArgumentException(
                    "L'utente " + destinatario.getNome() + " appartiene gia' a un team");
        }

        if (invitoRepository.esistePendente(team, destinatario)) {
            throw new IllegalArgumentException(
                    "Esiste gia' un invito pendente per questo utente");
        }

        Invito invito = new Invito(team, mittente, destinatario);
        return invitoRepository.salva(invito);
    }

    /**
     * Il destinatario accetta l'invito ed entra nel team.
     *
     * I controlli seguono l'ordine delle guardie del sequence diagram
     * SD_AccettaInvito: prima lo stato dell'invito (409), poi
     * l'appartenenza a un altro team e il destinatario (400).
     *
     * @return il team aggiornato
     * @throws IllegalStateException    se l'invito non e' piu' pendente
     * @throws IllegalArgumentException se invito o utente non esistono, se
     *         l'utente appartiene gia' a un team o non e' il destinatario
     */
    public Team accetta(UUID idInvito, UUID idUtente) {
        Invito invito = invitoRepository.trovaPerId(idInvito)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invito non trovato: " + idInvito));

        Utente utente = utenteRepository.trovaPerId(idUtente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Utente non trovato: " + idUtente));

        boolean appartieneGiaAUnTeam = teamRepository.trovaPerMembro(utente).isPresent();

        if (!invito.isPendente()) {
            throw new IllegalStateException("L'invito non e' piu' pendente");
        }
        if (appartieneGiaAUnTeam) {
            throw new IllegalArgumentException(
                    "L'utente " + utente.getNome() + " appartiene gia' a un team");
        }

        Team team = invito.accetta(utente);
        teamRepository.salva(team);
        invitoRepository.salva(invito);
        return team;
    }
}

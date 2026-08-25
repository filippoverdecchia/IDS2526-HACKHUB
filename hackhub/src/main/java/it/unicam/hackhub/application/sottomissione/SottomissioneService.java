package it.unicam.hackhub.application.sottomissione;

import it.unicam.hackhub.application.port.HackathonRepository;
import it.unicam.hackhub.application.port.StaffRepository;
import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.domain.hackathon.Hackathon;
import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.MembroStaff;
import it.unicam.hackhub.domain.team.Team;

import java.util.UUID;

/**
 * Logica dei casi d'uso "Invia sottomissione" e "Valuta sottomissione".
 *
 * Come per l'iscrizione dei team, il service non verifica in quale fase si
 * trovi l'hackathon: si limita a caricare le entita' coinvolte e a chiamare
 * il dominio, che consente l'operazione solo se la fase la prevede.
 */
public class SottomissioneService {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final StaffRepository staffRepository;

    public SottomissioneService(HackathonRepository hackathonRepository,
                                TeamRepository teamRepository,
                                StaffRepository staffRepository) {
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
        this.staffRepository = staffRepository;
    }

    /**
     * Registra la sottomissione di un team.
     * Se il team ha gia' inviato un elaborato, questo viene aggiornato:
     * il requisito prevede la modifica fino al termine dei lavori.
     *
     * @throws IllegalStateException se l'hackathon non e' in corso
     */
    public Sottomissione invia(UUID idHackathon, UUID idTeam,
                               String titolo, String contenuto) {
        Hackathon hackathon = caricaHackathon(idHackathon);
        Team team = caricaTeam(idTeam);

        Sottomissione sottomissione = new Sottomissione(titolo, contenuto);
        hackathon.aggiungiSottomissione(team, sottomissione);
        hackathonRepository.salva(hackathon);

        return hackathon.sottomissioneDi(team).orElseThrow();
    }

    /**
     * Assegna una valutazione alla sottomissione di un team.
     * Il controllo che il giudizio provenga dal giudice assegnato
     * all'hackathon e' applicato dallo stato di valutazione.
     *
     * @throws IllegalStateException se l'hackathon non e' in fase di valutazione
     */
    public Valutazione valuta(UUID idHackathon, UUID idTeam, UUID idGiudice,
                              String giudizio, int punteggio) {
        Hackathon hackathon = caricaHackathon(idHackathon);
        Team team = caricaTeam(idTeam);
        Giudice giudice = caricaGiudice(idGiudice);

        Valutazione valutazione = new Valutazione(giudizio, punteggio, giudice);
        hackathon.valutaSottomissione(team, valutazione);
        hackathonRepository.salva(hackathon);

        return valutazione;
    }

    // ===== metodi di supporto =====

    private Hackathon caricaHackathon(UUID idHackathon) {
        return hackathonRepository.trovaPerId(idHackathon)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Hackathon non trovato: " + idHackathon));
    }

    private Team caricaTeam(UUID idTeam) {
        return teamRepository.trovaPerId(idTeam)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Team non trovato: " + idTeam));
    }

    private Giudice caricaGiudice(UUID idGiudice) {
        MembroStaff membro = staffRepository.trovaPerId(idGiudice)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Giudice non trovato: " + idGiudice));

        if (!(membro instanceof Giudice giudice)) {
            throw new IllegalArgumentException(
                    "Il membro dello staff " + idGiudice + " non ha il ruolo di Giudice");
        }
        return giudice;
    }
}

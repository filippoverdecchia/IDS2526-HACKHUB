package it.unicam.hackhub.application.hackathon;

import it.unicam.hackhub.application.port.HackathonRepository;
import it.unicam.hackhub.application.port.StaffRepository;
import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.domain.hackathon.Hackathon;
import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.MembroStaff;
import it.unicam.hackhub.domain.staff.Mentore;
import it.unicam.hackhub.domain.staff.Organizzatore;
import it.unicam.hackhub.domain.team.Team;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Logica dei casi d'uso "Crea hackathon" e "Iscrivi team a hackathon".
 *
 * Il service verifica la coerenza dei dati in ingresso e l'esistenza dei
 * membri dello staff, poi delega al dominio. In particolare non decide se
 * l'iscrizione di un team sia consentita: quella e' una regola che dipende
 * dalla fase dell'evento e appartiene allo stato dell'hackathon.
 */
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final TeamRepository teamRepository;

    public HackathonService(HackathonRepository hackathonRepository,
                            StaffRepository staffRepository,
                            TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Crea un nuovo hackathon, che nasce in fase di iscrizione.
     *
     * @throws IllegalArgumentException se i dati non sono coerenti,
     *                                  se il nome e' gia' in uso
     *                                  o se un membro dello staff non esiste
     *                                  o non ha il ruolo richiesto
     */
    public Hackathon crea(DatiHackathon dati) {
        validaDati(dati);

        Organizzatore organizzatore =
                caricaConRuolo(dati.idOrganizzatore(), Organizzatore.class, "Organizzatore");
        Giudice giudice =
                caricaConRuolo(dati.idGiudice(), Giudice.class, "Giudice");

        List<Mentore> mentori = new ArrayList<>();
        for (UUID idMentore : dati.idMentori()) {
            mentori.add(caricaConRuolo(idMentore, Mentore.class, "Mentore"));
        }

        Hackathon hackathon = new Hackathon(
                dati.nome(), dati.regolamento(), dati.scadenzaIscrizioni(),
                dati.dataInizio(), dati.dataFine(), dati.luogo(),
                dati.premio(), dati.dimensioneMaxTeam(),
                organizzatore, giudice, mentori);

        return hackathonRepository.salva(hackathon);
    }

    /**
     * Iscrive un team a un hackathon.
     * Le regole sull'ammissibilita' dell'iscrizione (scadenza, dimensione
     * del team, doppia iscrizione) sono applicate dallo stato corrente
     * dell'hackathon.
     */
    public Hackathon iscriviTeam(UUID idHackathon, UUID idTeam) {
        Hackathon hackathon = caricaHackathon(idHackathon);

        Team team = teamRepository.trovaPerId(idTeam)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Team non trovato: " + idTeam));

        hackathon.iscriviTeam(team);
        return hackathonRepository.salva(hackathon);
    }

    /** Fa avanzare l'hackathon alla fase successiva. */
    public Hackathon avanzaFase(UUID idHackathon) {
        Hackathon hackathon = caricaHackathon(idHackathon);
        hackathon.passaAlProssimoStato();
        return hackathonRepository.salva(hackathon);
    }

    public List<Hackathon> trovaTutti() {
        return hackathonRepository.trovaTutti();
    }

    public Hackathon trovaPerId(UUID idHackathon) {
        return caricaHackathon(idHackathon);
    }

    // ===== metodi di supporto =====

    private Hackathon caricaHackathon(UUID idHackathon) {
        return hackathonRepository.trovaPerId(idHackathon)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Hackathon non trovato: " + idHackathon));
    }

    /** Coerenza dei dati che nessun vincolo di formato puo' esprimere. */
    private void validaDati(DatiHackathon dati) {
        if (dati.nome() == null || dati.nome().isBlank()) {
            throw new IllegalArgumentException("Il nome dell'hackathon e' obbligatorio");
        }
        if (hackathonRepository.esisteConNome(dati.nome())) {
            throw new IllegalArgumentException(
                    "Esiste gia' un hackathon con il nome: " + dati.nome());
        }
        if (!dati.scadenzaIscrizioni().isBefore(dati.dataInizio())) {
            throw new IllegalArgumentException(
                    "La scadenza delle iscrizioni deve precedere la data di inizio");
        }
        if (!dati.dataInizio().isBefore(dati.dataFine())) {
            throw new IllegalArgumentException(
                    "La data di inizio deve precedere la data di fine");
        }
        if (dati.premio() == null || dati.premio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Il premio deve essere positivo");
        }
        if (dati.dimensioneMaxTeam() < 1) {
            throw new IllegalArgumentException(
                    "La dimensione massima del team deve essere almeno 1");
        }
        if (dati.idMentori() == null || dati.idMentori().isEmpty()) {
            throw new IllegalArgumentException("E' richiesto almeno un Mentore");
        }
    }

    /**
     * Carica un membro dello staff verificando che ricopra il ruolo atteso:
     * un giudice non puo' essere indicato al posto di un mentore.
     */
    private <T extends MembroStaff> T caricaConRuolo(UUID id, Class<T> tipo, String ruolo) {
        MembroStaff membro = staffRepository.trovaPerId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        ruolo + " non trovato: " + id));

        if (!tipo.isInstance(membro)) {
            throw new IllegalArgumentException(
                    "Il membro dello staff " + id + " non ha il ruolo di " + ruolo);
        }
        return tipo.cast(membro);
    }
}

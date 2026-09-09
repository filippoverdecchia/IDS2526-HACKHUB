package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.Mentore;
import it.unicam.hackhub.domain.staff.Organizzatore;
import it.unicam.hackhub.domain.team.Team;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Entita' centrale del dominio e "Context" del pattern State.
 *
 * Non decide da se' quali operazioni siano permesse: delega allo stato
 * corrente, che consente solo cio' che la sua fase prevede. I metodi
 * associaXxx sono richiamati dagli stati quando l'operazione e' consentita.
 */
public class Hackathon {

    private final UUID id;
    private final String nome;
    private final String regolamento;
    private final LocalDate scadenzaIscrizioni;
    private final LocalDate dataInizio;
    private final LocalDate dataFine;
    private final String luogo;
    private final BigDecimal premio;
    private final int dimensioneMaxTeam;

    private final Organizzatore organizzatore;
    private final Giudice giudice;
    private final List<Mentore> mentori;

    private final List<Team> teamIscritti = new ArrayList<>();
    private final Map<UUID, Sottomissione> sottomissioniPerTeam = new HashMap<>();

    private HackathonState stato;

    public Hackathon(String nome, String regolamento, LocalDate scadenzaIscrizioni,
                     LocalDate dataInizio, LocalDate dataFine, String luogo,
                     BigDecimal premio, int dimensioneMaxTeam,
                     Organizzatore organizzatore, Giudice giudice, List<Mentore> mentori) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.luogo = luogo;
        this.premio = premio;
        this.dimensioneMaxTeam = dimensioneMaxTeam;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
        this.mentori = List.copyOf(mentori);
        this.stato = new SubscriptionState(this); // ogni hackathon nasce in iscrizione
    }

    // ===== operazioni delegate allo stato corrente =====

    public void passaAlProssimoStato() {
        stato.passaAlProssimoStato();
    }

    public void iscriviTeam(Team team) {
        stato.iscriviTeam(team);
    }

    public void aggiungiSottomissione(Team team, Sottomissione sottomissione) {
        stato.aggiungiSottomissione(team, sottomissione);
    }

    public void valutaSottomissione(Team team, Valutazione valutazione) {
        stato.valutaSottomissione(team, valutazione);
    }

    public HackathonStateType tipoStato() {
        return stato.tipo();
    }

    // ===== operazioni riservate agli oggetti-stato (package-private) =====

    /** Cambio di fase: nessuna classe fuori dal package puo' forzare lo stato. */
    void cambiaStato(HackathonState nuovoStato) {
        this.stato = nuovoStato;
    }

    /** Registra un team fra gli iscritti. Richiamato da SubscriptionState. */
    void associaTeam(Team team) {
        teamIscritti.add(team);
    }

    /** Registra la sottomissione di un team. Richiamato da ProgressState. */
    void associaSottomissione(Team team, Sottomissione sottomissione) {
        sottomissioniPerTeam.put(team.getId(), sottomissione);
    }

    /** Assegna una valutazione alla sottomissione di un team. Richiamato da EvaluationState. */
    void associaValutazione(Team team, Valutazione valutazione) {
        Sottomissione sottomissione = sottomissioneDi(team)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Il team " + team.getNome() + " non ha inviato alcuna sottomissione"));
        sottomissione.assegnaValutazione(valutazione);
    }

    // ===== interrogazioni usate dagli stati =====

    /** Verifica se un team risulta iscritto a questo hackathon. */
    public boolean isTeamIscritto(Team team) {
        return teamIscritti.stream()
                .anyMatch(t -> t.getId().equals(team.getId()));
    }

    /** Sottomissione inviata da un team, vuota se il team non ha ancora sottomesso. */
    public Optional<Sottomissione> sottomissioneDi(Team team) {
        return Optional.ofNullable(sottomissioniPerTeam.get(team.getId()));
    }

    public int numeroTeamIscritti() {
        return teamIscritti.size();
    }

    // ===== getter =====

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getRegolamento() { return regolamento; }
    public LocalDate getScadenzaIscrizioni() { return scadenzaIscrizioni; }
    public LocalDate getDataInizio() { return dataInizio; }
    public LocalDate getDataFine() { return dataFine; }
    public String getLuogo() { return luogo; }
    public BigDecimal getPremio() { return premio; }
    public int getDimensioneMaxTeam() { return dimensioneMaxTeam; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudice; }
    public List<Mentore> getMentori() { return mentori; }

    public List<Team> getTeamIscritti() {
        return Collections.unmodifiableList(teamIscritti);
    }
}

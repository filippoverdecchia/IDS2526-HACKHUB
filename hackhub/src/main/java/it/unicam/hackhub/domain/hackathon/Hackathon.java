package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.Mentore;
import it.unicam.hackhub.domain.staff.Organizzatore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Entita' centrale del dominio e "Context" del pattern State:
 * delega allo stato corrente ogni comportamento che dipende dalla fase.
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

    /** Delega allo stato corrente la transizione di fase. */
    public void passaAlProssimoStato() {
        stato.passaAlProssimoStato();
    }

    /** Fase corrente, utile per le API e per la persistenza. */
    public HackathonStateType tipoStato() {
        return stato.tipo();
    }

    /**
     * Cambio di stato riservato agli oggetti-stato: essendo package-private,
     * nessuna classe fuori dal package puo' forzare la fase dell'hackathon.
     */
    void cambiaStato(HackathonState nuovoStato) {
        this.stato = nuovoStato;
    }

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
}

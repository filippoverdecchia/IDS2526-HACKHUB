package it.unicam.hackhub.domain.invito;

import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Invito, inviato da un membro di un team, a unirsi a quel team.
 *
 * Information Expert: l'invito conosce il proprio stato e il proprio
 * destinatario, quindi e' l'invito a decidere se puo' essere accettato e
 * da chi. Il service si limita a caricare gli oggetti e a salvarli.
 *
 * Creator: l'invito non crea il team ne' l'utente, li riferisce soltanto
 * (associazioni navigabili da Invito verso Team e Utente).
 */
public class Invito {

    private final UUID id;
    private final LocalDateTime data;
    private final Team team;
    private final Utente mittente;
    private final Utente destinatario;
    private StatoInvito stato;

    public Invito(Team team, Utente mittente, Utente destinatario) {
        if (team == null) {
            throw new IllegalArgumentException("L'invito deve riferirsi a un team");
        }
        if (mittente == null || destinatario == null) {
            throw new IllegalArgumentException("L'invito deve avere mittente e destinatario");
        }
        this.id = UUID.randomUUID();
        this.data = LocalDateTime.now();
        this.team = team;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.stato = StatoInvito.PENDENTE;
    }

    /**
     * Il destinatario accetta l'invito ed entra nel team.
     *
     * @param utente l'utente che accetta
     * @return il team a cui l'utente e' stato aggiunto
     * @throws IllegalStateException    se l'invito non e' piu' pendente (409)
     * @throws IllegalArgumentException se chi accetta non e' il destinatario (400)
     */
    public Team accetta(Utente utente) {
        if (!isPendente()) {
            throw new IllegalStateException("L'invito non e' piu' pendente");
        }
        if (utente == null || !utente.getId().equals(destinatario.getId())) {
            throw new IllegalArgumentException("Solo il destinatario puo' accettare l'invito");
        }
        team.aggiungiMembro(utente);
        stato = StatoInvito.ACCETTATO;
        return team;
    }

    /** Un invito e' pendente finche' non viene accettato. */
    public boolean isPendente() {
        return stato == StatoInvito.PENDENTE;
    }

    public UUID getId() { return id; }

    public LocalDateTime getData() { return data; }

    public StatoInvito getStato() { return stato; }

    public Team getTeam() { return team; }

    public Utente getMittente() { return mittente; }

    public Utente getDestinatario() { return destinatario; }
}

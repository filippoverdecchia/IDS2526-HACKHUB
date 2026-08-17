package it.unicam.hackhub.domain.team;

import it.unicam.hackhub.domain.staff.Utente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Gruppo di partecipanti che si iscrive a un hackathon e produce una
 * sottomissione.
 *
 * Il team nasce indipendentemente dall'hackathon: viene creato da un utente
 * (che ne diventa il primo membro) e solo in un secondo momento iscritto a un
 * evento. Per questo la relazione con Hackathon e' un'associazione e non una
 * composizione.
 *
 * Information Expert: il team conosce i propri membri, quindi e' il team a
 * decidere se un nuovo membro puo' essere aggiunto.
 */
public class Team {

    private final UUID id;
    private final String nome;
    private final List<Utente> membri;

    public Team(String nome, Utente creatore) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome del team e' obbligatorio");
        }
        if (creatore == null) {
            throw new IllegalArgumentException("Il team deve avere un creatore");
        }
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.membri = new ArrayList<>();
        this.membri.add(creatore);
    }

    /**
     * Aggiunge un membro al team.
     *
     * @param utente il partecipante da aggiungere
     * @throws IllegalArgumentException se l'utente e' gia' nel team
     */
    public void aggiungiMembro(Utente utente) {
        if (utente == null) {
            throw new IllegalArgumentException("L'utente da aggiungere non puo' essere nullo");
        }
        if (contiene(utente)) {
            throw new IllegalArgumentException(
                    "L'utente " + utente.getNome() + " fa gia' parte del team");
        }
        membri.add(utente);
    }

    /** Verifica se un utente fa parte del team. */
    public boolean contiene(Utente utente) {
        return membri.stream()
                .anyMatch(m -> m.getId().equals(utente.getId()));
    }

    public int numeroMembri() {
        return membri.size();
    }

    public UUID getId() { return id; }

    public String getNome() { return nome; }

    /** Lista non modificabile: i membri si aggiungono solo tramite aggiungiMembro. */
    public List<Utente> getMembri() {
        return Collections.unmodifiableList(membri);
    }
}

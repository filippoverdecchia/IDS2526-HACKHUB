package it.unicam.hackhub.domain.staff;

/**
 * Membro dello staff: non partecipa agli hackathon, li gestisce.
 * Astratta perche' un membro dello staff esiste sempre in un ruolo concreto.
 */
public abstract class MembroStaff extends Account {

    protected MembroStaff(String email, String password, String nome) {
        super(email, password, nome);
    }

    /** Ruolo effettivo, utile per i controlli e per le risposte delle API. */
    public abstract RuoloStaff ruolo();
}

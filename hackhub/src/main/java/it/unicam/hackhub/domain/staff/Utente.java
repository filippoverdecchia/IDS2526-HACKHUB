package it.unicam.hackhub.domain.staff;

/**
 * Partecipante alla piattaforma: puo' creare team, iscriverli agli hackathon
 * e inviare sottomissioni.
 *
 * Estende Account, da cui eredita credenziali e identificativo: la
 * separazione da MembroStaff garantisce che un membro dello staff non possa
 * partecipare come concorrente.
 */
public class Utente extends Account {

    public Utente(String email, String password, String nome) {
        super(email, password, nome);
    }
}

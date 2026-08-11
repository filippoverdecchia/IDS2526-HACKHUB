package it.unicam.hackhub.domain.staff;

import java.util.UUID;

/**
 * Radice della gerarchia degli account.
 * Raccoglie i dati comuni a partecipanti e staff, evitando di duplicare
 * le credenziali in ogni sottoclasse.
 */
public abstract class Account {

    private final UUID id;
    private final String email;
    private final String password;
    private final String nome;

    protected Account(String email, String password, String nome) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.nome = nome;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNome() { return nome; }
}

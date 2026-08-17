package it.unicam.hackhub.infrastructure.persistence.memory;

import it.unicam.hackhub.application.port.UtenteRepository;
import it.unicam.hackhub.domain.staff.Utente;
import org.springframework.stereotype.Repository;

/** Adapter di persistenza in memoria per gli utenti partecipanti. */
@Repository
public class InMemoryUtenteRepository
        extends InMemoryRepository<Utente>
        implements UtenteRepository {

    public InMemoryUtenteRepository() {
        super(Utente::getId);
    }
}

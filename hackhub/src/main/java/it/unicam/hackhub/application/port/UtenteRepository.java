package it.unicam.hackhub.application.port;

import it.unicam.hackhub.domain.staff.Utente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Porta di persistenza per gli utenti partecipanti. */
public interface UtenteRepository {

    Utente salva(Utente utente);

    Optional<Utente> trovaPerId(UUID id);

    List<Utente> trovaTutti();
}

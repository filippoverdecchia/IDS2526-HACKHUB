package it.unicam.hackhub.application.port;

import it.unicam.hackhub.domain.invito.Invito;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;

import java.util.Optional;
import java.util.UUID;

/** Porta di persistenza per gli inviti. */
public interface InvitoRepository {

    Invito salva(Invito invito);

    Optional<Invito> trovaPerId(UUID id);

    /** Indica se il destinatario ha gia' un invito pendente per quel team. */
    boolean esistePendente(Team team, Utente destinatario);
}

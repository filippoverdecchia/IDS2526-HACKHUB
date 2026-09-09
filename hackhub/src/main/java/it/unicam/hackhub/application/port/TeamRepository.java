package it.unicam.hackhub.application.port;

import it.unicam.hackhub.domain.team.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Porta di persistenza per i team. */
public interface TeamRepository {

    Team salva(Team team);

    Optional<Team> trovaPerId(UUID id);

    List<Team> trovaTutti();

    boolean esisteConNome(String nome);
}

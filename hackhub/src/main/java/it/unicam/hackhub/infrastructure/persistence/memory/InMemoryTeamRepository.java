package it.unicam.hackhub.infrastructure.persistence.memory;

import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Adapter di persistenza in memoria per i team. */
@Repository
public class InMemoryTeamRepository
        extends InMemoryRepository<Team>
        implements TeamRepository {

    public InMemoryTeamRepository() {
        super(Team::getId);
    }

    @Override
    public boolean esisteConNome(String nome) {
        return contenuto().stream()
                .anyMatch(t -> t.getNome().equalsIgnoreCase(nome));
    }

    @Override
    public Optional<Team> trovaPerMembro(Utente utente) {
        if (utente == null) {
            return Optional.empty();
        }
        return contenuto().stream()
                .filter(t -> t.contiene(utente))
                .findFirst();
    }
}

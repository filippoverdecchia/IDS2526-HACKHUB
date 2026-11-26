package it.unicam.hackhub.infrastructure.persistence.memory;

import it.unicam.hackhub.application.port.InvitoRepository;
import it.unicam.hackhub.domain.invito.Invito;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;
import org.springframework.stereotype.Repository;

/** Adapter di persistenza in memoria per gli inviti. */
@Repository
public class InMemoryInvitoRepository
        extends InMemoryRepository<Invito>
        implements InvitoRepository {

    public InMemoryInvitoRepository() {
        super(Invito::getId);
    }

    @Override
    public boolean esistePendente(Team team, Utente destinatario) {
        return contenuto().stream()
                .anyMatch(i -> i.isPendente()
                        && i.getTeam().getId().equals(team.getId())
                        && i.getDestinatario().getId().equals(destinatario.getId()));
    }
}

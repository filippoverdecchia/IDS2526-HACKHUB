package it.unicam.hackhub.infrastructure.persistence.memory;

import it.unicam.hackhub.application.port.HackathonRepository;
import it.unicam.hackhub.domain.hackathon.Hackathon;
import org.springframework.stereotype.Repository;

/** Adapter di persistenza in memoria per gli hackathon. */
@Repository
public class InMemoryHackathonRepository
        extends InMemoryRepository<Hackathon>
        implements HackathonRepository {

    public InMemoryHackathonRepository() {
        super(Hackathon::getId);
    }

    @Override
    public boolean esisteConNome(String nome) {
        return contenuto().stream()
                .anyMatch(h -> h.getNome().equalsIgnoreCase(nome));
    }
}

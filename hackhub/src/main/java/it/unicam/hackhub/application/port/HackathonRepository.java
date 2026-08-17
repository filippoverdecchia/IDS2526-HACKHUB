package it.unicam.hackhub.application.port;

import it.unicam.hackhub.domain.hackathon.Hackathon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta di persistenza per gli hackathon (pattern Repository).
 *
 * Il livello applicativo dipende da questa interfaccia e non da una
 * implementazione concreta: sostituire la persistenza in memoria con JPA
 * significa aggiungere un adapter, senza modificare ne' i service ne' il
 * dominio (Dependency Inversion Principle).
 */
public interface HackathonRepository {

    Hackathon salva(Hackathon hackathon);

    Optional<Hackathon> trovaPerId(UUID id);

    List<Hackathon> trovaTutti();

    boolean esisteConNome(String nome);
}

package it.unicam.hackhub.infrastructure.persistence.memory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Base comune degli adapter di persistenza in memoria.
 *
 * Le operazioni di salvataggio e ricerca per identificativo sono identiche
 * per tutte le entita': raccoglierle qui evita di ripeterle in ogni
 * repository (DRY).
 *
 * L'identificativo viene estratto tramite una funzione passata al
 * costruttore, invece di richiedere alle entita' di implementare
 * un'interfaccia comune: cosi' il dominio resta libero da vincoli imposti
 * dalla persistenza.
 *
 * @param <T> tipo dell'entita' gestita
 */
public abstract class InMemoryRepository<T> {

    private final Map<UUID, T> archivio = new ConcurrentHashMap<>();
    private final Function<T, UUID> estrattoreId;

    protected InMemoryRepository(Function<T, UUID> estrattoreId) {
        this.estrattoreId = estrattoreId;
    }

    public T salva(T entita) {
        if (entita == null) {
            throw new IllegalArgumentException("L'entita' da salvare non puo' essere nulla");
        }
        archivio.put(estrattoreId.apply(entita), entita);
        return entita;
    }

    public Optional<T> trovaPerId(UUID id) {
        return id == null ? Optional.empty() : Optional.ofNullable(archivio.get(id));
    }

    public List<T> trovaTutti() {
        return List.copyOf(archivio.values());
    }

    /** Accesso alle entita' per le ricerche specifiche delle sottoclassi. */
    protected Collection<T> contenuto() {
        return archivio.values();
    }
}

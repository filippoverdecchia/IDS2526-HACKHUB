package it.unicam.hackhub.infrastructure.persistence.memory;

import it.unicam.hackhub.application.port.StaffRepository;
import it.unicam.hackhub.domain.staff.MembroStaff;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/** Adapter di persistenza in memoria per i membri dello staff. */
@Repository
public class InMemoryStaffRepository
        extends InMemoryRepository<MembroStaff>
        implements StaffRepository {

    public InMemoryStaffRepository() {
        super(MembroStaff::getId);
    }

    @Override
    public List<MembroStaff> trovaPerIds(List<UUID> ids) {
        if (ids == null) {
            return List.of();
        }
        return ids.stream()
                .map(this::trovaPerId)
                .flatMap(java.util.Optional::stream)
                .toList();
    }
}

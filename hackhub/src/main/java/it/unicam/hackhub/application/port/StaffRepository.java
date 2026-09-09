package it.unicam.hackhub.application.port;

import it.unicam.hackhub.domain.staff.MembroStaff;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Porta di persistenza per i membri dello staff. */
public interface StaffRepository {

    MembroStaff salva(MembroStaff membro);

    Optional<MembroStaff> trovaPerId(UUID id);

    List<MembroStaff> trovaPerIds(List<UUID> ids);

    List<MembroStaff> trovaTutti();
}

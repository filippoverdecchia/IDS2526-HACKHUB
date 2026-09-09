package it.unicam.hackhub.api.dto;

import it.unicam.hackhub.domain.hackathon.Hackathon;
import it.unicam.hackhub.domain.staff.MembroStaff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Dati di un hackathon esposti al client.
 *
 * Non si restituisce l'entita' di dominio: verrebbero serializzati anche i
 * team iscritti, le loro sottomissioni e gli account con le password. Il DTO
 * espone solo cio' che serve, disaccoppiando l'API dal modello interno.
 */
public record HackathonResponse(
        UUID id,
        String nome,
        String regolamento,
        LocalDate scadenzaIscrizioni,
        LocalDate dataInizio,
        LocalDate dataFine,
        String luogo,
        BigDecimal premio,
        int dimensioneMaxTeam,
        String stato,
        String organizzatore,
        String giudice,
        List<String> mentori,
        int numeroTeamIscritti
) {

    public static HackathonResponse da(Hackathon hackathon) {
        return new HackathonResponse(
                hackathon.getId(),
                hackathon.getNome(),
                hackathon.getRegolamento(),
                hackathon.getScadenzaIscrizioni(),
                hackathon.getDataInizio(),
                hackathon.getDataFine(),
                hackathon.getLuogo(),
                hackathon.getPremio(),
                hackathon.getDimensioneMaxTeam(),
                hackathon.tipoStato().name(),
                hackathon.getOrganizzatore().getNome(),
                hackathon.getGiudice().getNome(),
                hackathon.getMentori().stream().map(MembroStaff::getNome).toList(),
                hackathon.numeroTeamIscritti());
    }
}

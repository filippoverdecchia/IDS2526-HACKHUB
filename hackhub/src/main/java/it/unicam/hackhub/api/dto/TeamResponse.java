package it.unicam.hackhub.api.dto;

import it.unicam.hackhub.domain.staff.Account;
import it.unicam.hackhub.domain.team.Team;

import java.util.List;
import java.util.UUID;

/** Dati di un team esposti al client: dei membri si espone il solo nome. */
public record TeamResponse(
        UUID id,
        String nome,
        List<String> membri
) {

    public static TeamResponse da(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getNome(),
                team.getMembri().stream().map(Account::getNome).toList());
    }
}

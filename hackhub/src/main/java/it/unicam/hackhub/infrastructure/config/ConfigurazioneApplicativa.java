package it.unicam.hackhub.infrastructure.config;

import it.unicam.hackhub.application.hackathon.HackathonService;
import it.unicam.hackhub.application.port.HackathonRepository;
import it.unicam.hackhub.application.port.StaffRepository;
import it.unicam.hackhub.application.port.TeamRepository;
import it.unicam.hackhub.application.port.UtenteRepository;
import it.unicam.hackhub.application.sottomissione.SottomissioneService;
import it.unicam.hackhub.application.team.TeamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registra i service come componenti Spring.
 *
 * I service non sono annotati con @Service perche' il livello applicativo,
 * come il dominio, resta Java puro: le classi non conoscono il framework e
 * funzionerebbero anche senza. E' questa configurazione, che appartiene
 * all'infrastruttura, a costruirle iniettando gli adapter di persistenza.
 *
 * E' cosi' che il progetto soddisfa il vincolo "sviluppo in Java e
 * successiva portabilita' su Spring Boot": il framework sta al bordo,
 * non dentro la logica.
 */
@Configuration
public class ConfigurazioneApplicativa {

    @Bean
    public TeamService teamService(TeamRepository teamRepository,
                                   UtenteRepository utenteRepository) {
        return new TeamService(teamRepository, utenteRepository);
    }

    @Bean
    public HackathonService hackathonService(HackathonRepository hackathonRepository,
                                             StaffRepository staffRepository,
                                             TeamRepository teamRepository) {
        return new HackathonService(hackathonRepository, staffRepository, teamRepository);
    }

    @Bean
    public SottomissioneService sottomissioneService(HackathonRepository hackathonRepository,
                                                     TeamRepository teamRepository,
                                                     StaffRepository staffRepository) {
        return new SottomissioneService(hackathonRepository, teamRepository, staffRepository);
    }
}

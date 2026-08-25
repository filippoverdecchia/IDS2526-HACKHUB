package it.unicam.hackhub.infrastructure.config;

import it.unicam.hackhub.application.port.StaffRepository;
import it.unicam.hackhub.application.port.UtenteRepository;
import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.Mentore;
import it.unicam.hackhub.domain.staff.Organizzatore;
import it.unicam.hackhub.domain.staff.Utente;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popola la persistenza in memoria con alcuni account all'avvio e ne stampa
 * gli identificativi in console.
 *
 * Serve a poter provare subito gli endpoint: la registrazione degli account
 * appartiene a un'iterazione successiva, quindi senza questi dati non
 * esisterebbe alcun organizzatore da indicare nella creazione di un hackathon.
 */
@Component
public class DatiDiEsempio implements CommandLineRunner {

    private final StaffRepository staffRepository;
    private final UtenteRepository utenteRepository;

    public DatiDiEsempio(StaffRepository staffRepository, UtenteRepository utenteRepository) {
        this.staffRepository = staffRepository;
        this.utenteRepository = utenteRepository;
    }

    @Override
    public void run(String... args) {
        Organizzatore organizzatore =
                new Organizzatore("anna@unicam.it", "password", "Anna Rossi");
        Giudice giudice =
                new Giudice("marco@unicam.it", "password", "Marco Bianchi");
        Mentore mentore =
                new Mentore("sara@unicam.it", "password", "Sara Verdi");

        staffRepository.salva(organizzatore);
        staffRepository.salva(giudice);
        staffRepository.salva(mentore);

        Utente primoUtente = new Utente("luca@studenti.unicam.it", "password", "Luca Neri");
        Utente secondoUtente = new Utente("giulia@studenti.unicam.it", "password", "Giulia Blu");

        utenteRepository.salva(primoUtente);
        utenteRepository.salva(secondoUtente);

        System.out.println();
        System.out.println("=== Identificativi di prova ===");
        System.out.println("Organizzatore : " + organizzatore.getId());
        System.out.println("Giudice       : " + giudice.getId());
        System.out.println("Mentore       : " + mentore.getId());
        System.out.println("Utente 1      : " + primoUtente.getId());
        System.out.println("Utente 2      : " + secondoUtente.getId());
        System.out.println();
    }
}

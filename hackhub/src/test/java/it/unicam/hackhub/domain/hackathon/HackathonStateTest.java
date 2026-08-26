package it.unicam.hackhub.domain.hackathon;

import it.unicam.hackhub.domain.sottomissione.Sottomissione;
import it.unicam.hackhub.domain.sottomissione.Valutazione;
import it.unicam.hackhub.domain.staff.Giudice;
import it.unicam.hackhub.domain.staff.Mentore;
import it.unicam.hackhub.domain.staff.Organizzatore;
import it.unicam.hackhub.domain.staff.Utente;
import it.unicam.hackhub.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica il comportamento della macchina a stati dell'hackathon.
 *
 * Il test lavora sul solo dominio, senza Spring e senza persistenza: e' la
 * conferma pratica che il nucleo dell'applicazione e' Java puro.
 *
 * Le prove sono organizzate in due gruppi: le transizioni di fase e le
 * operazioni consentite in ciascuna fase.
 */
@DisplayName("Macchina a stati dell'hackathon")
class HackathonStateTest {

    private Hackathon hackathon;
    private Giudice giudice;
    private Team team;

    @BeforeEach
    void preparaHackathonEteam() {
        Organizzatore organizzatore =
                new Organizzatore("anna@unicam.it", "password", "Anna Rossi");
        giudice = new Giudice("marco@unicam.it", "password", "Marco Bianchi");
        Mentore mentore = new Mentore("sara@unicam.it", "password", "Sara Verdi");

        hackathon = new Hackathon(
                "AI Challenge 2026", "Regolamento v1",
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                "Camerino", new BigDecimal("1000"), 4,
                organizzatore, giudice, List.of(mentore));

        Utente creatore = new Utente("luca@studenti.unicam.it", "password", "Luca Neri");
        team = new Team("I Byte Perduti", creatore);
    }

    @Nested
    @DisplayName("Transizioni di fase")
    class Transizioni {

        @Test
        @DisplayName("un hackathon appena creato e' in fase di iscrizione")
        void nasceInFaseDiIscrizione() {
            assertEquals(HackathonStateType.IN_ISCRIZIONE, hackathon.tipoStato());
        }

        @Test
        @DisplayName("le fasi si susseguono nell'ordine previsto")
        void attraversaLeQuattroFasiInOrdine() {
            hackathon.passaAlProssimoStato();
            assertEquals(HackathonStateType.IN_CORSO, hackathon.tipoStato());

            hackathon.passaAlProssimoStato();
            assertEquals(HackathonStateType.IN_VALUTAZIONE, hackathon.tipoStato());

            hackathon.passaAlProssimoStato();
            assertEquals(HackathonStateType.CONCLUSO, hackathon.tipoStato());
        }

        @Test
        @DisplayName("dalla fase conclusa non si puo' avanzare")
        void nonAvanzaOltreLaFaseConclusa() {
            portaAllaFase(HackathonStateType.CONCLUSO);

            assertThrows(IllegalStateException.class,
                    () -> hackathon.passaAlProssimoStato());
        }
    }

    @Nested
    @DisplayName("Operazioni consentite per fase")
    class OperazioniPerFase {

        @Test
        @DisplayName("un team si iscrive solo mentre le iscrizioni sono aperte")
        void iscrizioneConsentitaSoloInFaseDiIscrizione() {
            hackathon.iscriviTeam(team);
            assertEquals(1, hackathon.numeroTeamIscritti());
            assertTrue(hackathon.isTeamIscritto(team));
        }

        @Test
        @DisplayName("la sottomissione e' rifiutata prima dell'inizio dei lavori")
        void sottomissioneRifiutataInFaseDiIscrizione() {
            hackathon.iscriviTeam(team);

            assertThrows(IllegalStateException.class,
                    () -> hackathon.aggiungiSottomissione(team, unaSottomissione()));
        }

        @Test
        @DisplayName("la sottomissione e' accettata mentre l'hackathon e' in corso")
        void sottomissioneAccettataInFaseDiSvolgimento() {
            hackathon.iscriviTeam(team);
            portaAllaFase(HackathonStateType.IN_CORSO);

            hackathon.aggiungiSottomissione(team, unaSottomissione());

            assertTrue(hackathon.sottomissioneDi(team).isPresent());
        }

        @Test
        @DisplayName("un team non puo' iscriversi a lavori iniziati")
        void iscrizioneRifiutataQuandoIlavoriSonoIniziati() {
            portaAllaFase(HackathonStateType.IN_CORSO);

            assertThrows(IllegalStateException.class,
                    () -> hackathon.iscriviTeam(team));
        }

        @Test
        @DisplayName("la valutazione e' accettata solo nella fase di valutazione")
        void valutazioneAccettataSoloInFaseDiValutazione() {
            hackathon.iscriviTeam(team);
            portaAllaFase(HackathonStateType.IN_CORSO);
            hackathon.aggiungiSottomissione(team, unaSottomissione());
            portaAllaFase(HackathonStateType.IN_VALUTAZIONE);

            hackathon.valutaSottomissione(team, unaValutazione());

            assertTrue(hackathon.sottomissioneDi(team).orElseThrow().isValutata());
        }

        @Test
        @DisplayName("nella fase conclusa nessuna operazione e' ammessa")
        void nessunaOperazioneAmmessaAdHackathonConcluso() {
            portaAllaFase(HackathonStateType.CONCLUSO);

            assertAll(
                    () -> assertThrows(IllegalStateException.class,
                            () -> hackathon.iscriviTeam(team)),
                    () -> assertThrows(IllegalStateException.class,
                            () -> hackathon.aggiungiSottomissione(team, unaSottomissione())),
                    () -> assertThrows(IllegalStateException.class,
                            () -> hackathon.valutaSottomissione(team, unaValutazione())));
        }
    }

    // ===== metodi di supporto =====

    /** Fa avanzare l'hackathon fino alla fase indicata. */
    private void portaAllaFase(HackathonStateType faseDesiderata) {
        while (hackathon.tipoStato() != faseDesiderata) {
            hackathon.passaAlProssimoStato();
        }
    }

    private Sottomissione unaSottomissione() {
        return new Sottomissione("Titolo elaborato", "Contenuto dell'elaborato");
    }

    private Valutazione unaValutazione() {
        return new Valutazione("Progetto ben strutturato", 9, giudice);
    }
}

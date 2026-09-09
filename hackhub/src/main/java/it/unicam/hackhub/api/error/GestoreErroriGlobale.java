package it.unicam.hackhub.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Traduce le eccezioni del dominio in risposte HTTP.
 *
 * Centralizzare qui la traduzione mantiene i controller privi di blocchi
 * try/catch e garantisce che lo stesso tipo di errore produca sempre la
 * stessa risposta (Pure Fabrication: una classe che non rappresenta un
 * concetto del dominio ma raccoglie una responsabilita' tecnica).
 *
 * Corrispondenza fra eccezioni e codici:
 *   IllegalArgumentException -> 400, i dati forniti non sono validi
 *   IllegalStateException    -> 409, l'operazione non e' ammessa nella fase corrente
 */
@RestControllerAdvice
public class GestoreErroriGlobale {

    /** Dati non validi o entita' non trovata. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> datiNonValidi(IllegalArgumentException eccezione) {
        return risposta(HttpStatus.BAD_REQUEST, eccezione.getMessage());
    }

    /** Operazione richiesta in una fase che non la consente. */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> operazioneNonAmmessa(IllegalStateException eccezione) {
        return risposta(HttpStatus.CONFLICT, eccezione.getMessage());
    }

    /** Violazione dei vincoli di formato dichiarati sui DTO di richiesta. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> formatoNonValido(MethodArgumentNotValidException eccezione) {
        String dettaglio = eccezione.getBindingResult().getFieldErrors().stream()
                .map(errore -> errore.getField() + ": " + errore.getDefaultMessage())
                .reduce((primo, secondo) -> primo + "; " + secondo)
                .orElse("richiesta non valida");

        return risposta(HttpStatus.BAD_REQUEST, dettaglio);
    }

    private ResponseEntity<Map<String, Object>> risposta(HttpStatus stato, String messaggio) {
        return ResponseEntity.status(stato).body(Map.of(
                "istante", LocalDateTime.now().toString(),
                "stato", stato.value(),
                "errore", stato.getReasonPhrase(),
                "messaggio", messaggio));
    }
}

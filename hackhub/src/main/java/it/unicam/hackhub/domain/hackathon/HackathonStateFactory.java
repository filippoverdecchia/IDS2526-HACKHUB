package it.unicam.hackhub.domain.hackathon;

/**
 * Factory del pattern State: dato un tipo e il Context, restituisce
 * l'oggetto-stato corrispondente.
 *
 * Serve soprattutto per RICOSTRUIRE lo stato quando un hackathon viene
 * caricato dalla persistenza, dove si salva solo il valore enumerato
 * e non l'oggetto.
 */
public final class HackathonStateFactory {

    private HackathonStateFactory() { }

    public static HackathonState crea(HackathonStateType tipo, Hackathon hackathon) {
        return switch (tipo) {
            case IN_ISCRIZIONE  -> new SubscriptionState(hackathon);
            case IN_CORSO       -> new ProgressState(hackathon);
            case IN_VALUTAZIONE -> new EvaluationState(hackathon);
            case CONCLUSO       -> new EndedState(hackathon);
        };
    }
}

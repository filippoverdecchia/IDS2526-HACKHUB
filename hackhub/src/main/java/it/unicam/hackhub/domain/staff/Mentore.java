package it.unicam.hackhub.domain.staff;

/** Supporta i team durante l'hackathon, propone call e segnala violazioni. */
public class Mentore extends MembroStaff {

    public Mentore(String email, String password, String nome) {
        super(email, password, nome);
    }

    @Override
    public RuoloStaff ruolo() { return RuoloStaff.MENTORE; }
}

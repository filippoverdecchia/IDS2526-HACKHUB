package it.unicam.hackhub.domain.staff;

/** Crea gli hackathon e riceve le segnalazioni di violazione del regolamento. */
public class Organizzatore extends MembroStaff {

    public Organizzatore(String email, String password, String nome) {
        super(email, password, nome);
    }

    @Override
    public RuoloStaff ruolo() { return RuoloStaff.ORGANIZZATORE; }
}

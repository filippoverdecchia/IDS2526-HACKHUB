package it.unicam.hackhub.domain.staff;

/** Valuta le sottomissioni dei team e proclama il vincitore. */
public class Giudice extends MembroStaff {

    public Giudice(String email, String password, String nome) {
        super(email, password, nome);
    }

    @Override
    public RuoloStaff ruolo() { return RuoloStaff.GIUDICE; }
}

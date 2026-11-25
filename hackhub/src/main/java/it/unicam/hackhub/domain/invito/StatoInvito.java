package it.unicam.hackhub.domain.invito;

/**
 * Stati possibili di un invito a unirsi a un team.
 *
 * Un invito nasce PENDENTE e diventa ACCETTATO quando il destinatario
 * entra nel team. Un invito accettato non puo' essere accettato di nuovo.
 */
public enum StatoInvito {
    PENDENTE,
    ACCETTATO
}

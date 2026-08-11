# Iterazione 1 — Ciclo di vita dell'hackathon

**Periodo:** 11 agosto 2026 → in corso
**Autori:** Filippo Verdecchia
**Stato:** in lavorazione

Documento vivo: si aggiorna a ogni avanzamento e si chiude a fine iterazione.

---

## Obiettivo

Realizzare la fetta verticale che attraversa l'intero ciclo di vita di un
hackathon, dalla creazione alla valutazione delle sottomissioni.
È l'iterazione che giustifica l'adozione del pattern State: ognuno dei casi
d'uso è permesso solo in una fase precisa.

## Casi d'uso inclusi

| Caso d'uso | Attore | Fase richiesta |
|---|---|---|
| Crea hackathon | Organizzatore | — |
| Crea team | Utente | — |
| Iscrivi team a hackathon | Membro del team | IN_ISCRIZIONE |
| Invia sottomissione | Membro del team | IN_CORSO |
| Valuta sottomissione | Giudice | IN_VALUTAZIONE |

Casi d'uso esplicitamente **fuori** da questa iterazione: invita utente nel team,
accetta invito, proclama vincitore, proponi una call, segnala violazione,
consulta elenco hackathon, visualizza dettagli hackathon.

## Design pattern applicati

| Pattern | Dove | Perché |
|---|---|---|
| State | `domain/hackathon` | Le operazioni permesse cambiano con la fase; evita if/switch sparsi |
| Factory | `HackathonStateFactory` | Ricostruisce l'oggetto-stato dal valore enumerato salvato |
| Repository | `repository/` + implementazioni in memoria | Isola il dominio dalla persistenza |

## Architettura

```
api/          Controller REST + DTO
service/      Logica dei casi d'uso
repository/   Interfacce (porte) + implementazioni in memoria (adapter)
domain/       Entità e macchina a stati — nessuna dipendenza da Spring
```

Regola: il dominio non importa nulla di Spring, JPA o HTTP.
Nessun `if` sulla fase dell'hackathon: si interroga lo stato.

---

## Avanzamento

### Diagrammi UML
- [x] Use Case Diagram `UC1` — 5 casi d'uso evidenziati in verde
- [x] Class Diagram di analisi (domain model)
- [ ] Class Diagram di progetto (Application / Domain / Infrastructure)
- [ ] Sequence Diagram — Crea hackathon
- [ ] Sequence Diagram — Crea team
- [ ] Sequence Diagram — Iscrivi team a hackathon
- [ ] Sequence Diagram — Invia sottomissione
- [ ] Sequence Diagram — Valuta sottomissione
- [ ] Use Case Description per i 5 casi d'uso (1 su 5 completata)

### Codice
- [x] Setup progetto Spring Boot 4.1.0, Java 21, Maven wrapper
- [ ] Gerarchia Account e ruoli dello staff
- [ ] Stati dell'hackathon (interfaccia, base astratta, enum)
- [ ] Stati concreti delle quattro fasi
- [ ] Factory degli stati
- [ ] Entità Hackathon (Context)
- [ ] Entità Team e Utente
- [ ] Entità Sottomissione e Valutazione
- [ ] Repository (interfacce + implementazioni in memoria)
- [ ] Service dei cinque casi d'uso
- [ ] Controller REST e DTO
- [ ] Test della macchina a stati

---

## Decisioni prese

**11 agosto — "Crea team" incluso nell'iterazione.**
Nel diagramma iniziale "Iscrivi team a hackathon" era previsto senza
"Crea team": senza un modo di creare i team il caso d'uso non sarebbe
dimostrabile. Aggiunto, portando l'iterazione a cinque casi d'uso.

**11 agosto — Persistenza in memoria prima di JPA.**
I repository sono interfacce con implementazione in memoria. Il passaggio a
JPA sarà un adapter aggiuntivo, senza modifiche al dominio.

**11 agosto — Niente autenticazione.**
Non richiesta dai requisiti; esclusa per contenere i tempi.

---

## Note per chi subentra

- Il file `docs/vp/HACKHUB.vpp` è **binario**: Git non sa fonderlo.
  Lo modifica una persona sola. Le proposte di diagramma si mandano come
  schizzo, mai come modifica al file.
- Ogni commit dal proprio account GitHub, mai commit per conto di altri.
- Commit piccoli: una cosa sola, descrivibile in una frase senza usare "e".
- Il diagramma UML si committa prima del codice che lo realizza.

---

## Consuntivo di fine iterazione

*(Da compilare alla chiusura: cosa è stato realizzato, cosa è slittato,
problemi incontrati, cosa cambia per l'iterazione successiva.)*

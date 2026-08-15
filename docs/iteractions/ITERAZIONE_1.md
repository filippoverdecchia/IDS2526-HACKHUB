# Iterazione 1 — Ciclo di vita dell'hackathon

**Periodo:** 11 agosto 2026 → 17 agosto 2026 (previsto)
**Autore:** Filippo Verdecchia
**Stato:** progettazione UML quasi completata, implementazione da avviare
**Ultimo aggiornamento:** 14 agosto 2026

Documento vivo: si aggiorna a ogni avanzamento e si chiude con un consuntivo.

---

## Piano complessivo del progetto

Quattro iterazioni del Processo Unificato. Repo congelata il **14 settembre 2026**,
giorno dello scritto.

| Iterazione | Casi d'uso | Novità tecniche | Scadenza |
|---|---|---|---|
| **1** | Crea hackathon · Crea team · Iscrivi team a hackathon · Invia sottomissione · Valuta sottomissione | State, Factory, Repository | 17 agosto |
| **2** | Invita utente nel team · Accetta invito · Consulta elenco hackathon | Gestione inviti | 24 agosto |
| **3** | Proclama vincitore · Visualizza dettagli hackathon · Segnala violazione | Adapter (pagamento premio) | 31 agosto |
| **4** | Proponi una call · casi d'uso residui e rifiniture | Adapter (calendario) | 7 settembre |

Dall'8 settembre: test, relazione finale, rifiniture.

---

## Obiettivo dell'iterazione

Realizzare la fetta verticale che attraversa l'intero ciclo di vita di un
hackathon, dalla creazione alla valutazione delle sottomissioni.
È l'iterazione che giustifica l'adozione del pattern State: ognuno dei casi
d'uso è permesso solo in una fase precisa dell'evento.

## Casi d'uso inclusi

| Caso d'uso | Attore | Fase richiesta | Esito atteso |
|---|---|---|---|
| Crea hackathon | Organizzatore | — | 201 Created |
| Crea team | Utente | — | 201 Created |
| Iscrivi team a hackathon | Membro del team | IN_ISCRIZIONE | 200 OK |
| Invia sottomissione | Membro del team | IN_CORSO | 201 Created |
| Valuta sottomissione | Giudice | IN_VALUTAZIONE | 201 Created |

Quando l'operazione è richiesta in una fase che non la consente, lo stato
lancia `IllegalStateException` e la risposta è `409 Conflict`.

## Design pattern applicati

| Pattern | Dove | Perché |
|---|---|---|
| State | `domain/hackathon` | Le operazioni permesse cambiano con la fase; elimina gli `if` sullo stato |
| Factory | `HackathonStateFactory` | Ricostruisce l'oggetto-stato dal valore enumerato salvato |
| Repository | porte in `application` + adapter in `infrastructure` | Isola il dominio dalla persistenza |

Requisito ufficiale: almeno due pattern diversi dal Singleton. Soddisfatto.

## Architettura

```
api/             Controller REST e DTO
application/     Service dei casi d'uso + porte Repository
domain/          Entità e macchina a stati — Java puro, nessuna dipendenza da Spring
infrastructure/  Implementazioni delle porte (in memoria)
```

Il dominio non importa nulla di Spring, JPA o HTTP: soddisfa il vincolo
"sviluppo in Java e successiva portabilità su Spring Boot".
Nessun `if` sulla fase dell'hackathon: si interroga lo stato.

---

## Avanzamento

### Diagrammi UML
- [x] Use Case Diagram `UC1` — casi d'uso dell'iterazione evidenziati in verde
- [x] Class Diagram di analisi — modello di dominio
- [x] Class Diagram di progetto — livello Domain
- [x] Class Diagram di progetto — livello Application
- [x] Class Diagram di progetto — livello Infrastructure
- [x] Sequence Diagram — Crea team
- [x] Sequence Diagram — Crea hackathon
- [x] Sequence Diagram — Iscrivi team a hackathon
- [ ] Sequence Diagram — Invia sottomissione
- [ ] Sequence Diagram — Valuta sottomissione
- [ ] Use Case Description (1 di 5 completata: Crea hackathon)

### Codice
- [x] Setup Spring Boot 4.1.0, Java 21, Maven wrapper
- [x] Gerarchia Account e ruoli dello staff
- [x] Stati dell'hackathon: enumerazione, interfaccia, classe base astratta
- [x] Quattro stati concreti
- [x] Factory degli stati
- [x] Entità Hackathon come Context
- [ ] Entità Utente, Team, Sottomissione, Valutazione
- [ ] Operazioni di fase negli stati concreti (iscrizione, sottomissione, valutazione)
- [ ] Porte Repository e implementazioni in memoria
- [ ] Service dei cinque casi d'uso
- [ ] Controller REST, DTO e gestore errori centralizzato
- [ ] Test della macchina a stati

---

## Struttura del modello Visual Paradigm

```
HACKHUB
├── ATTORI                        (fuori dalle iterazioni: valgono per tutte)
└── ITERAZIONE 1
    ├── CLASS DIAGRAM
    │   ├── ANALISI               Domain_Model_Analisi
    │   └── PROGETTO
    │       ├── APPLICATION       Application_Layer
    │       ├── DOMAIN            Domain_Layer
    │       └── INFRASTRUCTURE    Infrastructure_Layer
    └── USE CASE/SD
        ├── SEQUENCE DIAGRAM      un diagramma per caso d'uso
        └── USE CASE DIAGRAM      UC1
```

---

## Decisioni prese

**11 agosto — "Crea team" incluso nell'iterazione.**
Il diagramma iniziale prevedeva "Iscrivi team a hackathon" senza "Crea team":
senza un modo di creare i team il caso d'uso non sarebbe dimostrabile.
L'iterazione passa a cinque casi d'uso.

**11 agosto — Persistenza in memoria prima di JPA.**
Le porte sono interfacce, le implementazioni usano una mappa in memoria.
Il passaggio a JPA sarà un adapter aggiuntivo, senza modifiche a dominio
e livello applicativo.

**11 agosto — Niente autenticazione.**
Non richiesta dai requisiti; esclusa per contenere i tempi.

**12 agosto — Quattro iterazioni invece di tre.**
Iterazioni più piccole rendono il processo più leggibile e limitano il danno
in caso di ritardo sull'ultima.

**12 agosto — Oggetto parametro invece di Builder.**
Il costruttore di `Hackathon` ha undici parametri, che è uno smell.
La soluzione adottata è il record `DatiHackathon`, che risolve anche il
problema di passare i dati dal boundary REST al livello applicativo senza
accoppiare i due livelli. Un Builder avrebbe migliorato una sola chiamata.

**14 agosto — Lifeline dello stato tipizzata sull'interfaccia.**
Nei sequence diagram la lifeline dello stato è `stato : HackathonState` e non
lo stato concreto: nel ramo alternativo del frammento `alt` l'hackathon si
trova per definizione in una fase diversa da quella richiesta, quindi il tipo
concreto sarebbe scorretto.

**14 agosto — Distinzione fra le eccezioni.**
`IllegalArgumentException` per dati non validi (risposta 400);
`IllegalStateException` per operazione richiesta nella fase sbagliata
(risposta 409). La traduzione da eccezione a codice HTTP avverrà in un
gestore centralizzato annotato `@RestControllerAdvice`, non nei controller.

---

## Compromessi noti sui principi SOLID

Dichiarati esplicitamente: sono scelte, non sviste.

**Interface Segregation.** `HackathonState` espone cinque operazioni, ma ogni
stato concreto ne usa una o due; `EndedState` non ne usa nessuna. Segmentare
l'interfaccia frammenterebbe il pattern e impedirebbe a `Hackathon` di trattare
gli stati in modo uniforme. È il compromesso standard del pattern State.

**Liskov.** `AbstractHackathonState` vieta ogni operazione per default e i
sottotipi fanno override solo di ciò che consentono: formalmente è un
"refused bequest". Il contratto dell'interfaccia dichiara però che
l'operazione può fallire se la fase non la permette, quindi i sottotipi
rispettano il contratto dichiarato.

**Single Responsibility, Open/Closed, Dependency Inversion** sono invece
pienamente rispettati: entità con le proprie regole, service con la logica
del caso d'uso, controller con il solo protocollo HTTP; un nuovo stato è una
classe nuova senza modifiche alle esistenti; il dominio dipende da interfacce
e non da implementazioni.

---

## Note per chi subentra

- Il file `docs/vp/HACKHUB.vpp` è **binario**: Git non sa fonderlo.
  Lo modifica una persona sola. Le proposte di diagramma si mandano come
  schizzo, mai come modifica al file.
- Ogni commit dal proprio account GitHub, mai commit per conto di altri.
- Commit piccoli: una cosa sola, descrivibile in una frase senza usare "e".
- Il diagramma UML si committa prima del codice che lo realizza.
- Convenzione dei messaggi: `<tipo>(<ambito>): <cosa>` con tipi
  `feat` `fix` `docs` `test` `refactor` `chore` e ambiti
  `domain` `app` `api` `infra` `uml`.

---

## Consuntivo di fine iterazione

*(Da compilare alla chiusura: cosa è stato realizzato, cosa è slittato,
problemi incontrati, cosa cambia per l'iterazione successiva.)*

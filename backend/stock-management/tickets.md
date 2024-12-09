# Projekt: Aktienverwaltungs-Webapp

**Technologie-Stack:**

- Backend: Spring Boot
- Frontend: Angular
- Datenbank: PostgreSQL (mit Docker)
- Ziel: Dashboard mit Kursverlauf, Dividendenrendite, etc.

---

## Infrastruktur & Setup

### Ticket 1: Projekt-Setup (Backend)

**Beschreibung:**

- Initiales Erstellen des Spring-Boot-Projekts
- Maven/Gradle konfigurieren
- Standardverzeichnisstruktur anlegen
- Health-Check-Endpoint `/health` hinzufügen

**Akzeptanzkriterien:**

- Applikation startet lokal ohne Fehler
- `/health` Endpoint erreichbar

---

### Ticket 2: Projekt-Setup (Frontend)

**Beschreibung:**

- Angular-Projekt mit Angular CLI erstellen
- Grundlegende App-Struktur (Components, Modules, Routing)
- Startseite mit "Hello World"

**Akzeptanzkriterien:**

- `ng serve` zeigt Startseite an
- Projektstruktur nach Best Practices

---

### Ticket 3: Docker-Setup für Datenbank

**Beschreibung:**

- Docker-Compose für PostgreSQL anlegen
- Datenbankcontainer starten (Port, Persistenz)
- Verbindung Spring Boot <-> DB sicherstellen

**Akzeptanzkriterien:**

- `docker-compose up` startet Postgres ohne Fehler
- Spring Boot App verbindet sich erfolgreich

---

## Datenbank & Models

### Ticket 4: Datenbankmodell für Aktien

**Beschreibung:**

- Entity `Stock` anlegen
- Felder: `id (PK)`, `tickerSymbol`, `name`, `sektor`, `isin`, `kaufDatum`, `kaufPreis`, `anzahl`, ...

**Akzeptanzkriterien:**

- Tabelle `stocks` wird beim Starten erstellt
- JPA-Entity korrekt angelegt

---

### Ticket 5: Datenbankmodell für Dividenden

**Beschreibung:**

- Entity `Dividend` anlegen
- Felder: `id (PK)`, `stock_id (FK)`, `betrag`, `datum`, `währung`
- Beziehung: Viele Dividenden gehören zu einer Aktie (Many-to-One)

**Akzeptanzkriterien:**

- Tabelle `dividends` wird erstellt
- Beziehung zu `Stock` korrekt definiert

---

## Backend-Funktionalitäten

### Ticket 6: REST-Endpunkte für Aktien

**Beschreibung:**

- `StockController` erstellen mit CRUD-Methoden:
  - `GET /api/stocks`
  - `GET /api/stocks/{id}`
  - `POST /api/stocks`
  - `PUT /api/stocks/{id}`
  - `DELETE /api/stocks/{id}`

**Akzeptanzkriterien:**

- Alle CRUD-Operationen funktionieren
- JSON-Formate klar definiert

---

### Ticket 7: REST-Endpunkte für Dividenden

**Beschreibung:**

- `DividendController` mit:
  - `GET /api/stocks/{id}/dividends`
  - `POST /api/stocks/{id}/dividends`
  - `PUT /api/dividends/{id}`
  - `DELETE /api/dividends/{id}`

**Akzeptanzkriterien:**

- Dividenden werden korrekt einer Aktie zugeordnet
- CRUD für Dividenden funktioniert

---

### Ticket 8: Validierung & Fehlerbehandlung

**Beschreibung:**

- Bean Validation für Eingaben (z.B. Ticker nicht leer, Betrag > 0)
- Exception Handling (HTTP 400 bei ungültigen Daten, HTTP 404 bei nicht gefundenen Ressourcen)

**Akzeptanzkriterien:**

- Ungültige Eingaben liefern passende Fehlermeldungen
- Nicht vorhandene IDs liefern 404

---

## Frontend-Funktionalitäten

### Ticket 9: Aktien-Übersichtsseite (Frontend)

**Beschreibung:**

- `StockListComponent` erstellen
- Anzeige aller Aktien in Tabelle
- "Neu hinzufügen" Button

**Akzeptanzkriterien:**

- `GET /api/stocks` wird aufgerufen und Daten angezeigt
- Button führt zu Formular für neue Aktie

---

### Ticket 10: Formular für neue Aktie

**Beschreibung:**

- `StockFormComponent` erstellen
- Felder: Ticker, Name, Kaufdatum, Kaufpreis, Anzahl
- POST an `/api/stocks`

**Akzeptanzkriterien:**

- Erfolgreiche Erstellung leitet zurück zur Übersicht
- Validierungsfehler werden angezeigt

---

### Ticket 11: Detailansicht für Aktie mit Dividenden

**Beschreibung:**

- `StockDetailComponent` erstellen
- Anzeige der Aktiendetails sowie zugehöriger Dividenden
- Button "Dividende hinzufügen"

**Akzeptanzkriterien:**

- `GET /api/stocks/{id}` zeigt Aktiendetails
- `GET /api/stocks/{id}/dividends` zeigt Dividenden
- Button führt zum Dividenden-Formular

---

### Ticket 12: Dividenden-Formular

**Beschreibung:**

- `DividendFormComponent` erstellen
- Felder: Betrag, Datum, Währung
- POST an `/api/stocks/{id}/dividends`

**Akzeptanzkriterien:**

- Nach POST Rückkehr zur Detailansicht
- Validierungsfehler anzeigen

---

## Dashboard & Reporting

### Ticket 13: Dashboard-Ansicht (Mock-Kursverlauf)

**Beschreibung:**

- `DashboardComponent` erstellen
- Anzeige von Kennzahlen: Gesamtwert, Gesamtdividenden
- Einbinden eines Chart-Tools (z. B. Chart.js) für Kursverläufe

**Akzeptanzkriterien:**

- Darstellung von Mock-Daten im Diagramm
- Kennzahlen werden angezeigt

---

### Ticket 14: Dividendenrendite-Berechnung (Backend)

**Beschreibung:**

- Rendite: `(Summe Dividenden / Kaufpreis) * 100` pro Aktie
- Erweiterte GET-Endpunkte, um Rendite zurückzugeben

**Akzeptanzkriterien:**

- Endpunkt liefert Renditen
- Frontend kann Rendite anzeigen

---

### Ticket 15: Kursdaten-Abfrage (Mock)

**Beschreibung:**

- Mock-Service für Kursdaten
- `GET /api/stocks/{id}/prices` liefert Dummy-Kursverlauf
- Später erweiterbar für echte API-Aufrufe

**Akzeptanzkriterien:**

- Dummy-Daten abrufbar und darstellbar im Dashboard

---

## Deployment & CI/CD

### Ticket 16: Docker-Compose für Gesamtsystem

**Beschreibung:**

- Dockerfile für Backend und Frontend erstellen
- Docker-Compose für Backend, Frontend, Datenbank
- `docker-compose up` startet komplettes System

**Akzeptanzkriterien:**

- Ein Browserzugriff zeigt laufende Anwendung
- Alle Services laufen in Containern

---

### Ticket 17: Basic CI Pipeline

**Beschreibung:**

- CI (GitHub Actions, GitLab CI, o. Ä.) für Build & Test
- Tests bei jedem Push ausführen
- Optional: Deployment-Job für Staging

**Akzeptanzkriterien:**

- Pipeline schlägt bei fehlerhaftem Code fehl
- Erfolgreicher Build & Test bei korrektem Code

---

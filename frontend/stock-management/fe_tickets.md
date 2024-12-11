## Frontend Tickets (Angular 18)

### Ticket 9: Aktien-Übersichtsseite (Frontend)

**Beschreibung:**

- `StockListComponent` erstellen
- Anzeige aller Aktien in einer Tabelle
- "Neu hinzufügen"-Button, um eine neue Aktie anzulegen

**Akzeptanzkriterien:**

- `GET /api/v1/stocks` wird aufgerufen und Daten werden in Tabelle dargestellt
- Button führt zu Formular für neue Aktie

---

### Ticket 10: Formular für neue Aktie

**Beschreibung:**

- `StockFormComponent` erstellen
- Formularfelder: Ticker, Name, Kaufdatum, Kaufpreis, Anzahl
- POST an `/api/v1/stocks`

**Akzeptanzkriterien:**

- Erfolgreiche Erstellung einer Aktie führt zurück zur Übersicht
- Validierungsfehler werden angezeigt (z. B. Pflichtfelder, Wertebereiche)

---

### Ticket 11: Detailansicht für Aktie mit Dividenden

**Beschreibung:**

- `StockDetailComponent` erstellen
- Anzeige von Aktiendetails (über `GET /api/v1/stocks/{id}`)
- Anzeige aller Dividenden zur Aktie (über `GET /api/v1/stocks/{id}/dividends`)
- Button "Dividende hinzufügen" führt zum Dividenden-Formular

**Akzeptanzkriterien:**

- Detailansicht zeigt korrekte Daten der Aktie
- Dividenden werden in einer Liste dargestellt
- Button für Dividenden-Formular vorhanden

---

### Ticket 12: Dividenden-Formular

**Beschreibung:**

- `DividendFormComponent` erstellen
- Felder: Betrag, Datum, Währung
- POST an `/api/v1/stocks/{id}/dividends`

**Akzeptanzkriterien:**

- Nach erfolgreichem POST zurück zur Detailansicht der Aktie
- Validierungsfehler anzeigen

---

### Ticket 13: Dashboard-Ansicht

**Beschreibung:**

- `DashboardComponent` erstellen
- Anzeige von Kennzahlen wie Gesamtwert des Portfolios, Gesamtdividenden
- Integration eines Chart-Tools (z. B. Chart.js) zur Darstellung von Kursverläufen

**Akzeptanzkriterien:**

- Daten werden grafisch angezeigt
- Kennzahlen (z. B. Summen) werden korrekt berechnet und dargestellt

# 📚 Library API

## 🧾 Beskrivning

Detta projekt är ett backend API för ett bibliotekssystem utvecklat med Spring Boot.
Systemet hanterar böcker, författare och utlåning, med fokus på korrekt arkitektur, affärsregler och robust hantering av samtidiga anrop.

---

## ⚙️ Funktionalitet

### 📖 Books

* Skapa bok
* Hämta alla böcker
* Hämta bok via ID

### ✍️ Authors

* Skapa författare
* Hämta författare via ID
* Hämta alla böcker för en författare

### 🔄 Loans

* Skapa lån
* Hämta alla lån

---

## 📏 Affärsregler

* En bok kan endast ha **ett aktivt lån åt gången**
* Felhantering:

  * `400 Bad Request` – om bok redan är utlånad
  * `404 Not Found` – om resurs saknas

---

## 🏗️ Arkitektur

Projektet följer en tydlig lagerstruktur:

* **Controller** – hanterar HTTP requests
* **Service** – innehåller affärslogik
* **Repository** – databasåtkomst via JPA
* **DTO** – används för request/response (entities exponeras inte)

---

## 🗄️ Databas

* In-memory databas: **H2**
* Används för utveckling och test

---

## 🧪 Tester

Integrationstester är implementerade med:

* `@SpringBootTest`
* `TestRestTemplate`

Testerna verifierar:

* Skapande av Author, Book och Loan
* Att samma bok inte kan lånas två gånger
* 404 vid saknad resurs
* Concurrency (parallella anrop)

---

## ⚡ Concurrency (Samtidiga anrop)

Systemet skyddar mot race conditions vid utlåning genom:

* Synkronisering i service-lagret
* Kontroll av aktiva lån i databasen

Ett test simulerar flera samtidiga requests och verifierar att:

* Endast ett lån skapas per bok

---

## 📈 Del 5 – Skalbarhet (Reflektion)

Systemet är byggt som ett klassiskt Spring Boot API med tydlig lagerstruktur (Controller, Service, Repository), vilket gör det lätt att underhålla och vidareutveckla. Vid normal belastning fungerar systemet stabilt, men vid hög belastning finns flera aspekter att ta hänsyn till.

En potentiell flaskhals i systemet är hanteringen av samtidiga anrop vid utlåning av böcker. I implementationen används `synchronized` i service-lagret för att förhindra race conditions. Detta säkerställer korrekt beteende, men innebär också att endast en tråd i taget kan exekvera metoden, vilket begränsar skalbarheten vid hög belastning.

Databasen är en annan viktig faktor. Just nu används en enkel H2-databas, vilket fungerar bra för utveckling och test, men inte är optimerad för produktion. Vid många samtidiga anrop kan databasen bli en flaskhals, särskilt vid frekventa läsningar och skrivningar kopplade till lån.

För att förbättra skalbarheten i ett verkligt system skulle man kunna:

* använda en produktionsdatabas som PostgreSQL eller MySQL
* implementera caching (t.ex. för GET-anrop av böcker)
* ersätta `synchronized` med mer skalbara lösningar som optimistic locking (`@Version`)

Sammanfattningsvis är systemet robust för mindre belastning, men kräver vidare optimering för att hantera hög trafik effektivt.

---

## 🚀 Hur man kör projektet

1. Starta applikationen
2. API finns tillgängligt på:

   ```
   http://localhost:8080/api/v1
   ```

### Exempel endpoints:

* `POST /api/v1/books`
* `GET /api/v1/books`
* `POST /api/v1/authors`
* `POST /api/v1/loans`

---

## ⚙️ Konfiguration

Standardport är `8080`.
Om porten är upptagen kan den ändras i `application.properties`.

---

## ✅ Status

Projektet uppfyller kraven för:

* REST API struktur
* Affärslogik
* DTO-användning
* Integrationstester
* Concurrency-hantering

---

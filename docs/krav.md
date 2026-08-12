# Krav — BFF-mall för mikrofrontend (TMBF)

## Bakgrund och syfte

Detta är en mall, inte en driftsatt tjänst. Den ger ett team en färdig utgångspunkt för att
bygga en ny backend-for-frontend till en regel-mikrofrontend: exempel på ändpunkter,
indatavalidering, felhanteringsmönster, hälsokontroll och integrationstestning. Syftet är att
nya BFF:er ska kunna byggas snabbt och konsekvent, med samma grundmönster för felhantering och
integration som övriga BFF:er i plattformen.

---

## Intressenter och aktörer

| Aktör | Roll |
|---|---|
| Utvecklingsteam | Klonar/kopierar mallen för att skapa en ny BFF |
| En mikrofrontend byggd på motsvarande mikrofrontend-mall | Kommer att anropa den färdiga BFF:en |
| En bakomliggande regeltjänst | Kommer att anropas av den färdiga BFF:en |

---

## Funktionella krav

### TMBF-FR-01 — Exempeländpunkter

- **TMBF-FR-01.1** Mallen ska tillhandahålla exempel på en ändpunkt för att hämta ärendedata
  från en bakomliggande tjänst.
- **TMBF-FR-01.2** Mallen ska tillhandahålla exempel på en ändpunkt för att uppdatera
  ärendedata hos en bakomliggande tjänst.
- **TMBF-FR-01.3** Mallen ska tillhandahålla exempel på en ändpunkt som kräver
  auktoriseringsuppgifter och vidarebefordrar dem oförändrade till en bakomliggande tjänst.
- **TMBF-FR-01.4** Mallen ska tillhandahålla exempel på en ändpunkt för att slutföra ett ärende.
- **TMBF-FR-01.5** Mallen ska tillhandahålla en egen hälsokontrolländpunkt utöver ramverkets
  standardhälsokontroller.

### TMBF-FR-02 — Felhanteringsmönster

- **TMBF-FR-02.1** Mallen ska demonstrera hur fel från en bakomliggande tjänst mappas till en
  konsekvent felrespons utan att exponera interna feldetaljer.
- **TMBF-FR-02.2** Mallen ska demonstrera indatavalidering på inkommande förfrågningar med
  automatiskt felsvar vid ogiltig indata.

---

## Icke-funktionella krav

### TMBF-NFR-01 — Testbarhet

- **TMBF-NFR-01.1** Mallen ska tillhandahålla ett fungerande exempel på integrationstester mot
  en simulerad bakomliggande tjänst, som täcker normalfall, valideringsfel och
  otillgänglighetsfel.

### TMBF-NFR-02 — Säkerhet

- **TMBF-NFR-02.1** Mallen ska demonstrera att auktoriseringsuppgifter vidarebefordras till
  bakomliggande tjänst utan att själva verifieras i BFF-lagret.

---

## API-gränssnitt (översikt)

| API | Målgrupp | Specifikationsartefakt |
|---|---|---|
| Exempel-BFF-API | Illustrativt, ingen riktig konsument | Ingen specifikation — ersätts av den nya BFF:ns riktiga kontrakt |

---

## Integration med en bakomliggande regeltjänst

Mallen demonstrerar ett enda, generiskt integrationsmönster mot en bakomliggande tjänst via ett
REST-klientgränssnitt knutet till en enskild konfigurationsegenskap. Ett team som bygger en ny
BFF på mallen ersätter detta med sin egen bakomliggande tjänst och datamodell.

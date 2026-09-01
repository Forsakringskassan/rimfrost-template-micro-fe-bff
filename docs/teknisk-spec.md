# Teknisk spec — BFF-mall för mikrofrontend (TMBF)

## Översikt

Quarkus-mall för en synkron REST BFF, utan egen datalagring och utan meddelandeintegration.
Demonstrerar ett enda REST-klientintegrationsmönster mot en bakomliggande tjänst. Avsedd att
kopieras, inte driftsättas som den är.

## Komponentstruktur

```text
src/main/java/se/fk/github/templatebff
├── TemplateBFFController      # Exempeländpunkter
├── integration/BackendClient   # REST-klientmönster mot en bakomliggande tjänst
├── GlobalExceptionMapper (motsv.) # Enhetlig felmappning
└── model/                     # TaskRequest, PatchTaskRequest, PatchTaskBody
```

## API-specifikationer

Illustrativa exempeländpunkter, ingen riktig specifikation:

| Metod | Sökväg | Beskrivning |
|---|---|---|
| GET | `/api/health` | Egen hälsokontroll |
| POST | `/api/task` | Exempel: hämta ärendedata |
| PATCH | `/api/task` | Exempel: uppdatera ärendedata |
| POST | `/api/task/done` | Exempel: slutföra ärende (kräver auktoriseringsuppgifter) |
| GET | `/api/uppgiftsbeskrivning` | Exempel: hämta hjälptext |

## Kafka-integration

Ingen. Mallen ger inget exempel på meddelandeintegration.

## Konfiguration

| Egenskap | Beskrivning | Standardvärde |
|---|---|---|
| `quarkus.rest-client.backend.url` (`BACKEND_URL`) | Bas-URL till bakomliggande tjänst | `http://localhost:8080/regel/bekraftabeslut` |
| `CORS_ORIGINS` | Tillåtna ursprung för CORS | Lokala mikrofrontend-portar |
| `quarkus.http.port` | Lyssningsport | `9009` |

## Liveness

`/api/health` (egen), samt `/q/health` från det delade ramverket.

## Kända begränsningar och framtida arbete

| Begränsning | Föreslagen åtgärd |
|---|---|
| Ingen README finns i repot som förklarar att det är en mall och hur den ska användas | Återskapa en introduktionsdokumentation |
| Svarsdata är otypad (skickas vidare som rå JSON) — inget exempel finns på svarsmodellering | Lägg till ett exempel om detta är ett vanligt behov |
| Inget exempel på meddelandeintegration finns, trots att flera verkliga BFF:er i plattformen använder sådan | Bedöm om ett Kafka-exempel bör läggas till |
| Behållarens namn/grupp är platshållarvärden som måste bytas ut manuellt | Automatisera eller tydliggör bytet i dokumentationen |

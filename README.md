# Micro Frontend BFF Template

En template för Backend-For-Frontend (BFF) server som fungerar som mellanlager mellan en micro frontend och backend-API:er.

## 🚀 Features

- **Express-baserad BFF** - Snabb och lätt Node.js server
- **TypeScript** - Fullt typad utvecklingsupplevelse
- **Hot Reload** - Automatisk omstart vid kodändringar under utveckling
- **CORS-hantering** - Färdigkonfigurerat för frontend-anrop
- **Request logging** - Inbyggd loggning av alla requests
- **Data transformation** - Exempel på hur man transformerar backend-data (snake_case → camelCase)
- **Error handling** - Robust felhantering och logging
- **ESLint & Prettier** - Kodkvalitet och formatering

## 📋 Förutsättningar

- Node.js 24+ (eller justera `@tsconfig/node24` i package.json)
- npm eller annan pakethanterare

## 🔧 Kom igång

1. **Använd templaten**
   - Klicka på "Use this template" på GitHub
   - Skapa ditt nya repository

2. **Installera beroenden**
   ```bash
   npm install
   ```

3. **Konfigurera miljövariabler**
   ```bash
   # Kopiera .env.example till .env
   cp .env.example .env
   
   # Redigera .env med dina värden
   ```

4. **Starta utvecklingsserver**
   ```bash
   npm run dev
   ```

   Servern startar på `http://localhost:9002` (eller din konfigurerade PORT)

## 📁 Projektstruktur

```
├── index.ts                      # Huvudfil med Express-server och routes
├── utils/
│   └── transformBackendResponse.ts  # Dataomvandlingsfunktioner
├── package.json                  # Projektberoenden och scripts
├── tsconfig.json                 # TypeScript-konfiguration
├── eslint.config.js              # ESLint-regler
└── .env.example                  # Exempel på miljövariabler
```

## 📜 Tillgängliga scripts

```bash
npm run dev          # Starta dev-server med hot reload
npm run build        # Bygg TypeScript till JavaScript
npm start            # Starta produktionsserver (kräver build först)
npm run type-check   # Kontrollera TypeScript-typer utan att bygga
npm run lint         # Kör ESLint
npm run lint:fix     # Fixa ESLint-problem automatiskt
npm run format       # Formatera kod med Prettier
npm run format:check # Kontrollera kodformatering
```

## 🌐 API Endpoints

### Health Check
```
GET /api/health
```
Returnerar serverstatus


## 🔒 Miljövariabler

| Variabel | Beskrivning | Default |
|----------|-------------|---------|
| `PORT` | Port som BFF-servern lyssnar på | `9002` |
| `BACKEND_BASE_URL` | Bas-URL till backend-API | `http://localhost:8890` |

## 🚢 Deployment

1. Bygg projektet:
   ```bash
   npm run build
   ```

2. Sätt miljövariabler i din deployment-miljö

3. Starta servern:
   ```bash
   npm start
   ```

## 💡 Tips

- **Development**: Använd `npm run dev` för snabb utveckling med auto-reload
- **Type Safety**: Definiera TypeScript-interfaces för din data i separata filer
- **Error Logging**: Överväg att lägga till ett logging-library i produktion (t.ex. Winston, Pino)
- **Validation**: Lägg till request/response validation (t.ex. Zod, Joi)
- **Testing**: Lägg till test-framework (t.ex. Jest, Vitest)

## 📝 License

ISC
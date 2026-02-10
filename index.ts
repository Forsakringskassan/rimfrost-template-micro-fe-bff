import express from 'express';
import path from "path";
import { fileURLToPath } from "node:url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 9002;

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Logging of all incoming requests
app.use((req, res, next) => {
    console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
    next();
});

app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Methods", "GET, PUT, POST, PATCH, DELETE, OPTIONS");
    res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
    if (req.method === "OPTIONS") {
        res.sendStatus(200);
    } else {
        next();
    }
});

app.get("/api/health", (req, res) => {
    console.log("Health check called");
    res.json({ status: "ok", timestamp: new Date().toISOString() });
});

// Endpoint for fetching a random cat fact from meowfacts API
app.get("/api/cat-fact", async (req, res) => {
    try {
        console.log("Fetching cat fact from meowfacts API");
        
        const response = await fetch("https://meowfacts.herokuapp.com");
        
        if (!response.ok) {
            console.error(`Meowfacts API error: ${response.status}`);
            return res.status(response.status).json({ error: "Failed to fetch cat fact from external API" });
        }
        
        const data = await response.json();
        console.log("Cat fact fetched successfully");
        res.json(data);
    } catch (error) {
        console.error("Error fetching cat fact:", error);
        res.status(500).json({ error: "Internal server error", message: error instanceof Error ? error.message : String(error) });
    }
});


app.listen(PORT, () => {
    console.log(`BFF server running on port ${PORT}`);
});
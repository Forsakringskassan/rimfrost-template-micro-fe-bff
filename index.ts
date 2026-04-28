import express from 'express';
// import { transformBackendResponse } from '#utils/transformBackendResponse.js';
import { requireEnv } from '#utils/requireEnv.js';

const app = express();
const PORT = requireEnv("PORT");
const BE_URL = requireEnv("BE_URL");
const BE_RULE_PATH = process.env.BE_RULE_PATH || "regel/bekraftabeslut";

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

app.get("/api/task/:handlaggningId", async (req, res) => {
    const { handlaggningId } = req.params;

    try {
        const response = await fetch(
            `${BE_URL}/${BE_RULE_PATH}/${handlaggningId}`
        );
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const data = await response.json();
        // Transform data as needed before sending to frontend
        // res.json(transformBackendResponse(data));
    } catch (error) {
        console.error(`Error fetching decision data for handlaggningId ${handlaggningId}:`, error);
        res.status(500).json({ error: "Internal server error" });
    }
});

app.patch("/api/task/:handlaggningId", async (req, res) => {
    const { handlaggningId } = req.params;

    // Example body, change as appropriate
    const { ersattningId, yrkandestatus } = req.body;
    const patchBody = JSON.stringify({ ersattning_id: ersattningId, yrkandestatus });

    try {
        const response = await fetch(
            `${BE_URL}/${BE_RULE_PATH}/${handlaggningId}`,
            {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: patchBody
            }
        );
        if (!response.ok) {
            const errorBody = await response.text();
            console.error(`Backend returned ${response.status}: ${errorBody}`);
            throw new Error(`HTTP ${response.status}`);
        }
        res.status(200).end();
    } catch (error) {
        console.error(`Error patching data for handlaggningId ${handlaggningId}:`, error);
        res.status(500).json({ error: "Internal server error"});
    }
});

app.post("/api/task/:handlaggningId/done", async (req, res) => {
    const { handlaggningId } = req.params;

    try {
        const response = await fetch(
            `${BE_URL}/${BE_RULE_PATH}/${handlaggningId}/done`,
            {
                method: "POST",
                headers: {
                    ...(req.headers.authorization ? { authorization: req.headers.authorization } : {}),
                },
            }
        );
        if (!response.ok) {
            const errorBody = await response.text();
            console.error(`Backend /done returned ${response.status}: ${errorBody}`);
            throw new Error(`HTTP ${response.status}`);
        }
        res.status(204).end();
    } catch (error) {
        console.error(`Error calling /done for handlaggningId ${handlaggningId}:`, error);
        res.status(500).json({ error: "Internal server error" });
    }
});

app.get("/api/uppgiftsbeskrivning", async (req, res) => {
    const backendUrl = `${BE_URL}/${BE_RULE_PATH}/utokadUppgiftsbeskrivning`;

    try {
        const response = await fetch(backendUrl, { method: 'GET' });
        if (!response.ok) {
            const errorText = await response.text();
            return res.status(response.status).json({ error: "Failed to fetch from backend", details: errorText });
        }
        const data = await response.json();
        res.json(data);
    } catch (error) {
        res.status(502).json({ error: "Backend service unavailable" });
    }
});

app.listen(PORT, () => {
    console.log(`BFF server running on port ${PORT}`);
});
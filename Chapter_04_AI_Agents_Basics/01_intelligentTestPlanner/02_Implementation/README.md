# Jira Test Plan Generator

Minimal React + Express app for reading one Jira issue and producing the supplied test-plan template.

## Setup

1. From this folder, run `npm run install:all`.
2. Set Jira credentials in the parent workspace `.env` (this app reads it without copying secrets):
   - `JIRA_BASE_URL`
   - `JIRA_EMAIL`
   - `JIRA_API_TOKEN`
3. Optional AI setting: `GROQ_MODEL` (defaults to `openai/gpt-oss-20b`). You can enter a Groq API key on the Settings page; it is held in memory only for the current browser session and sent to the local backend for API calls.

## Run

- `npm run dev`
- Open `http://127.0.0.1:5173`
- Backend: `http://127.0.0.1:3001`

## Use

Enter a Jira key such as `QA-123`, then generate the draft. Add your Groq API key in Settings and test the connection to use AI generation. Without a key, the app uses deterministic template fallback.

## Verify

Jira and Groq calls require real credentials. Without them, UI and template fallback can be checked using mock/unit checks; no live success is claimed in that case.

import React, { useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

function App() {
  const [page, setPage] = useState('main');
  const [issueKey, setIssueKey] = useState('');
  const [plan, setPlan] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [settings, setSettings] = useState({ baseUrl: '', email: '', token: '', groqApiKey: '', groqModel: 'llama-3.3-70b-versatile' });
  const [connection, setConnection] = useState('');
  const [groqConnection, setGroqConnection] = useState('');
  const [planMode, setPlanMode] = useState('');

  async function generate(event) {
    event.preventDefault();
    if (loading) return;
    setError(''); setPlan(''); setLoading(true);
    try {
      const r = await fetch('/api/generate', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ issueKey, ...settings }) });
      const data = await r.json();
      if (!r.ok) throw new Error(data.error || 'Unable to generate the test plan.');
      setPlan(data.plan);
      setPlanMode(data.mode);
    } catch (e) { setError(e.message); } finally { setLoading(false); }
  }

  async function testConnection() {
    setConnection('Testing...');
    try {
      const r = await fetch('/api/jira/test', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(settings) });
      const data = await r.json();
      setConnection(r.ok ? data.message : data.error);
    } catch { setConnection('Unable to reach the backend.'); }
  }

  async function testGroqConnection() {
    setGroqConnection('Testing...');
    try {
      const r = await fetch('/api/groq/test', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ groqApiKey: settings.groqApiKey }) });
      const data = await r.json();
      setGroqConnection(r.ok ? data.message : data.error);
    } catch { setGroqConnection('Unable to reach the backend.'); }
  }

  return <main>
    <nav>
      <button className={page === 'main' ? 'active' : ''} onClick={() => setPage('main')}>Main</button>
      <button className={page === 'settings' ? 'active' : ''} onClick={() => setPage('settings')}>Settings</button>
    </nav>

    {page === 'main' ? <div className="layout">
      <section className="card prompt-card">
        <h1>Jira Test Plan</h1>
        <form onSubmit={generate}>
          <label htmlFor="issue">Jira issue ID</label>
          <input id="issue" value={issueKey} onChange={e => setIssueKey(e.target.value)} placeholder="QA-123" disabled={loading} />
          <button className="primary" type="submit" disabled={loading}>{loading ? 'Generating...' : 'Generate Test Plan'}</button>
        </form>
        {error && <div className="state error">{error}</div>}
        {!plan && !error && !loading && <div className="state">Enter a Jira issue ID to begin.</div>}
      </section>
      <section className="card result-card">
        <h2>Reply with Test Plan</h2>
        {loading ? <div className="state">Loading Jira issue and generating draft...</div> : plan ? <><div className="plan-mode">Generated with {planMode}</div><pre>{plan}</pre></> : <div className="empty">No test plan yet</div>}
      </section>
    </div> : <div className="layout settings-layout">
      <section className="card settings-card">
        <h2>JIRA Connection</h2>
        <label>Token<input type="password" value={settings.token} onChange={e => setSettings({...settings, token: e.target.value})} /></label>
        <label>Email<input type="email" value={settings.email} onChange={e => setSettings({...settings, email: e.target.value})} /></label>
        <label>Base URL<input value={settings.baseUrl} onChange={e => setSettings({...settings, baseUrl: e.target.value})} placeholder="https://...atlassian.net" /></label>
        <button className="primary" type="button" onClick={testConnection}>Test Connection</button>
        {connection && <div className="state">{connection}</div>}
      </section>
      <section className="card settings-card">
        <h2>Groq Configuration</h2>
        <label>Groq API Key<input type="password" autoComplete="new-password" value={settings.groqApiKey} onChange={e => setSettings({...settings, groqApiKey: e.target.value})} placeholder="gsk_..." /></label>
        <label>Model<input value={settings.groqModel} onChange={e => setSettings({...settings, groqModel: e.target.value})} /></label>
        <button className="primary" type="button" onClick={testGroqConnection}>Test Groq Connection</button>
        {groqConnection && <div className="state">{groqConnection}</div>}
        <p>Your key is held in memory and sent only to this local backend; it is not saved in browser storage. Without a key, the app uses the template fallback.</p>
      </section>
    </div>}
  </main>;
}

createRoot(document.getElementById('root')).render(<App />);

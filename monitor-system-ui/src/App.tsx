import { useEffect, useState } from 'react';
import type { Device } from './types/device';
import { Login } from './components/Login';
import './App.css';
import { DeviceForm } from './components/DeviceForm';

function App() {
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState('');
  const [currentTab, setCurrentTab] = useState<'dashboard' | 'logs'>('dashboard');
  const [logs, setLogs] = useState<any[]>([]); 
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editForm, setEditForm] = useState({ name: '', location: '', type: '' });

  const fetchDevices = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/devices");
      if (!response.ok) throw new Error("Erro ao ligar à API");
      const data = await response.json();
      setDevices(data);
      setLoading(false);
    } catch (error) {
      console.error("Erro no Java:", error);
      setLoading(false);
    }
  };

  const fetchLogs = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/devices/logs");
      const contentType = response.headers.get("content-type");
      if (!response.ok || !contentType?.includes("application/json")) return;
      const data = await response.json();
      setLogs(data);
    } catch (error) {
      console.error("Erro ao buscar logs:", error);
    }
  };

  const handleUpdate = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8080/api/devices/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(editForm)
      });
      if (response.ok) {
        setEditingId(null);
        fetchDevices();
      }
    } catch (error) {
      console.error("Erro:", error);
    }
  };

  const handleToggleStatus = async (id: number, currentStatus: boolean) => {
    try {
      const response = await fetch(`http://localhost:8080/api/devices/${id}/status`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ active: !currentStatus })
      });
      if (response.ok) {
        fetchDevices(); 
      } else {
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          const errorData = await response.json();
          alert("⚠️ Bloqueio: " + (errorData.message || "Ação não permitida"));
        }
      }
    } catch (error) {
      console.error("Erro:", error);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("⚠️ Deseja apagar este dispositivo?")) {
      await fetch(`http://localhost:8080/api/devices/${id}`, { method: 'DELETE' });
      fetchDevices();
    }
  };

  useEffect(() => {
    if (isLoggedIn) {
      fetchDevices();
      const interval = setInterval(fetchDevices, 5000);
      return () => clearInterval(interval);
    }
  }, [isLoggedIn]);

  if (!isLoggedIn) return <Login onLogin={(r) => { setRole(r); setIsLoggedIn(true); }} />;

  const getDeviceIcon = (type: string) => {
    const t = type.toLowerCase();
    if (t.includes('camera') || t.includes('câmera')) return '📹';
    if (t.includes('sensor')) return '🚨';
    if (t.includes('portão')) return '🚪';
    return '🛠️';
  };

  return (
    <div className="dashboard-container">
      <header className="header">
        <div className="header-brand-nav">
          <div className="logo-section">
            <h1>🛰️ Security Monitor</h1>
            <p>Painel de Controle</p>
          </div>
          {role === 'admin' && (
            <nav className="nav-tabs">
              <button className={currentTab === 'dashboard' ? 'active' : ''} onClick={() => setCurrentTab('dashboard')}>Monitoramento</button>
              <button className={currentTab === 'logs' ? 'active' : ''} onClick={() => { setCurrentTab('logs'); fetchLogs(); }}>Histórico</button>
            </nav>
          )}
        </div>

        <div className="profile-wrapper">
          <img src="https://github.com/yan-borges.png" className="profile-avatar-clickable" onClick={() => setShowProfileMenu(!showProfileMenu)} alt="Perfil" />
          {showProfileMenu && (
            <div className="profile-dropdown-menu">
              <div className="dropdown-header">
                <span className="user-name">Yan Borges</span>
                <span className="user-role-text">{role.toUpperCase()}</span>
              </div>
              <div className="dropdown-divider"></div>
              <button className="dropdown-item">⚙️ Configurações</button>
              <button className="dropdown-item logout-item" onClick={() => setIsLoggedIn(false)}>🚪 Sair</button>
            </div>
          )}
        </div>
      </header>

      <main className="main-content">
        {(role !== 'admin' || currentTab === 'dashboard') ? (
          <>
            {role === 'admin' && <section className="admin-panel"><DeviceForm onSuccess={fetchDevices} /></section>}
            <div className="device-grid">
              {devices.map((device) => (
                <div key={device.id} className="device-card">
                  <div className={`status-line ${device.active ? 'online' : 'offline'}`} />
                  {editingId === device.id ? (
                    <div className="edit-mode-form">
                      <input value={editForm.name} onChange={e => setEditForm({...editForm, name: e.target.value})} placeholder="Nome" />
                      <input value={editForm.location} onChange={e => setEditForm({...editForm, location: e.target.value})} placeholder="Localização" />
                      <div className="edit-actions">
                        <button onClick={() => handleUpdate(device.id)}>✅</button>
                        <button onClick={() => setEditingId(null)}>❌</button>
                      </div>
                    </div>
                  ) : (
                    <>
                      <div className="card-header">
                        <span className="device-type-icon">{getDeviceIcon(device.type)}</span>
                        <h3>{device.name}</h3>
                        {role === 'admin' && <button className="edit-icon-btn" onClick={() => { setEditingId(device.id); setEditForm({ name: device.name, location: device.location, type: device.type }); }}>✏️</button>}
                      </div>
                      <div className="device-details"><p>📍 {device.location}</p><p>🏷️ {device.type}</p></div>
                      <div className={`badge ${device.active ? 'bg-green' : 'bg-red'}`} style={{ cursor: role === 'admin' ? 'pointer' : 'default' }} onClick={() => role === 'admin' && handleToggleStatus(device.id, device.active)}>{device.active ? 'ONLINE' : 'OFFLINE'}</div>
                      {role === 'admin' && <button className="delete-btn" onClick={() => handleDelete(device.id)}>🗑️ Remover</button>}
                    </>
                  )}
                </div>
              ))}
            </div>
          </>
        ) : (
          <section className="logs-section">
            <div className="table-wrapper">
              <table className="logs-table">
                <thead><tr><th>Data/Hora</th><th>Dispositivo</th><th>Ação</th><th>Mensagem</th></tr></thead>
                <tbody>
                  {logs.map((log) => (
                    <tr key={log.id}>
                      <td>{new Date(log.timestamp).toLocaleString('pt-BR')}</td>
                      <td>{log.device?.name || "Eliminado"}</td>
                      <td><span className="log-tag">{log.action}</span></td>
                      <td>{log.details || log.message}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}
      </main>
    </div>
  );
}

export default App;
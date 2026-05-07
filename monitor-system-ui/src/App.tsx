import { useEffect, useState } from 'react';
import type { Device } from './types/device';
import { Login } from './components/login';
import './App.css';
import { DeviceForm } from './components/DeviceForm';

function App() {
  // --- ESTADOS ---
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState(''); // 'admin' ou 'user'

  // --- FUNÇÃO PARA BUSCAR DISPOSITIVOS (GET) ---
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

  // --- FUNÇÃO PARA EXCLUIR (DELETE) - APENAS ADMIN ---
  const handleDelete = async (id: number) => {
  // Confirmar antes de fazer asneira
  const confirmed = window.confirm("⚠️ ATENÇÃO: Esta ação irá apagar o dispositivo e TODOS os seus logs de segurança. Deseja continuar?");
  
  if (confirmed) {
    try {
      // Opcional: podes colocar um estado de 'deleting' aqui para mostrar um spinner
      const response = await fetch(`http://localhost:8080/api/devices/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        // Atualização optimista: remove da lista imediatamente
        setDevices(prev => prev.filter(device => device.id !== id));
        // Usar um Toast seria mais profissional, mas o alert resolve por agora
        console.log("Dispositivo e dependências removidos com sucesso.");
      } else {
        const errorData = await response.json();
        alert(`Erro ao excluir: ${errorData.message || 'Erro desconhecido'}`);
      }
    } catch (error) {
      console.error("Erro na comunicação com o servidor:", error);
      alert("Servidor offline ou erro de rede.");
    }
  }
};

  // --- CONTROLO DE ATUALIZAÇÃO ---
  useEffect(() => {
    if (isLoggedIn) {
      fetchDevices();
      const interval = setInterval(fetchDevices, 5000); // Atualiza a cada 5s
      return () => clearInterval(interval);
    }
  }, [isLoggedIn]);

  // --- LÓGICA DE LOGIN ---
  const handleLogin = (userRole: string) => {
    setIsLoggedIn(true);
    setRole(userRole);
  };

  // 1. Se não estiver logado, mostra a tela de Login
  if (!isLoggedIn) {
    return <Login onLogin={handleLogin} />;
  }

  const getDeviceIcon = (type: string) => {
  const t = type.toLowerCase();
  if (t.includes('camera') || t.includes('câmera')) return '📹';
  if (t.includes('sensor')) return '🚨';
  if (t.includes('portão') || t.includes('gate')) return '🚪';
  if (t.includes('alarme')) return '🔔';
  return '🛠️'; // Ícone padrão para outros tipos
};

  // 2. Se estiver logado, mostra o Dashboard
  return (
    <div className="dashboard-container">
      <header className="header">
        <div className="header-info">
          <h1>🛰️ Security Monitor</h1>
          <p>Sessão: <strong>{role.toUpperCase()}</strong></p>
        </div>
        <button onClick={() => setIsLoggedIn(false)} className="logout-btn">
          Sair
        </button>
      </header>
      {/* SEÇÃO EXCLUSIVA PARA ADMIN: FORMULÁRIO DE CADASTRO */}
      {role === 'admin' && (
        <section className="admin-panel">
          <DeviceForm onSuccess={fetchDevices} />
        </section>
      )}

      {loading ? (
        <div className="status-msg">A ligar ao servidor...</div>
      ) : (
        <div className="device-grid">
          {devices.map((device) => (
  <div key={device.id} className="device-card">
    <div className={`status-line ${device.active ? 'online' : 'offline'}`} />
    
    {/* AQUI É ONDE O ÍCONE ENTRA */}
    <div className="card-header">
      <span className="device-type-icon">{getDeviceIcon(device.type)}</span>
      <h3>{device.name}</h3>
    </div>

    <div className="device-details">
      <p>📍 {device.location}</p>
      <p>🏷️ {device.type}</p>
    </div>

              <div className={`badge ${device.active ? 'bg-green' : 'bg-red'}`}>
                {device.active ? 'ONLINE' : 'OFFLINE'}
              </div>

              {/* AÇÃO DE ADMIN: Botão de Excluir */}
              {role === 'admin' && (
                <button 
                  className="delete-btn" 
                  onClick={() => handleDelete(device.id)}
                >
                  🗑️ Remover
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default App;
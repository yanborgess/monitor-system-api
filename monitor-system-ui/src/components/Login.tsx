import React, { useState } from 'react';
import './Login.css'; 

interface LoginProps {
  onLogin: (role: string) => void;
}

export function Login({ onLogin }: LoginProps) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // Simulação de autenticação (depois conectaremos ao Java)
    if (username === 'admin' && password === 'admin123') {
      onLogin('admin');
    } else if (username === 'user' && password === 'user123') {
      onLogin('user');
    } else {
      alert("Credenciais inválidas! Tente admin/admin123 ou user/user123");
    }
  };

  return (
    <div className="login-container">
      <form className="login-box" onSubmit={handleSubmit}>
        <h2>🛡️ Security System</h2>
        <p>Faça login para acessar o monitoramento</p>
        
        <input 
          type="text" 
          placeholder="Usuário" 
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required 
        />
        
        <input 
          type="password" 
          placeholder="Senha" 
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required 
        />
        
        <button type="submit">Acessar Painel</button>
      </form>
    </div>
  );
}